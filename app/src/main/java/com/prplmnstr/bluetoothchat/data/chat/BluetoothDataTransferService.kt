package com.prplmnstr.bluetoothchat.data.chat

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.prplmnstr.bluetoothchat.data.chat.Constants.Companion.AUDIO_MSG_MARK
import com.prplmnstr.bluetoothchat.data.chat.Constants.Companion.TEXT_MSG_MARK
import com.prplmnstr.bluetoothchat.domain.chat.BluetoothMessage
import com.prplmnstr.bluetoothchat.domain.chat.TransferFailedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.DataInputStream
import java.io.IOException
import java.nio.ByteBuffer

class BluetoothDataTransferService(
    private val socket: BluetoothSocket
) {
    fun listenForIncomingMessages(): Flow<BluetoothMessage> {
        return flow {
            if (!socket.isConnected) {
                return@flow
            }
            Log.d("TAG", "listenForIncomingMessages ")
            val bufferList = mutableListOf<ByteArray>()
            var buffer = ByteArray(990)
            var msg = ""
            while (true) {
                val byteCount = try {
                    socket.inputStream.read(buffer)
                } catch (e: IOException) {
                    Log.d("TAG", "transefer failed Error: $e+++++ ${e.cause}")
                    throw TransferFailedException()
                }
                bufferList.add(buffer.copyOfRange(0,byteCount))
                val lastByte= buffer[byteCount-1]

                //text message received
                if(lastByte == TEXT_MSG_MARK){
                    Log.d("TAG", "TEXT MESSAGE RECEIVED")
                    val combinedByteArray = bufferList.fold(ByteArray(0)) { acc, byteArray ->
                        acc + byteArray
                    }
                    val message = combinedByteArray.dropLast(1).toByteArray().decodeToString()

                    emit(
                        message.toBluetoothMessage(
                            isFromLocalUser = false
                        )

                    )
                    bufferList.clear()
                }else if(lastByte == AUDIO_MSG_MARK){
                    Log.d("TAG", "AUDIO MESSAGE RECEIVED")
                    val combinedByteArray = bufferList.fold(ByteArray(0)) { acc, byteArray ->
                        acc + byteArray
                    }
                    emit(


                        combinedByteArray.copyOf(combinedByteArray.size).toBluetoothAudioMessage(false)
                    )
                    bufferList.clear()
                }
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun sendMessage(bytes: ByteArray): Boolean {

        return withContext(Dispatchers.IO) {
            try {
                socket.outputStream.write(bytes)
                Log.e("TAG", "sendMessage:bytecount: ${bytes}--${bytes}")
            } catch (e: IOException) {
                Log.e("TAG", "sendMessage Error: ${e.message}++///+++ ${e.cause}")
                e.printStackTrace()
                return@withContext false
            }

            true
        }
    }
}



