package com.prplmnstr.bluetoothchat.di

import android.content.Context
import com.prplmnstr.bluetoothchat.data.chat.AndroidBluetoothController
import com.prplmnstr.bluetoothchat.data.chat.models.MessageEntity
import com.prplmnstr.bluetoothchat.data.chat.realm.RealmDaoImpl
import com.prplmnstr.bluetoothchat.data.chat.storage.ExternalStorage
import com.prplmnstr.bluetoothchat.domain.chat.BluetoothController
import com.prplmnstr.bluetoothchat.domain.chat.playback.AndroidAudioPlayer
import com.prplmnstr.bluetoothchat.domain.chat.recorder.AndroidAudioRecorder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBluetoothController(@ApplicationContext context: Context): BluetoothController {
        return AndroidBluetoothController(context)
    }

    @Provides
    @Singleton
    fun provideAudioPlayer(@ApplicationContext context: Context): AndroidAudioPlayer {
        return AndroidAudioPlayer(context)
    }

    @Provides
    @Singleton
    fun provideAudioRecorder(@ApplicationContext context: Context): AndroidAudioRecorder {
        return AndroidAudioRecorder(context)
    }

    @Provides
    @Singleton
    fun provideCacheDir(@ApplicationContext context: Context): File {
        return context.cacheDir
    }

    @Provides
    @Singleton
    fun provideExternalStorage(@ApplicationContext context: Context): ExternalStorage {
        return ExternalStorage(context)
    }


    @Provides
    @Singleton
    fun provideRealm(
        @ApplicationContext context: Context,
    ): Realm {
        val realmConfig = RealmConfiguration.create(
            schema = setOf(
                MessageEntity::class,
            ),
        )
        return Realm.open(realmConfig)
    }


        @Provides
        @Singleton
        fun provideRealmDaoImpl(realm: Realm): RealmDaoImpl {
            return RealmDaoImpl(realm)
        }


}