package com.prplmnstr.bluetoothchat.domain.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

typealias BluetoothDeviceDomain = BluetoothDevice



@Parcelize
data class BluetoothDevice(
    val name: String?,
    val address: String
):Parcelable