package com.prplmnstr.bluetoothchat.data.chat.realm

import com.prplmnstr.bluetoothchat.data.chat.models.MessageEntity
import kotlinx.coroutines.flow.Flow

interface RealmDao {
    suspend fun insertMessage(message: MessageEntity)
    fun getAllMessages(senderAddress:String): Flow<List<MessageEntity>>
    suspend fun deleteMessage(message: MessageEntity)
}

