package com.jdm.alija.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

object FileUtil {
    fun saveImageIntoFileFromUri(context: Context, bitmap: Bitmap, fileName: String, path: String) : File {
        val file: File = File(path, fileName)
        Log.e("fileutil", "${path}${fileName}")
        try {

            val fileOutputStream : FileOutputStream = FileOutputStream(file)
            when (file.absolutePath.substring(file.absolutePath.lastIndexOf(".") + 1)) {
                "jpeg", "jpg" -> {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)

                }
                "png" -> {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                }
                else -> {}
            }
            //val pdu = buildPdu(context, file)
            //fileOutputStream.write(pdu)
            fileOutputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.e("FileUtil", "FileNOtFoundException")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("FileUtil", "IOException")
        }
        return file
    }
    fun getExternalFilePath(context: Context) : String{
        val filePath : String = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}/"
        return filePath
    }
    fun getFile(path: String): File? {
        val file = File(path)
        if (file.exists()) return file else return null
    }



}