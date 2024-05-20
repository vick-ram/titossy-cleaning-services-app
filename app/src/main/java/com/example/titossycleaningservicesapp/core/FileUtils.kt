package com.example.titossycleaningservicesapp.core

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

object FileUtils {

    fun getFileFromUri(context: Context, uri: Uri): File? {
        if (uri.scheme == "file") {
            return File(uri.path!!)
        }

        if (uri.scheme == "content") {
            val contentResolver = context.contentResolver
            val fileName = getFileName(context, uri)
            val tempFile = File.createTempFile(fileName ?: "temp", null, context.cacheDir)

            contentResolver.openInputStream(uri)?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            return tempFile
        }

        return null
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                return if (nameIndex != -1) cursor.getString(nameIndex) else null
            }
        }
        return null
    }

    fun createErrorMessage(error: Any?): String {
        val errorMap = error as? Map<String, List<String>>
        return errorMap?.entries?.joinToString(", ") {
            "${it.key}: ${it.value.joinToString(", ")}"
        } ?: error.toString()
    }
}