package com.prplmnstr.bluetoothchat.domain.chat

import java.util.Date

data class BluetoothMessage(
    val message: String,
    val date: String,
    val time: String,
    val senderName: String,
    val senderAddress:String,
    val isFromLocalUser: Boolean
)