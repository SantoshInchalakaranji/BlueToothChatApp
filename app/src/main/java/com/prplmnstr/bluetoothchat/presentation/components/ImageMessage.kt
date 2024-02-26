package com.prplmnstr.bluetoothchat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prplmnstr.bluetoothchat.domain.chat.BluetoothMessage
import com.prplmnstr.bluetoothchat.ui.theme.BlueViolet3
import com.prplmnstr.bluetoothchat.ui.theme.LightRed

@Composable
fun ImageMessage(
    message: BluetoothMessage.ImageMessage,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = if (message.isFromLocalUser) 15.dp else 0.dp,
                    topEnd = 15.dp,
                    bottomStart = 15.dp,
                    bottomEnd = if (message.isFromLocalUser) 0.dp else 15.dp
                )
            )
            .background(
                if (message.isFromLocalUser) BlueViolet3 else LightRed
            )
            .padding(16.dp)
    ) {

        val painter = byteArrayToPainter(message.imageData)
        painter.let {
            Image(painter = painter!!, contentDescription = "",
                modifier = Modifier.size(250.dp))
        }

        Text(
            text = message.time,
            fontSize = 10.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.End)
        )
    }
}
fun byteArrayToPainter(byteArray: ByteArray): Painter? {
    return try {
        val bitmap = android.graphics.BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val imageBitmap = bitmap.asImageBitmap()
        BitmapPainter(imageBitmap)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
