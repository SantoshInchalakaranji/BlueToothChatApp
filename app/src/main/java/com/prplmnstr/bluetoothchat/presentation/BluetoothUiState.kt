package com.prplmnstr.bluetoothchat.presentation

import com.prplmnstr.bluetoothchat.domain.chat.bluetooth.entity.BluetoothDevice
import com.prplmnstr.bluetoothchat.domain.chat.bluetooth.BluetoothDeviceDomain
import com.prplmnstr.bluetoothchat.domain.chat.bluetooth.entity.BluetoothMessage

data class BluetoothUiState(
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val messages: List<BluetoothMessage> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,

    var peerDevice: BluetoothDeviceDomain? = null

)