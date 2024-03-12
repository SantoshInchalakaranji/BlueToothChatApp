package com.prplmnstr.bluetoothchat.domain.chat.bluetooth

import com.prplmnstr.bluetoothchat.domain.chat.bluetooth.entity.BluetoothDevice
import com.prplmnstr.bluetoothchat.domain.chat.bluetooth.entity.BluetoothMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface BluetoothController {
    val isConnected: StateFlow<Boolean>
    val connectedDevice: StateFlow<BluetoothDeviceDomain>
    val scannedDevices: StateFlow<List<BluetoothDevice>>
    val pairedDevices: StateFlow<List<BluetoothDevice>>
    val errors: SharedFlow<String>

    fun startDiscovery()
    fun stopDiscovery()

    fun startBluetoothServer(): Flow<ConnectionResult>
    fun connectToDevice(device: BluetoothDevice): Flow<ConnectionResult>


    suspend fun trySendMessage(message: String): BluetoothMessage?
    suspend fun trySendMessage(audioData : File): BluetoothMessage?
    suspend fun trySendMessage(imageData : ByteArray): BluetoothMessage?

    fun closeConnection()
    fun release()
}