package com.prplmnstr.bluetoothchat.presentation

import com.prplmnstr.bluetoothchat.domain.chat.BluetoothDevice
import com.prplmnstr.bluetoothchat.domain.chat.BluetoothMessage

data class BluetoothUiState(
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val messages: List<BluetoothMessage> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val peerName:String? = ""

)