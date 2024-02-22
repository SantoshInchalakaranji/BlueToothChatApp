package com.prplmnstr.bluetoothchat.presentation

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prplmnstr.bluetoothchat.domain.chat.BluetoothController
import com.prplmnstr.bluetoothchat.domain.chat.BluetoothDeviceDomain
import com.prplmnstr.bluetoothchat.domain.chat.ConnectionResult
import com.prplmnstr.bluetoothchat.domain.chat.playback.AndroidAudioPlayer
import com.prplmnstr.bluetoothchat.domain.chat.recorder.AndroidAudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ViewModel responsible for managing Bluetooth connectivity and data exchange.
 *
 * This ViewModel communicates with the [BluetoothController] to establish connections,
 * send and receive messages, and handle Bluetooth-related events.
 * It also utilizes an [AndroidAudioPlayer] and [AndroidAudioRecorder] for audio playback and recording.
 *
 * @property bluetoothController The [BluetoothController] responsible for handling Bluetooth operations.
 * @property audioPlayer The [AndroidAudioPlayer] for audio playback.
 * @property audioRecorder The [AndroidAudioRecorder] for audio recording.
 * @property cacheDir The cache directory for storing audio files.
 * @property audioFile The audio file being recorded or played.
 */


@HiltViewModel
class BluetoothViewModel @Inject constructor(
    @Singleton
    private val bluetoothController: BluetoothController,
    private val audioPlayer: AndroidAudioPlayer,
    private val audioRecorder: AndroidAudioRecorder,
    private val cacheDir: File
) : ViewModel() {


    private var audioFile: File? = null

    @Singleton
    private val _state = MutableStateFlow(BluetoothUiState())
    val state = combine(
        bluetoothController.scannedDevices,
        bluetoothController.pairedDevices,
        _state
    ) { scannedDevices, pairedDevices, state ->
        Log.e("TAG", "scanning: running")
        state.copy(
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices,
            messages = if (state.isConnected) state.messages else emptyList()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    private var deviceConnectionJob: Job? = null

    init {
        Log.e("TAG", "model: running")
        bluetoothController.isConnected.onEach { isConnected ->
            _state.update { it.copy(isConnected = isConnected) }
        }.launchIn(viewModelScope)

        bluetoothController.errors.onEach { error ->
            _state.update {
                it.copy(
                    errorMessage = error
                )
            }
        }.launchIn(viewModelScope)
    }

    fun connectToDevice(device: BluetoothDeviceDomain) {
        _state.update { it.copy(isConnecting = true, peerDevice = device) }
        deviceConnectionJob = bluetoothController
            .connectToDevice(device)
            .listen()
    }

    fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        bluetoothController.closeConnection()
        _state.update {
            it.copy(
                messages = emptyList(),
                isConnecting = false,
                isConnected = false
            )
        }
    }

    fun waitForIncomingConnections() {
        Log.d("TAG", "BluetoothServer  : STARTED ")
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController
            .startBluetoothServer()
            .listen()
    }


    fun sendMessage(message: String) {

        viewModelScope.launch {
            val bluetoothMessage = bluetoothController.trySendMessage(message)
            if (bluetoothMessage != null) {
                _state.update {
                    it.copy(
                        messages = it.messages + bluetoothMessage
                    )
                }
            }
        }
    }

    fun sendMessage() {

        viewModelScope.launch {
            val bluetoothMessage = audioFile?.let { bluetoothController.trySendMessage(it) }
            if (bluetoothMessage != null) {
                _state.update {
                    it.copy(
                        messages = it.messages + bluetoothMessage
                    )
                }
            }
        }
    }


    fun startScan() {
        Log.d("TAG", "startScan  : started ")
        bluetoothController.startDiscovery()
    }

    fun stopScan() {
        Log.d("TAG", "stopScan  : stopped ")
        bluetoothController.stopDiscovery()
    }

    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            when (result) {
                is ConnectionResult.ConnectionEstablished -> {
                    _state.update {
                        it.copy(
                            isConnected = true,
                            isConnecting = false,
                            errorMessage = null,
                            peerDevice = result.peerDevice
                        )
                    }
                }

                is ConnectionResult.TransferSucceeded -> {
                    _state.update {
                        it.copy(
                            messages = it.messages + result.message
                        )
                    }
                }

                is ConnectionResult.Error -> {
                    _state.update {
                        it.copy(
                            isConnected = false,
                            isConnecting = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
            .catch { throwable ->
                bluetoothController.closeConnection()
                _state.update {
                    it.copy(
                        isConnected = false,
                        isConnecting = false,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun releaseResources() = bluetoothController.release()


    //audio

     fun startRecord() {
        audioFile?.let { audioRecorder.start(it) }
    }

     fun stopRecord() {
        audioRecorder.stop()
         setPlayer()
    }
    fun setPlayer(){
        audioFile?.let { audioPlayer.playFile(it) }
    }


    fun startPlay() {
       audioPlayer.start()
    }

    fun stopPlay() {
        audioPlayer.stop()
    }

    fun seekTo(postion: Int) {
        audioPlayer.seekTo(postion)
    }
    fun getAudioDuration():Int {
        return audioPlayer.getAudioDuration()
    }
    fun getCurrentPosition():Int {
        return audioPlayer.getCurrentPosition()
    }

    fun createAudioFile(name: String) {
        File(cacheDir, name).also {
            audioFile = it
            Log.e("TAG", "createAudioFile  : file created ")
        }
    }

    fun saveByteArrayToFile(audioData: ByteArray){
        try {
            val outputStream = FileOutputStream(audioFile)
            outputStream.write(audioData)
            outputStream.close()
            println("Audio data saved successfully to file: ${audioFile?.absolutePath}")
        } catch (e: IOException) {
            println("Error saving audio data to file: ${e.message}")
        }
    }


    override fun onCleared() {
        super.onCleared()
        //   bluetoothController.release()
        Log.e("TAG", "Viewmodel  : cleared ")
    }
}