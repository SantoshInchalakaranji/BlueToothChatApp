package com.prplmnstr.bluetoothchat.domain.chat.bluetooth.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BluetoothDevice(
    val name: String?,
    val address: String
): Parcelable