package com.jdm.alija.domain.repository

import com.jdm.alija.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    fun getPhotoList(): Flow<MutableList<Photo>>
}