package com.prplmnstr.bluetoothchat.presentation.chatScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prplmnstr.bluetoothchat.R
import com.prplmnstr.bluetoothchat.domain.chat.bluetooth.entity.BluetoothMessage
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

        val imageBitmap = byteArrayToPainter(message.imageData)
        if(imageBitmap!=null){
            Image(bitmap =imageBitmap , contentDescription = "",
                modifier = Modifier.size(250.dp))
        }else{
            Image(
                painterResource(id = R.drawable.place_holder) , contentDescription = "",
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
fun byteArrayToPainter(byteArray: ByteArray): ImageBitmap? {
    return try {
        val bitmap = android.graphics.BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val imageBitmap = bitmap.asImageBitmap()
       imageBitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
