package com.prplmnstr.bluetoothchat.domain.chat.bluetooth

import com.prplmnstr.bluetoothchat.domain.chat.bluetooth.entity.BluetoothMessage

sealed interface ConnectionResult {
    data class ConnectionEstablished(val peerDevice: BluetoothDeviceDomain): ConnectionResult
    data class TransferSucceeded(val message: BluetoothMessage): ConnectionResult
    data class Error(val message: String): ConnectionResult
}