package com.example.titossycleaningservicesapp.core

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import com.example.titossycleaningservicesapp.presentation.ui.theme.AppTheme
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun captureComposableAsBitmap(
    context: Context,
    density: Density,
    composable: @Composable () -> Unit,
): Bitmap {
    val bitmap = Bitmap.createBitmap(
        (1080 / density.density).toInt(),
        (1920 / density.density).toInt(),
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)

    val composeView = ComposeView(context).apply {
        setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    composable()
                }
            }
        }
    }
    composeView.measure(
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )
    composeView.layout(0, 0, composeView.measuredWidth, composeView.measuredHeight)
    composeView.draw(canvas)
    return bitmap
}

@Composable
fun CaptureAndPrintComposable(
    context: Context,
    composable: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithContent {
                graphicsLayer.record {
                    this@drawWithContent.drawContent()
                }
                drawLayer(graphicsLayer)
            }
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.TopCenter).padding(16.dp),
            onClick = {
                scope.launch {
                    val bitmap = graphicsLayer.toImageBitmap()
                    val androidBitmap = bitmap.asAndroidBitmap()

                    // Save as PDF and initiate print
                    val pdfFile = saveImageAsBitmap(androidBitmap)
                    pdfFile?.let { printPdf(context, it) }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Print,
                contentDescription = null
            )
        }
        composable()
    }
}


fun saveImageAsBitmap(bitmap: Bitmap): File? {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    page.canvas.drawBitmap(bitmap, 0f, 0f, null)
    pdfDocument.finishPage(page)

    val pdfFile = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "receipt.pdf"
    )

    try {
        val outputStream = FileOutputStream(pdfFile)
        pdfDocument.writeTo(outputStream)
        pdfDocument.close()
        outputStream.close()
        return pdfFile
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

fun printPdf(context: Context, pdfFile: File) {
    val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
    val printAdapter: PrintDocumentAdapter = PdfPrintAdapter(pdfFile)
    printManager.print("Document", printAdapter, PrintAttributes.Builder().build())
}

class PdfPrintAdapter(
    private val pdfFile: File
) : PrintDocumentAdapter() {

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes?,
        cancellationSignal: android.os.CancellationSignal?,
        callback: LayoutResultCallback?,
        extras: android.os.Bundle?
    ) {
        val info = PrintDocumentInfo.Builder(pdfFile.name)
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .build()
        callback?.onLayoutFinished(info, true)
    }

    override fun onWrite(
        pages: Array<PageRange?>,
        destination: ParcelFileDescriptor?,
        cancellationSignal: android.os.CancellationSignal?,
        callback: WriteResultCallback?
    ) {
        try {
            // Open the PDF file for reading
            ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY).use { input ->
                PdfRenderer(input).use { renderer ->
                    // We assume a single-page PDF; adjust for multi-page support if needed
                    val page = renderer.openPage(0)
                    val width = page.width
                    val height = page.height

                    // Create a bitmap to render the PDF page into
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

                    // Render the PDF page onto the bitmap
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT)
                    page.close()

                    destination?.use { output ->
                        val pdfCanvas = PdfDocument().startPage(
                            PdfDocument.PageInfo.Builder(width, height, 1).create()
                        )
                        pdfCanvas.canvas.drawBitmap(bitmap, 0f, 0f, null)

                        PdfDocument().finishPage(pdfCanvas)
                        callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            callback?.onWriteFailed(e.message)
        }
    }
}
