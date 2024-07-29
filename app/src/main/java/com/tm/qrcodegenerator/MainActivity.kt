package com.tm.qrcodegenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tm.qrcodegenerator.ui.theme.QRCodeGeneratorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QRCodeGeneratorTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var showScanner by remember { mutableStateOf(false) }
    var scanResult by remember { mutableStateOf<String?>(null) }

    if (showScanner) {

        QRCodeScannerScreen() { result ->
            scanResult = result
            showScanner = false
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = scanResult ?: "No scan result")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { showScanner = true }) {
                Text(text = "Open QR Scanner")
            }

            Spacer(modifier = Modifier.height(16.dp))

            QRCodeScreen()
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    QRCodeGeneratorTheme {
        MainScreen()
    }
}