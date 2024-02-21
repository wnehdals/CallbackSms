package com.jdm.alija.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import com.google.android.mms.pdu_alt.EncodedStringValue
import com.google.android.mms.pdu_alt.PduBody
import com.google.android.mms.pdu_alt.PduComposer
import com.google.android.mms.pdu_alt.PduPart
import com.google.android.mms.pdu_alt.SendReq
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
    private fun buildPdu(context: Context, file: File): ByteArray? {
        val sendRequestPdu = SendReq()
        sendRequestPdu.addTo(EncodedStringValue("010-5139-1216"))
        val pduBody = PduBody()
        val sampleImageData: ByteArray = FileUtils.readFileToByteArray(file)
        val sampleImagePduPart = PduPart()
        sampleImagePduPart.data = sampleImageData
        sampleImagePduPart.contentType = "image/png".toByteArray()
        //sampleImagePduPart.filename = file.name.getBytes()
        pduBody.addPart(sampleImagePduPart)
        sendRequestPdu.body = pduBody
        val composer = PduComposer(context, sendRequestPdu)
        val pduData = composer.make()
        return pduData
    }



}