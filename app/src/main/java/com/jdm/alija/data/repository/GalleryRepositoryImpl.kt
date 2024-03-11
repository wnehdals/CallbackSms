package com.jdm.alija.data.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import com.jdm.alija.domain.model.Photo
import com.jdm.alija.domain.repository.GalleryRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : GalleryRepository{
    override fun getPhotoList(): Flow<MutableList<Photo>> = flow {
        val result = mutableListOf<Photo>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.SIZE,
        )

        val collecton = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.SIZE + " > 0"
        } else {
            null
        }

        val query = context.contentResolver.query(
            collecton,
            projection,
            selection,
            null,
            sortOrder
        )

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val uriColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val dateAddedColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
            val mineColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val mediaUri = getImageMediaUri(id, cursor.getString(uriColumn))
                val datedAddedSecond = cursor.getLong(dateAddedColumn)
                var mineType = cursor.getString(mineColumn)
                var size : Long = cursor.getLong(sizeColumn)
                result.add(Photo(id = id, uri = mediaUri, dateAddedSecond = datedAddedSecond, size = size))
            }
        }
        query?.close()
        emit(result)
    }.catch {
        emit(mutableListOf())
    }

    private fun getImageMediaUri(id: Long, str: String): Uri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ContentUris.withAppendedId(contentUri, id)
        } else {
            Uri.fromFile(File(str))
        }
}