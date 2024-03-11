package com.jdm.alija.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.core.net.toUri
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

object FileUtil {
    fun saveImageIntoFileFromUri(context: Context, bitmap: Bitmap, fileName: String, path: String) : File {
        val file: File = File(path, fileName)
        Log.e("fileutil", "${path}${fileName}")
        var quality = 100
        try {
            while (true) {
                val fileOutputStream: FileOutputStream = FileOutputStream(file)
                Log.e(
                    "title",
                    "${file.absolutePath.substring(file.absolutePath.lastIndexOf(".") + 1)}"
                )
                when (file.absolutePath.substring(file.absolutePath.lastIndexOf(".") + 1)) {
                    "jpeg", "jpg" -> {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream)

                    }

                    "png" -> {
                        bitmap.compress(Bitmap.CompressFormat.PNG, quality, fileOutputStream)
                    }

                    else -> {}
                }
                Log.e("file size ","${file.length() / 1000}kb")
                fileOutputStream.close()
                if (file.length() / 1024 < 300) {
                    break
                } else {
                    quality -= 20
                    deleteExternalFilePath(fileName)
                }

            }

            //val pdu = buildPdu(context, file)
            //fileOutputStream.write(pdu)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.e("FileUtil", "FileNOtFoundException")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("FileUtil", "IOException")
        }
        return file
    }
    fun getExternalFilePath() : String{
        val filePath : String = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}/"
        return filePath
    }
    fun getFile(path: String): File? {
        val file = File(path)
        if (file.exists()) return file else return null
    }
    fun deleteExternalFilePath(fileName: String) {
        val filePath = getExternalFilePath()
        val file = File(filePath, fileName)
        if (file.exists())
            file.delete()
    }




}
fun Uri.deletePic(context: Context) {
    val file = File(this.path)
    if (file.exists()) {
        context.contentResolver.delete(this, null, null)
    }
}