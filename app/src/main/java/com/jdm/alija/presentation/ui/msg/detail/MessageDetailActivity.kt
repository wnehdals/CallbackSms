package com.jdm.alija.presentation.ui.msg.detail

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.view.MotionEvent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jdm.alija.R
import com.jdm.alija.base.BaseActivity
import com.jdm.alija.databinding.FragmentGroupBinding
import com.jdm.alija.databinding.FragmentMessageDetailBinding
import com.jdm.alija.dialog.EditTextDialog
import com.jdm.alija.domain.model.Group
import com.jdm.alija.domain.model.Photo
import com.jdm.alija.presentation.ui.group.GroupActivity
import com.jdm.alija.presentation.ui.group.GroupAdapter
import com.jdm.alija.presentation.ui.group.GroupContract
import com.jdm.alija.presentation.ui.group.GroupViewModel
import com.jdm.alija.presentation.ui.group.detail.GroupDetailActivity
import com.jdm.alija.presentation.ui.photoselect.PhotoSelectActivity
import com.jdm.alija.presentation.util.FileUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class MessageDetailActivity : BaseActivity<FragmentMessageDetailBinding>() {
    override val layoutResId: Int
        get() = R.layout.fragment_message_detail

    private val messageDetailViewModel: MessageDetailViewModel by viewModels()
    var requirePermission = arrayOf<String>()
    val storageList = mutableListOf<String>()
    private var type: Int = -1
    private var id: Int = -1
    private var photoSelectLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        onFileSelected(it)
    }
    private var imgPermissionLauncher: ActivityResultLauncher<Array<String>> =  registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (isPermissionAllow(requirePermission)) {
            showAlbum()
        } else {

        }
    }
    private val glideListener: RequestListener<Bitmap> =
        object : RequestListener<Bitmap> { //이미지 로드 완료
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Bitmap,
                model: Any,
                target: Target<Bitmap>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                val bitmap = resource
                val fileName = "alija" + System.currentTimeMillis() + ".jpg"
                val path = FileUtil.getExternalFilePath(this@MessageDetailActivity)
                val file = FileUtil.saveImageIntoFileFromUri(
                    this@MessageDetailActivity,
                    bitmap,
                    fileName,
                    path
                )

                complete("$path/$fileName")
                return false
            }
        }
    override fun initView() {
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



    }

    private fun showAlbum() {
        photoSelectLauncher.launch(PhotoSelectActivity.getPhotoSelectIntent(this))
    }

    override fun subscribe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                messageDetailViewModel.viewState.collectLatest { state ->
                    binding.etMessageDetail.setText(state.text)
                    if (state.uri == null) {
                        if (state.imgPath.isNullOrEmpty() || state.imgPath == "null") {
                            binding.clMeesageDetailPhotoDelete.visibility = View.GONE
                            binding.ivMeessageDetailPhoto.visibility = View.GONE
                        } else {
                            val file = FileUtil.getFile(state.imgPath)
                            if (file != null) {
                                binding.ivMeessageDetailPhoto.visibility = View.VISIBLE
                                Glide.with(this@MessageDetailActivity)
                                    .load(file)
                                    .error(R.drawable.ic_img_fail_black)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(binding.ivMeessageDetailPhoto)

                                binding.clMeesageDetailPhotoDelete.visibility = View.VISIBLE
                            }

                        }
                    } else {
                        binding.ivMeessageDetailPhoto.visibility = View.VISIBLE
                        Glide.with(this@MessageDetailActivity)
                            .load(state.uri)
                            .placeholder(R.drawable.ic_img_fail_black)
                            .error(R.drawable.ic_img_fail_black)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(binding.ivMeessageDetailPhoto)
                        binding.clMeesageDetailPhotoDelete.visibility = View.VISIBLE
                    }

                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                messageDetailViewModel.effect.collectLatest { effect ->
                    when (effect) {
                        is MessageDetailContract.MessageDetailSideEffect.GoToBack -> {
                            intent.putExtra(BUNDLE_TYPE, type)
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                        else -> {}
                    }
                }
            }
        }
    }
    fun complete(path: String) {
        val intent = Intent()
        messageDetailViewModel.updateGroup(binding.etMessageDetail.text.toString(), path)

    }
    override fun initEvent() {
        with(binding) {
            clMeesageDetailPhoto.setOnClickListener {
                if (isPermissionAllow(requirePermission)) {
                    showAlbum()
                } else {
                    imgPermissionLauncher.launch(requirePermission)
                }
            }
            etMessageDetail.setOnTouchListener { view, event -> // TODO Auto-generated method stub
                if (view.id == com.jdm.alija.R.id.et_message_detail) {
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    when (event.action and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(
                            false
                        )
                    }
                }
                false
            }
            tvMessageDetailComplete.setOnClickListener {
                if (messageDetailViewModel.viewState.value.uri == null) {
                    complete("")
                } else {
                    loadBitmapUri(messageDetailViewModel.viewState.value.uri!!)
                }

            }
            clMeesageDetailPhotoDelete.setOnClickListener {
                messageDetailViewModel.setEvent(MessageDetailContract.MessageDetailEvent.OnClickDeleteImg(binding.etMessageDetail.text.toString()))
            }
            ivMessageDetailBack.setOnClickListener {
                finish()
            }

        }

    }


    override fun initData() {
        type = intent.getIntExtra(BUNDLE_TYPE, -1)
        id = intent.getIntExtra(GROUP_ID, -1)
        if (type == -1) {
            finish()
            return
        }
        if (id == -1) {
            finish()
            return
        }
        when (type) {
            TYPE_INCALL -> binding.tvMessageDetailAppbarTitle.text = getString(R.string.str_incall_mesesage)
            TYPE_OUTCALL -> binding.tvMessageDetailAppbarTitle.text = getString(R.string.str_outcall_message)
            TYPE_RELEASECALL -> binding.tvMessageDetailAppbarTitle.text = getString(R.string.str_releasecall_message)
        }
        messageDetailViewModel.getGroup(id, type)

    }
    fun onFileSelected(result: ActivityResult) {
        if (result.resultCode != Activity.RESULT_OK) {
            return
        }
        val photo: Photo? =
            result.data?.getParcelableExtra<Photo>(PhotoSelectActivity.BUNDLE_KEY_PHOTO)
        if (photo != null) {
            if (photo!!.uri != null) {
                messageDetailViewModel.updateUri(photo.uri!!)
                //loadBitmapUri(photo.uri)
            }
        }


    }

    private fun loadBitmapUri(uri: Uri) {
        Glide.with(this)
            .asBitmap()
            .load(uri)
            .listener(glideListener)
            .preload()
    }
    companion object {
        val TAG = "MessageDetailActivity"
        val TYPE_INCALL = 0
        val TYPE_OUTCALL = 1
        val TYPE_RELEASECALL = 2
        val GROUP_ID = "GROUP_ID"
        val BUNDLE_TYPE = "BUNDLE_TYPE"

        fun getIntent(context: Context, type: Int, id: Int) : Intent {
            val intent = Intent(context, MessageDetailActivity::class.java)
            intent.putExtra(BUNDLE_TYPE, type)
            intent.putExtra(GROUP_ID, id)
            return intent
        }
    }
}