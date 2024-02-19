package com.jdm.alija.presentation.ui.photoselect

import androidx.lifecycle.viewModelScope
import com.jdm.alija.base.BaseViewModel
import com.jdm.alija.domain.model.Photo
import com.jdm.alija.domain.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections
import javax.inject.Inject

@HiltViewModel
class PhotoSelectViewModel @Inject constructor(
    private val galleryRepository: GalleryRepository
): BaseViewModel<PhotoSelectContract.PhotoSelectViewState, PhotoSelectContract.PhotoSelectSideEffect, PhotoSelectContract.PhotoSelectEvent>(
    PhotoSelectContract.PhotoSelectViewState()
) {
    override fun handleEvents(event: PhotoSelectContract.PhotoSelectEvent) {
        when (event) {
            is PhotoSelectContract.PhotoSelectEvent.OnClickPhotoSelect -> {
                saveFile(event.photo)
            }
        }
    }
    fun saveFile(photo: Photo) {

    }
    fun getPhotoList() {
        viewModelScope.launch(Dispatchers.IO + ceh) {
            galleryRepository.getPhotoList().collectLatest { photo ->
                withContext(Dispatchers.Main) {
                    Collections.sort(photo)
                    updateState { copy(photoList = photo) }
                }
            }

        }
    }

}