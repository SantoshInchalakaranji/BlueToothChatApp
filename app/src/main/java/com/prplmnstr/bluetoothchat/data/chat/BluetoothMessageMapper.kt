package com.prplmnstr.bluetoothchat.data.chat

import android.util.Log
import com.prplmnstr.bluetoothchat.domain.chat.BluetoothMessage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream

fun String.toBluetoothMessage(isFromLocalUser: Boolean, senderAddress:String): BluetoothMessage {
    val (senderName, _, date, time, message) = split("#@")
        .map { it.replace("#@","") }
    return BluetoothMessage.TextMessage(
        text = message,
        senderName = senderName,
        senderAddress = senderAddress,
        date = date,
        time = time,
        isFromLocalUser = isFromLocalUser
    )
}

fun BluetoothMessage.TextMessage.toByteArray(): ByteArray {
    return "$senderName#@$senderAddress#@$date#@$time#@$text".encodeToByteArray()
}


fun ByteArray.toBluetoothAudioMessage(isFromLocalUser: Boolean, senderAddress:String): BluetoothMessage {
    val inputStream = ByteArrayInputStream(this)
    val dataInputStream = DataInputStream(inputStream)

    // Read sender name
    val senderName = dataInputStream.readUTF()
    // Read sender address
    val senderAdd_ = dataInputStream.readUTF()
    // Read date
    val date = dataInputStream.readUTF()
    // Read time
    val time = dataInputStream.readUTF()
    // Read audio data length
    val audioLength = dataInputStream.readInt()
    // Read audio data
    val audioBytes = ByteArray(audioLength)

    dataInputStream.read(audioBytes)
   // Log.e("TAG", "toBluetoothAudioMessage: ${audioBytes.contentToString()}-----${audioBytes.size}")
    dataInputStream.close()

    return BluetoothMessage.AudioMessage(

        audioData = audioBytes,
        senderName = senderName,
        senderAddress = senderAddress,
        date = date,
        time = time,
        isFromLocalUser = isFromLocalUser
    )
}


fun BluetoothMessage.AudioMessage.toByteArray(): ByteArray {
    val outputStream = ByteArrayOutputStream()
    val dataOutputStream = DataOutputStream(outputStream)

    // Write sender name
    dataOutputStream.writeUTF(senderName)
    // Write sender address
    dataOutputStream.writeUTF(senderAddress)
    // Write date
    dataOutputStream.writeUTF(date)
    // Write time
    dataOutputStream.writeUTF(time)
    // Write audio data length
    dataOutputStream.writeInt(audioData.size)
    // Write audio data
    dataOutputStream.write(audioData)
    Log.e("TAG", "toByteArray: ${audioData.contentToString()}-----${audioData.size}")

    dataOutputStream.flush()
    dataOutputStream.close()

    return outputStream.toByteArray()
}

fun BluetoothMessage.ImageMessage.toByteArray(): ByteArray {
    val outputStream = ByteArrayOutputStream()
    val dataOutputStream = DataOutputStream(outputStream)

    // Write sender name
    dataOutputStream.writeUTF(senderName)
    // Write sender address
    dataOutputStream.writeUTF(senderAddress)
    // Write date
    dataOutputStream.writeUTF(date)
    // Write time
    dataOutputStream.writeUTF(time)
    // Write audio data length
    dataOutputStream.writeInt(imageData.size)
    // Write audio data
    dataOutputStream.write(imageData)
    Log.e("TAG", "toByteArray: ${imageData.contentToString()}-----${imageData.size}")

    dataOutputStream.flush()
    dataOutputStream.close()

    return outputStream.toByteArray()
}


fun ByteArray.toBluetoothImageMessage(isFromLocalUser: Boolean, senderAddress:String): BluetoothMessage {
    val inputStream = ByteArrayInputStream(this)
    val dataInputStream = DataInputStream(inputStream)

    // Read sender name
    val senderName = dataInputStream.readUTF()
    // Read sender address
    val senderAdd_ = dataInputStream.readUTF()
    // Read date
    val date = dataInputStream.readUTF()
    // Read time
    val time = dataInputStream.readUTF()
    // Read audio data length
    val imageLength = dataInputStream.readInt()
    // Read audio data
    val imageBytes = ByteArray(imageLength)

    dataInputStream.read(imageBytes)
    Log.e("TAG", "toBluetoothImageMessage: ${imageBytes.contentToString()}-----${imageBytes.size}")
    dataInputStream.close()

    return BluetoothMessage.ImageMessage(

        imageData = imageBytes,
        senderName = senderName,
        senderAddress = senderAddress,
        date = date,
        time = time,
        isFromLocalUser = isFromLocalUser
    )
}

