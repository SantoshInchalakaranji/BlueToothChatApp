package com.prplmnstr.bluetoothchat.data.chat.realm

import android.util.Log
import com.prplmnstr.bluetoothchat.data.chat.models.MessageEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class RealmDaoImpl @Inject constructor(private val realmDb: Realm):RealmDao {

    override suspend fun insertMessage(message: MessageEntity) {
        realmDb.write {
          val result =   copyToRealm(message, updatePolicy = UpdatePolicy.ALL)

        }
    }

    override fun getAllMessages(senderAddress:String): Flow<List<MessageEntity>> {
        return realmDb
            .query<MessageEntity>(
                "senderAddress == $0",
                senderAddress
            )
            .asFlow()
            .map {results->
                results.list.toList()
            }

    }

    override suspend fun deleteMessage(message: MessageEntity) {
       realmDb.write {

           val latestMessage = findLatest(message)?:return@write
           delete(latestMessage)
       }
    }

}