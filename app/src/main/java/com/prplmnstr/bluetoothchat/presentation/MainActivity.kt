package com.prplmnstr.bluetoothchat.presentation

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.prplmnstr.bluetoothchat.ui.theme.BlueToothChatTheme
import androidx.hilt.navigation.compose.hiltViewModel
import com.prplmnstr.bluetoothchat.presentation.components.App
import com.prplmnstr.bluetoothchat.presentation.components.ChatScreen
import com.prplmnstr.bluetoothchat.presentation.components.DeviceScreen
import com.prplmnstr.bluetoothchat.presentation.components.PermissionDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    // Check if Bluetooth is enabled
    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("TAG", "main: running")

        // Activity Result Launcher for enabling Bluetooth
        val enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { /* Not needed */ }


        // Activity Result Launcher for requesting permissions
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perms ->
            // Check if Bluetooth permissions are granted
            val canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                perms[Manifest.permission.BLUETOOTH_CONNECT] == true
            } else true


            // If Bluetooth permissions are granted and Bluetooth is not enabled, request to enable Bluetooth
            if (canEnableBluetooth && !isBluetoothEnabled) {
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }

        }

        // Launch permission request based on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.RECORD_AUDIO
                )
            )
        } else {

            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.RECORD_AUDIO
                )
            )
        }

        makeDeviceDiscoverable()

        setContent {
            BlueToothChatTheme {

                CheckAndRequestLocationPermission(enableBluetoothLauncher)

                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {

                    App(
                       applicationContext
                    )

                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun makeDeviceDiscoverable() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            Toast.makeText(applicationContext, "Please Enable Bluetooth Permissions. ", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Launch the intent to make the device discoverable
        val requestCode = 1;
        val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        }
        startActivityForResult(discoverableIntent, requestCode)


    }

    @Composable
    private fun CheckAndRequestLocationPermission(enableBluetoothLauncher: ActivityResultLauncher<Intent>) {
        var dialogOpen by remember { mutableStateOf(true) }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S && !isLocationEnabled(this)) {
            if (dialogOpen) {
                PermissionDialog(onOkClick = {
                    enableBluetoothLauncher.launch(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    )
                }, onDismissClick = {
                    dialogOpen = false
                }
                )
            }

        }
    }

    /**
     * Check if location is enabled.
     *
     * @param context The application context.
     * @return True if location is enabled, false otherwise.
     */
    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    private fun hasPermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return applicationContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }

    }
}