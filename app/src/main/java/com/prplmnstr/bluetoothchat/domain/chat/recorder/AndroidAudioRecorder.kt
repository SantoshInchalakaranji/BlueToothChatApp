package com.prplmnstr.bluetoothchat.domain.chat.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import java.io.File
import java.io.FileOutputStream

class AndroidAudioRecorder(
    private val context: Context
) : AudioRecord{

    private var recorder: MediaRecorder? = null

    private fun createRecorder(): MediaRecorder {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    override  fun start(outputFile: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)

            prepare()
            start()

            recorder = this
            Log.e("TAG", "createRecorded  : sound recorded")
        }
    }

    override  fun stop() {
        recorder?.stop()
        recorder?.reset()
        recorder = null
    }
}