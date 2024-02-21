package com.jdm.alija.presentation.ui.contractdetail

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.jdm.alija.R
import com.jdm.alija.base.BaseActivity
import com.jdm.alija.databinding.ActivityContactDetailBinding
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.Photo
import com.jdm.alija.presentation.ui.photoselect.PhotoSelectActivity
import com.jdm.alija.presentation.ui.photoselect.PhotoSelectActivity.Companion.BUNDLE_KEY_PHOTO
import com.jdm.alija.presentation.util.FileUtil
import com.jdm.alija.presentation.util.SmsUtil
import com.klinker.android.send_message.Message
import com.klinker.android.send_message.Settings
import com.klinker.android.send_message.Transaction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ContactDetailActivity : BaseActivity<ActivityContactDetailBinding>() {
    override val layoutResId: Int
        get() = R.layout.activity_contact_detail
    private val contactDetailViewModel: ContactDetailViewModel by viewModels()
    var uri: Uri? = null
    lateinit var sendTransaction: Transaction
    @Inject
    lateinit var smsUtil: SmsUtil
    private val albumPicker =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onFileSelected(
                result
            )
        }
    private val photoSelectLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        onFileSelected(it)
    }

    private val imgPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (isPermissionAllow(requirePermission)) {
                showAlbum()
            } else {

            }
        }
    val storageList = mutableListOf<String>()
    var requirePermission = arrayOf<String>()
    override fun initView() {
        val sendSettings = Settings()
        sendSettings.useSystemSending = true
        sendTransaction = Transaction(this, sendSettings)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            storageList.add(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storageList.add(Manifest.permission.READ_MEDIA_IMAGES)
            storageList.add(Manifest.permission.READ_MEDIA_VIDEO)
        } else {
            storageList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        requirePermission = storageList.toTypedArray()

        val contact = intent.getParcelableExtra<Contact>(CONTACT_BUNDLE_KEY)
        uri = Uri.parse(contact?.imgUri)
        contactDetailViewModel.initData(contact)
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (resultCode) {
                    RESULT_OK -> Toast.makeText(baseContext, "SMS delivered", Toast.LENGTH_SHORT)
                        .show()

                    RESULT_CANCELED -> Toast.makeText(
                        baseContext,
                        "SMS not delivered",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }, IntentFilter("SMS_DELIVERED"), RECEIVER_NOT_EXPORTED)
    }


    override fun subscribe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                contactDetailViewModel.viewState.collect { state ->
                    binding.tvContactDetailName.text = state.name
                    binding.ivContactDetailAvatar.isSelected = state.isSelected
                    if (state.imgUri != null) {
                        binding.clContactDetailCamera.visibility = View.GONE
                        binding.ivContactDetailAttachImg.visibility = View.VISIBLE
                        Glide.with(this@ContactDetailActivity)
                            .load(state.imgUri)
                            .placeholder(R.drawable.bg_r8_f_gray100)
                            .error(R.drawable.ic_img_fail_black)
                            .into(binding.ivContactDetailAttachImg)
                    } else {
                        binding.ivContactDetailAttachImg.visibility = View.GONE
                        binding.clContactDetailCamera.visibility = View.VISIBLE
                    }
                    binding.taContactDetail.setText(state.text)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                contactDetailViewModel.effect.collect { effect ->
                    when (effect) {
                        is ContactDetailContract.ContactDetailSideEffect.ShowToast -> {
                            Toast.makeText(
                                this@ContactDetailActivity,
                                effect.msg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ContactDetailContract.ContactDetailSideEffect.GoToBack -> {
                            finish()
                        }
                    }
                }
            }
        }
    }

    fun getPathFromUri(uri: Uri?): String? {
        val cursor = contentResolver.query(uri!!, null, null, null, null)
        cursor!!.moveToNext()
        val path = cursor.getString(cursor.getColumnIndexOrThrow("_data"))
        cursor.close()
        return path
    }

    override fun initEvent() {
        binding.ivContactDetailBack.setOnClickListener {
            smsUtil.sendTextMessage("01051391216", "test")

        }
        binding.clContactDetailCamera.setOnClickListener {
            if (isPermissionAllow(requirePermission)) {
                showAlbum()
            } else {
                imgPermissionLauncher.launch(requirePermission)
            }
        }
        binding.tvContactDetailComplete.setOnClickListener {


            setImage(uri) {
                val fileName = "alija" + System.currentTimeMillis() + ".jpg"
                val file = FileUtil.saveImageIntoFileFromUri(this, it, fileName, FileUtil.getExternalFilePath(this) )
                val mediaType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
                val photoURL = FileProvider.getUriForFile(this, "com.jdm.alija.fileProvider", file)
                smsUtil.sendImgMessage("010-5139-1216",it)
                    return@setImage
                Log.e("fileUri", "${photoURL.toString()}")
                val smsUri = Uri.parse("smsto:" + "01051391216")
                val intent = Intent(Intent.ACTION_SENDTO)
                //intent.putExtra("address","01000000000")
                intent.putExtra(Intent.EXTRA_STREAM, photoURL)
                intent.setType("image/*")
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.setData(smsUri)

                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                intent.putExtra("sms_body", "테스트 메시지")
                startActivity(Intent.createChooser(intent, "share"))

                //intent.setDataAndType(smsUri, "image/*" )

            }
            //contactDetailViewModel.setEvent(ContactDetailContract.ContactDetailEvent.OnClickComplete(binding.taContactDetail.getText()))



        }
    }

    private fun setImage(uri: Uri?, bitmapCallback: (Bitmap) -> Unit) {
        if (uri == null)
            return
        Glide.with(this)
            .load(uri) //uri로 이미지 로드
            .apply(RequestOptions.skipMemoryCacheOf(true)) //메모리 캐시 사용 no
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)) //디스크 캐시 사용 no
            .listener(object : RequestListener<Drawable> { //이미지 로드 완료 시 리스너
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    if (resource is BitmapDrawable) {
                        val bitmap = resource.bitmap
                        bitmapCallback(bitmap)
                        bitmap.byteCount		// 리사이징된 이미지 바이트
                        bitmap.width	    	// 이미지 넓이
                        bitmap.height			// 이미지 높이
                    }
                    return false
                }
            })
            .into(binding.ivContactDetailAttachImg)

    }
    fun getOriginalImageSize(uri: Uri, context: Context): Pair<Int, Int> {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(
            context.contentResolver.openInputStream(uri),
            null,
            options
        )
        val imageHeight = options.outWidth
        val imageWidth = options.outHeight
        return imageHeight to imageWidth
    }

    private fun showAlbum() {
        photoSelectLauncher.launch(PhotoSelectActivity.getPhotoSelectIntent(this))
        //albumPicker.launch(Intent(Intent.ACTION_GET_CONTENT).apply {
        //        type = "image/*"
        //        putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*"))
        //    }
        //)

    }

    override fun initData() {
    }

    fun onFileSelected(result: ActivityResult) {
        if (result.resultCode != Activity.RESULT_OK) {
            return
        }
        val photo: Photo? = result.data?.getParcelableExtra<Photo>(BUNDLE_KEY_PHOTO)
        contactDetailViewModel.setEvent(
            ContactDetailContract.ContactDetailEvent.OnClickAttachImg(
                photo,
                binding.taContactDetail.getText()
            )
        )

    }

    companion object {
        val CONTACT_BUNDLE_KEY = "contact"
        fun getContactDetailIntent(activity: Context, item: Contact): Intent {
            val intent = Intent(activity, ContactDetailActivity::class.java)
            intent.putExtra(CONTACT_BUNDLE_KEY, item)
            return intent
        }

        val TAG = this.javaClass.simpleName
    }
}