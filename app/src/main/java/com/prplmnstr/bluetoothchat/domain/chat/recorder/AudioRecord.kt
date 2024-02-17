package com.prplmnstr.bluetoothchat.domain.chat.recorder

import java.io.File

interface AudioRecord {

   suspend fun start(outputFile: File)
   suspend fun stop()
}