package com.jdm.alija.presentation.ui.main

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jdm.alija.R
import com.jdm.alija.presentation.service.SmsService
import com.jdm.alija.base.BaseActivity
import com.jdm.alija.base.BaseFragment
import com.jdm.alija.databinding.ActivityMainBinding
import com.jdm.alija.presentation.ui.contract.ContractFragment
import com.jdm.alija.presentation.ui.home.HomeFragment
import com.jdm.alija.presentation.util.Const.ACTION_START_LOCATION_SERVICE
import com.jdm.alija.presentation.util.Const.ACTION_STOP_LOCATION_SERVICE
import com.jdm.alija.presentation.util.Const.SERVICE_NAME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutResId: Int
        get() = R.layout.activity_main
    private val mainViewModel: MainViewModel by viewModels()
    private val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {

    }
    private val contractFragment: ContractFragment by lazy { ContractFragment.newInstance() }
    private val homeFragment: HomeFragment by lazy { HomeFragment.newInstance() }
    private val bottomIcons: List<AppCompatImageView> by lazy {
        listOf(R.id.iv_home, R.id.iv_contract).map { findViewById(it) }
    }
    private val bottomTexts: List<AppCompatTextView> by lazy {
        listOf(R.id.tv_home, R.id.tv_contract).map { findViewById(it) }
    }
    override fun initView() {
        /*
        //val receiver = CallComplete()
        //val filter = IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        //this.registerReceiver(receiver, filter)
        val tm: TelephonyManager = (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            /*
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PERMISSION_GRANTED) {
                launcher.launch(Manifest.permission.READ_PHONE_STATE)
                return
            }

             */

            tm.registerTelephonyCallback(
                mainExecutor,
                object : TelephonyCallback(), TelephonyCallback.CallStateListener {
                    override fun onCallStateChanged(state: Int) {
                        when(state) {
                            /*
                            TelephonyManager.CALL_STATE_IDLE -> {
                                val phoneNumber = intent.extras?.getString("incoming_number")
                                Log.e(CallComplete.TAG, "${phoneNumber}")
                            }
                             */
                            TelephonyManager.CALL_STATE_IDLE -> {
                                Log.e(TAG,"call state idle")
                            }
                            TelephonyManager.CALL_STATE_RINGING -> {
                                Log.e(TAG,"call state ringing")
                            }
                            TelephonyManager.CALL_STATE_OFFHOOK -> {
                                Log.e(TAG,"call state offhook")
                            }
                            else -> {}
                        }
                    }
                }
            )
        } else {
            tm.listen(object : PhoneStateListener() {
                override fun onCallStateChanged(state: Int, phoneNumber: String?) {

                    when (state) {
                        TelephonyManager.CALL_STATE_IDLE -> {
                            Log.e(TAG,"call state idle")
                            /*
                            val phoneNumber2 = intent.extras?.getString("incoming_number")
                            Log.e(CallComplete.TAG, "${Build.VERSION.SDK_INT}-${phoneNumber2}")
                            val smsUri = Uri.parse("smsto:" + phoneNumber)
                            val intent = Intent(Intent.ACTION_SENDTO)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.setData(smsUri)
                            intent.putExtra("sms_body","test")
                            startActivity(intent)

                             */
                        }
                        TelephonyManager.CALL_STATE_RINGING -> {
                            Log.e(TAG,"call state ringing")
                        }
                        TelephonyManager.CALL_STATE_OFFHOOK -> {
                            Log.e(TAG,"call state offhook")
                        }
                        else -> {}
                    }

                }
            }, PhoneStateListener.LISTEN_CALL_STATE)
        }

         */
    }

    override fun subscribe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.effect.collect { effect ->
                    when (effect) {
                        is MainContract.MainSideEffect.ShowFragment -> {
                            when (effect.tag) {
                                HomeFragment.TAG -> { showFragment(homeFragment, HomeFragment.TAG) }
                                ContractFragment.TAG -> { showFragment(contractFragment, ContractFragment.TAG) }
                            }
                        }
                        is MainContract.MainSideEffect.StartService -> {
                            val intent = Intent(applicationContext, SmsService::class.java)
                            intent.setAction(ACTION_START_LOCATION_SERVICE)
                            startService(intent)
                        }
                        is MainContract.MainSideEffect.StopService -> {
                            val intent = Intent(applicationContext, SmsService::class.java)
                            intent.setAction(ACTION_STOP_LOCATION_SERVICE)
                            startService(intent)
                        }
                    }
                }
            }
        }
    }

    override fun initEvent() {
        binding.llHome.setOnClickListener {
            mainViewModel.setEvent(MainContract.MainEvent.OnClickHomeButton)
        }
        binding.llContract.setOnClickListener {
            mainViewModel.setEvent(MainContract.MainEvent.OnClickContractButton)
        }
        /*
        binding.btStart.setOnClickListener {
            val intent = Intent(applicationContext, SmsService::class.java)
            intent.setAction(ACTION_START_LOCATION_SERVICE)
            startService(intent)


            val smsUri = Uri.parse("smsto:" + "01051391216")
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setData(smsUri)
            intent.putExtra("sms_body", "test")
            startActivity(intent)



        }
        binding.btStop.setOnClickListener {
            val intent = Intent(applicationContext, SmsService::class.java)
            intent.setAction(ACTION_STOP_LOCATION_SERVICE)
            startService(intent)
        }

         */
    }

    override fun initData() {
        showFragment(homeFragment, HomeFragment.TAG)
    }

    override fun onResume() {
        super.onResume()

    }
    private fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commitAllowingStateLoss()
            if (fm is BaseFragment<*>) {
                fm.removeBackPress()
            }
        }
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()
            if (it is BaseFragment<*>) {
                it.setOnBackPress()
            }
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction().add(R.id.fv_main, fragment, tag)
                .commitAllowingStateLoss()
        }

        bottomIconAllOff()

        when (tag) {
            HomeFragment.TAG -> {
                bottomIcons[0].isSelected = true
                bottomTexts[0].isSelected = true
            }
            ContractFragment.TAG -> {
                bottomIcons[1].isSelected = true
                bottomTexts[1].isSelected = true
            }
            else -> { }
        }
    }
    private fun bottomIconAllOff() {
        bottomIcons.forEach { it.isSelected = false }
        bottomTexts.forEach { it.isSelected = false }
    }
    private fun isLocationServiceRunning(): Boolean {
        val activityManager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getRunningServices(Int.MAX_VALUE).forEach {
            if (it.service.className == SERVICE_NAME) {
                if (it.foreground) {
                    return true
                }
            } else {
                return false
            }
        }
        return false
    }
    companion object {
        val TAG = this.javaClass.simpleName
    }
}