package com.prplmnstr.bluetoothchat.di

import android.content.Context
import com.prplmnstr.bluetoothchat.data.chat.AndroidBluetoothController
import com.prplmnstr.bluetoothchat.domain.chat.BluetoothController
import com.prplmnstr.bluetoothchat.domain.chat.playback.AndroidAudioPlayer
import com.prplmnstr.bluetoothchat.domain.chat.playback.AudioPlayer
import com.prplmnstr.bluetoothchat.domain.chat.recorder.AndroidAudioRecorder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBluetoothController(@ApplicationContext context: Context):BluetoothController{
        return AndroidBluetoothController(context)
    }

    @Provides
    @Singleton
    fun provideAudioPlayer(@ApplicationContext context: Context):AndroidAudioPlayer{
        return AndroidAudioPlayer(context)
    }

    @Provides
    @Singleton
    fun provideAudioRecorder(@ApplicationContext context: Context):AndroidAudioRecorder{
        return AndroidAudioRecorder(context)
    }
    @Provides
    @Singleton
    fun provideCacheDir(@ApplicationContext context: Context): File {
        return context.cacheDir
    }

}