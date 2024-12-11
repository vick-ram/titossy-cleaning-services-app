package com.example.titossycleaningservicesapp.core

import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Density
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun generatePdf(context: Context, density: Density,composable: @Composable () -> Unit) {
    val bitmap = captureComposableAsBitmap(context,density, composable)

    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    canvas.drawBitmap(bitmap, 0f, 0f, null)
    pdfDocument.finishPage(page)

    val file = File(context.cacheDir, "receipt.pdf")
    try {
        val outputStream = FileOutputStream(file)
        pdfDocument.writeTo(outputStream)
        pdfDocument.close()
        outputStream.close()

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val printIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        context.startActivity(printIntent)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}


