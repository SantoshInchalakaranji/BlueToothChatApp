package com.prplmnstr.bluetoothchat.data.chat

import com.prplmnstr.bluetoothchat.domain.chat.BluetoothMessage

fun String.toBluetoothMessage(isFromLocalUser: Boolean): BluetoothMessage {
    val (senderName, senderAddress, date, time, message) = split("#@")
        .map { it.replace("#@","") }
    return BluetoothMessage(
        message = message,
        senderName = senderName,
        senderAddress = senderAddress,
        date = date,
        time = time,
        isFromLocalUser = isFromLocalUser
    )
}

fun BluetoothMessage.toByteArray(): ByteArray {
    return "$senderName#@$senderAddress#@$date#@$time#@$message".encodeToByteArray()
}