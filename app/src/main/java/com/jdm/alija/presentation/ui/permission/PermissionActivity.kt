package com.jdm.alija.presentation.ui.permission

import android.Manifest
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.telecom.TelecomManager
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.jdm.alija.R
import com.jdm.alija.base.BaseActivity
import com.jdm.alija.databinding.ActivityPermissionBinding
import com.jdm.alija.dialog.CommonDialog
import com.jdm.alija.presentation.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionActivity : BaseActivity<ActivityPermissionBinding>() {
    override val layoutResId: Int
        get() = R.layout.activity_permission
    //RoleManager 방식은 api 29 부터 사용할 수 있음
    private val permissionViewModel: PermissionViewModel by viewModels()
    private val roleManager: RoleManager? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getSystemService(ROLE_SERVICE) as RoleManager
        } else null
    }
    private val permissionList = mutableListOf<String>(
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.SEND_SMS,
    )
    private lateinit var phonePermission: Array<String>
    private val telecomManager: TelecomManager by lazy { getSystemService(TELECOM_SERVICE) as TelecomManager }

    private val isDefaultDialer get() = packageName.equals(telecomManager.defaultDialerPackage)
    private val changeDefaultDialerIntent
        get() = if (isDefaultDialer) {
            Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                roleManager!!.createRequestRoleIntent(RoleManager.ROLE_DIALER)
            } else {
                Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).apply {
                    putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
                }
            }
        }

    //startActivityForResult deprecate 이후로 ActivityResultLauncher 를 이용하여 같은 기능을 수행함
    //이 객체는 무조건 액티비티 생성시에 같이 생성되어야 함
    //onCreate 안에서 객체를 생성할 경우 크래시 발생
    private val changeDefaultDialerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            initView()
        }
    private val phoneLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

    }
    private val overlayLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (Settings.canDrawOverlays(this)) {

        } else {

        }
    }

    override fun initView() {
        binding.llPermission.visibility = View.INVISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE)
            permissionList.add(Manifest.permission.READ_PHONE_NUMBERS)

        } else {
            permissionList.add(Manifest.permission.READ_PHONE_STATE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionList.add((Manifest.permission.POST_NOTIFICATIONS))
        }
        phonePermission = permissionList.toTypedArray()

        //binding.llBasePhone.visibility = if (isDefaultDialer) View.GONE else View.VISIBLE

    }
    fun checkPermission() {
        if (isPermissionAllow(phonePermission)) {
            if (Settings.canDrawOverlays(this)) {
                if (isWhiteList()) {
                    goToMainActivity()
                } else {
                    binding.llPermission.visibility = View.VISIBLE
                }
            } else {
                binding.llPermission.visibility = View.VISIBLE
            }
        } else {
            binding.llPermission.visibility = View.VISIBLE
        }
    }

    override fun subscribe() {
        permissionViewModel.splashSideEffect.observe(this) {
            when (it) {
                ForceVersionUpdate -> {
                    showForceUpdateDialog()
                }
                SelectVersionUpdate -> {
                    showSelectUpdateDialog()
                }
                MoveMain -> {
                    checkPermission()
                    //checkNotiPermission()
                }
            }
        }
    }
    private fun isWhiteList(): Boolean {
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        return pm.isIgnoringBatteryOptimizations(packageName)
    }
    fun showForceUpdateDialog() {
        CommonDialog(
            title = getString(R.string.str_notification_update),
            msg = getString(R.string.str_update_guide),
            rightText = getString(R.string.str_exit_app),
            rightClick = {
                exitApp()
            },
            isCancel = false
        ).show(supportFragmentManager, CommonDialog.TAG)
    }
    fun showSelectUpdateDialog() {
        CommonDialog(
            title = getString(R.string.str_notification_update),
            msg = getString(R.string.str_update_guide),
            rightText = getString(R.string.str_do_next),
            rightClick = {
                checkPermission()
            },
            isCancel = false
        ).show(supportFragmentManager, CommonDialog.TAG)
    }
    fun goToMainActivity() {
        val intent = Intent(this@PermissionActivity, MainActivity::class.java)
        goToActivity(intent)
        finish()
    }
    override fun initEvent() {
        with(binding){
            btPermissionOk.setOnClickListener {

                if (isPermissionAllow(phonePermission)) {
                    if (Settings.canDrawOverlays(this@PermissionActivity)) {
                        if (isWhiteList()) {
                            goToMainActivity()
                        } else {
                            CommonDialog(
                                title = "배터리 최적화 예외 앱 지정",
                                msg = "알리자 앱이 대기모드 또는 절전모드에서 안정적인 콜백 성능을 보장하기 위해서는 해당 앱을 배터리 사용량 최적화 목록에서 \"제외\"해야 합니다\n [확인] 버튼을 누른 후 시스템 알림 대화 상자가 뜨면 [허용] 을 선택해 주세요",
                                rightText = "확인",
                                rightClick = {
                                    val intent = Intent()
                                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                                    intent.setData(Uri.parse("package:" + packageName))
                                    startActivity(intent);
                                }
                            ).show(supportFragmentManager, CommonDialog.TAG)
                        }
                    } else {
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+this@PermissionActivity.packageName))
                        overlayLauncher.launch(intent)
                    }
                } else {
                    Log.e(TAG, "permission deny")
                    phoneLauncher.launch(phonePermission)
                }
                /*
                Log.e(TAG, "${changeDefaultDialerIntent.toString()}")
                if (isDefaultDialer) {
                    if (isPermissionAllow(phonePermission)) {
                        //val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+this@PermissionActivity.packageName))
                        val intent = Intent(this@PermissionActivity, MainActivity::class.java)
                        goToActivity(intent)
                    } else {
                        Log.e(TAG, "permission deny")
                        phoneLauncher.launch(phonePermission)
                    }
                } else {
                    changeDefaultDialerLauncher.launch(changeDefaultDialerIntent)
                }

                 */


            }
        }
    }

    override fun initData() {
        permissionViewModel.getAppVersion(this)
    }

    companion object {
        val TAG = this.javaClass.simpleName
        val ForceVersionUpdate = 1
        val SelectVersionUpdate = 2
        val MoveMain = 3
    }

}