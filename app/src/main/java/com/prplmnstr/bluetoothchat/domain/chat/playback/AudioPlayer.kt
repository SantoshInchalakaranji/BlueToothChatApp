package com.prplmnstr.bluetoothchat.domain.chat.playback

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
    fun seekTo(position: Int)
}