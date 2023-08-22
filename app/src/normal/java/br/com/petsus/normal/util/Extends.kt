package br.com.petsus.normal.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun File.toMultipart(): RequestBody =
    this.asRequestBody("image/jpeg".toMediaTypeOrNull())

fun Uri.toMultipart(context: Context): MultipartBody.Part {
    context.contentResolver.openFileDescriptor(this, "r").use { fileDescriptor ->
        fileDescriptor ?: throw NullPointerException("File descriptor not found")
        val imageFile = File.createTempFile("image", ".jpeg")
        BitmapFactory.decodeFileDescriptor(fileDescriptor.fileDescriptor)
            .compress(Bitmap.CompressFormat.JPEG, 100, imageFile.outputStream())
        return MultipartBody.Part.createFormData("file", imageFile.name, imageFile.toMultipart())
    }
}