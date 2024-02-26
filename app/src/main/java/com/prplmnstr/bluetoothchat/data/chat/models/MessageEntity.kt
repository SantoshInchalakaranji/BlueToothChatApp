package com.prplmnstr.bluetoothchat.data.chat.models

import android.util.Log
import com.prplmnstr.bluetoothchat.data.chat.storage.ExternalStorage
import com.prplmnstr.bluetoothchat.domain.chat.BluetoothMessage
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class MessageEntity: RealmObject {
    @PrimaryKey var _id : ObjectId = ObjectId()
    var type:Int = MessageType.DEFAULT.value
    var data: String = ""
    var date: String= ""
    var time: String= ""
    var senderName: String= ""
    var senderAddress: String= ""
    var isFromLocalUser: Boolean = true
}

enum class MessageType(val value: Int) {
    DEFAULT(0),
    TEXT(1),
    AUDIO(2),
    IMAGE(3)
}

fun MessageEntity.toBluetoothMessage(externalStorage: ExternalStorage): BluetoothMessage {

    return when (type) {
        MessageType.TEXT.value -> {
            BluetoothMessage.TextMessage(
                text = data,
                date = date,
                time = time,
                senderName = senderName,
                senderAddress = senderAddress,
                isFromLocalUser = isFromLocalUser
            )
        }
        MessageType.AUDIO.value -> {
            Log.e("TAAG", "toBluetoothMessage:AudioMessage--${this.data} ", )
            BluetoothMessage.AudioMessage(
                audioData = externalStorage.retrieveAudioFile(data)?: ByteArray(0),
                date = date,
                time = time,
                senderName = senderName,
                senderAddress = senderAddress,
                isFromLocalUser = isFromLocalUser
            )
        }
        MessageType.IMAGE.value -> {
            BluetoothMessage.ImageMessage(
                imageData = data.toByteArray(),
                date = date,
                time = time,
                senderName = senderName,
                senderAddress = senderAddress,
                isFromLocalUser = isFromLocalUser
            )
        }
        else -> {
            // Default case if type does not match any known BluetoothMessage type
            // You can handle this case based on your application logic
            // For example, throw an exception or return a default message
            BluetoothMessage.TextMessage(
                text = "Unknown message type",
                date = date,
                time = time,
                senderName = senderName,
                senderAddress = senderAddress,
                isFromLocalUser = isFromLocalUser
            )
        }
    }
}


