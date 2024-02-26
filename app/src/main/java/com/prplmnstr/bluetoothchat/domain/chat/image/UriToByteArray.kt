package com.prplmnstr.bluetoothchat.domain.chat.image

import android.content.ContentResolver
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException

class UriToByteArray {
    suspend fun uriToByteArray(contentResolver: ContentResolver, uri: Uri): ByteArray? {
        return withContext(Dispatchers.IO) {
            try {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    val outputStream = ByteArrayOutputStream()
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                    outputStream.toByteArray()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

}