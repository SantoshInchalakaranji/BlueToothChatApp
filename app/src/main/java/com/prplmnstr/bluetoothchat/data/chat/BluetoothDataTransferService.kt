package com.prplmnstr.bluetoothchat.data.chat

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.prplmnstr.bluetoothchat.domain.chat.BluetoothMessage
import com.prplmnstr.bluetoothchat.domain.chat.TransferFailedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.DataInputStream
import java.io.IOException

class BluetoothDataTransferService(
    private val socket: BluetoothSocket
) {
    fun listenForIncomingMessages(): Flow<BluetoothMessage> {
        return flow {
            if (!socket.isConnected) {
                return@flow
            }
            Log.d("TAG", "listenForIncomingMessages ")
            var buffer = ByteArray(990)
            var msg = ""
            while (true) {
                val byteCount = try {
                    socket.inputStream.read(buffer)
                } catch (e: IOException) {
                    Log.d("TAG", "transefer failed Error: $e+++++ ${e.cause}")
                    throw TransferFailedException()
                }
                Log.e("TAG", "listenForIncomingMessages +++++byteCount=$byteCount" +
                        "---")

                msg +=  buffer.decodeToString(
                    endIndex = byteCount
                )
                if(msg.endsWith("~`")){
                    emit(
                        msg.dropLast(2).toBluetoothMessage(
                            isFromLocalUser = false
                        )
                        //   buffer.copyOf(byteCount).toBluetoothAudioMessage(false)
                    )
                    Log.e("TAG", "listenForIncomingMessages +++++msg=$msg")
                    msg=""
                }


            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun sendMessage(bytes: ByteArray): Boolean {
        Log.e("TAG", "sendMessage:bytecount: ${bytes.size}")
        return withContext(Dispatchers.IO) {
            try {
                socket.outputStream.write(bytes)
            } catch (e: IOException) {
                Log.e("TAG", "sendMessage Error: ${e.message}++///+++ ${e.cause}")

                e.printStackTrace()
                return@withContext false
            }

            true
        }
    }
}