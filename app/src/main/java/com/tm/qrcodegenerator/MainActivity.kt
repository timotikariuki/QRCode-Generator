package com.tm.qrcodegenerator

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QRCodeScreen()
        }
    }
}
@Composable
fun QRCodeScreen() {
    val qrContent = "Your QR Code Content Here"
    val size = 600

    val qrBitmap = generateQRCode(qrContent, size)
    val finalBitmap = addTextToBitmap(qrBitmap, "SCAN ME")

    if (finalBitmap != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = finalBitmap.asImageBitmap(),
                contentDescription = "QR Code"
            )
        }
    }
}

private fun generateQRCode(content: String, size: Int): Bitmap? {
    return try {
        val writer = QRCodeWriter()
        val hints = HashMap<EncodeHintType, Any>()
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"

        val bitMatrix: BitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size, hints)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)

        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }

        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun addTextToBitmap(bitmap: Bitmap?, text: String): Bitmap? {
    if (bitmap == null) return null

    val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(mutableBitmap)
    val paint = Paint().apply {
        color = Color.BLACK
        textSize = 40f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    val textWidth = paint.measureText(text)
    val textHeight = paint.descent() - paint.ascent()

    val centerX = (canvas.width / 2).toFloat()
    val centerY = (canvas.height / 2).toFloat()

    // Draw white rectangle background for the text
    val rectPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    canvas.drawRect(
        centerX - textWidth / 2 - 10,
        centerY - textHeight / 2 - 10,
        centerX + textWidth / 2 + 10,
        centerY + textHeight / 2 + 10,
        rectPaint
    )

    // Draw the text
    canvas.drawText(text, centerX, centerY - (paint.descent() + paint.ascent()) / 2, paint)

    return mutableBitmap
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    QRCodeScreen()
}