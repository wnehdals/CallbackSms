package com.jdm.alija.presentation.ui.photoselect

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jdm.alija.BuildConfig
import com.jdm.alija.R
import com.jdm.alija.base.BaseActivity
import com.jdm.alija.databinding.ActivityPhotoSelectBinding
import com.jdm.alija.domain.model.Photo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class PhotoSelectActivity : BaseActivity<ActivityPhotoSelectBinding>() {
    override val layoutResId: Int
        get() = R.layout.activity_photo_select

    private val photoSelectViewModel: PhotoSelectViewModel by viewModels()
    private val photoAdapter : PhotoSelectAdapter by lazy { PhotoSelectAdapter(this, this::onClickPhoto) }
    override fun initView() {
        with(binding) {
            rvPhotoSelect.adapter = photoAdapter
        }
    }

    override fun subscribe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoSelectViewModel.viewState.collect {
                    photoAdapter.submitList(it.photoList)
                    if (it.photoList.isNotEmpty())
                        Log.e("viewstate","${it.photoList[0].uri.toString()}")
                }
            }
        }
    }

    override fun initEvent() {
        binding.ivPhotoSelectBack.setOnClickListener { finish() }
        binding.tvPhotoSelectComplete.setOnClickListener {
            if (photoAdapter.getSelectedPhoto() == null) {

            } else {
                val photo: Photo = photoAdapter.getSelectedPhoto()!!
                //val imagePath = File(this.filesDir, "images")
                //val file = File(getPathFromUri(photo.uri))
                //val uri1 = Uri.fromFile(file)
                //val uri2 = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", file)
                //Log.e("complete1", uri1.toString())
                //Log.e("complete2", uri2.toString())
                val intent = Intent().apply { putExtra(BUNDLE_KEY_PHOTO, photo ) }
                setResult(RESULT_OK, intent)
                finish()
                //photoSelectViewModel.setEvent(PhotoSelectContract.PhotoSelectEvent.OnClickPhotoSelect(photoAdapter.getSelectedPhoto()!!))
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

    override fun initData() {
        photoSelectViewModel.getPhotoList()
    }
    private fun onClickPhoto(previousIdx: Int?, nowIdx: Int) {
        previousIdx?.let { photoAdapter.notifyItemChanged(it) }
        photoAdapter.notifyItemChanged(nowIdx)
    }
    companion object {
        fun getPhotoSelectIntent(activity: Context) : Intent{
            val intent = Intent(activity, PhotoSelectActivity::class.java)
            return intent
        }
        val BUNDLE_KEY_PHOTO = "photo"
    }
}