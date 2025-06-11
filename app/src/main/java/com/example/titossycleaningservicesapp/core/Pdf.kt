package com.example.titossycleaningservicesapp.core

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun <T> generateItextPdf(
    context: Context,
    fileName: String,
    data: List<T>,
    contentBuilder: (T, PdfPTable) -> Unit,
    headerTitles: List<String>? = null,
    title: String = "Document",
    logoResId: Int? = null,
    onComplete: (filePath: String?) -> Unit
) {
    val document = Document()

    try {
//        Create File path
        val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            ?: context.filesDir
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val file = File(downloadsDir, "${fileName}_$timestamp.pdf")

        PdfWriter.getInstance(document, FileOutputStream(file))
        document.open()

//        Add company logo
        logoResId?.let { resId ->
            try {
                val inputStream = context.resources.openRawResource(resId)
                val bytes = inputStream.readBytes()
                inputStream.close()
                val logo = Image.getInstance(bytes)
                logo.scaleToFit(200f, 100f) // Adjust size as needed
                logo.alignment = Image.ALIGN_CENTER
                document.add(logo)
                document.add(Paragraph("\n")) // Add space after logo
            } catch (e: Exception) {
                Log.e("PDF", "Failed to add logo", e)
            }
        }

        val titleFont = Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD)
        val titleParagraph = Paragraph(title, titleFont)
        titleParagraph.alignment = Element.ALIGN_CENTER

//        Add title
        document.add(titleParagraph)

        document.add(Paragraph("\n"))

//        Create table
        val table = PdfPTable(headerTitles?.size ?: 1).apply {
            widthPercentage = 100f
            defaultCell.apply {
                backgroundColor = BaseColor.LIGHT_GRAY
                paddingTop = 8f
                paddingBottom = 8f
                paddingLeft = 8f
                paddingRight = 8f
            }
        }

//        Add headers if provided
        headerTitles?.forEach { t ->
            table.addCell(PdfPCell(Phrase(t)).apply {
                backgroundColor = BaseColor(64, 64, 64)
                horizontalAlignment = Element.ALIGN_CENTER
                verticalAlignment = Element.ALIGN_MIDDLE
            })
        }

//        Build content using the lambda
        data.forEach { item ->
            contentBuilder(item, table)
        }

        document.add(table)
        document.close()
        triggerDownload(context, file, onComplete)
//        onComplete(file.absolutePath)
    } catch (e: Exception) {
        document.close()
        onComplete(null)
        e.printStackTrace()
    }
}

private fun triggerDownload(
    context: Context,
    file: File,
    onComplete: (String?) -> Unit
) {
    try {
        val contentUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(contentUri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_TASK
        }

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
            onComplete(file.absolutePath)
        } else {
            Toast.makeText(context, "No PDF viewer installed", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        Toast.makeText(context, "${e.message}", Toast.LENGTH_LONG).show()
        onComplete(null)
    }
}