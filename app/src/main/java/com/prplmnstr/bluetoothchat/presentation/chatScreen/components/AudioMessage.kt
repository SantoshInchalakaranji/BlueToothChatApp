package com.prplmnstr.bluetoothchat.presentation.chatScreen.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prplmnstr.bluetoothchat.R
import com.prplmnstr.bluetoothchat.domain.chat.bluetooth.entity.BluetoothMessage
import com.prplmnstr.bluetoothchat.ui.theme.BlueViolet3
import com.prplmnstr.bluetoothchat.ui.theme.LightRed
import kotlinx.coroutines.delay
import java.io.File


@Composable
fun AudioMessage(
    message: BluetoothMessage.AudioMessage,
    modifier: Modifier = Modifier,
    startPlaying: () -> Unit,
    stopPlaying: () -> Unit,
    seekTo: (position: Int) -> Unit,
    getAudioDuration:() ->Int,
    getCurrentPosition:() ->Int,
    createAudioFile:(name:String)->File,
    saveByteArrayToFile:(ByteArray,File)->Unit,
    setPlayer: (File)->Unit,
) {
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var duration by remember { mutableStateOf(10f) }
    var audioFile by rememberSaveable { mutableStateOf<File?>(null) }


    val context = LocalContext.current





    LaunchedEffect(isPlaying) {

        while (isPlaying) {
            delay(500L)
            progress = (progress + 500f ).coerceAtMost(duration)
            if(progress==duration){
                isPlaying=!isPlaying
                progress = 0f
            }
        }
    }

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
            .padding(16.dp),
    ) {


        Row(

            verticalAlignment = Alignment.CenterVertically

        ) {
            // Play button
            IconButton(
                onClick = {
                    if (isPlaying) {
                        stopPlaying()
                        isPlaying = !isPlaying

                    } else {
                       if(message.audioData.isEmpty()){
                           Toast.makeText(context, "Audio data not found. File may be deleted from device.", Toast.LENGTH_SHORT).show()
                       }else{
                           if(audioFile==null){

                               audioFile =  createAudioFile("temp")
                               saveByteArrayToFile(message.audioData,audioFile!!)
                               setPlayer(audioFile!!)
                               duration = getAudioDuration().toFloat()
                           }
                           startPlaying()
                           isPlaying = !isPlaying
                       }


                    }


                },

                content = {
                    Icon(

                        painterResource(
                            id =
                            if (isPlaying) R.drawable.pause_24 else R.drawable.play_arrow_24
                        ),
                        contentDescription = "Play/Pause",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp),
                    )
                }
            )

            // Slider for seeking
            Slider(
                value = progress,
                onValueChange = {
                    progress = it
                    seekTo(it.toInt())

                },
                valueRange = 0f..duration,
                onValueChangeFinished = {
                },
                modifier = Modifier.fillMaxWidth(0.5f)
            )
            Text(
                text = message.time,
                fontSize = 10.sp,
                color = Color.Black,
                )
        }

    }

}


