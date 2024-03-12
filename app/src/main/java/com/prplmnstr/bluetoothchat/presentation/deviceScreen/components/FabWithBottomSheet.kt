package com.prplmnstr.bluetoothchat.presentation.deviceScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prplmnstr.bluetoothchat.R

@Composable
fun FabWithBottomSheet(
    onStartServer: () -> Unit,
) {
    // Create a boolean state to track whether the bottom sheet is open or not
    val (isBottomSheetOpen, setIsBottomSheetOpen) = remember { mutableStateOf(false) }

//    // Text and icon for FAB
//    val fabText = if (isBottomSheetOpen) "Stop Server" else "Start Server"
//    val fabIcon = if (isBottomSheetOpen) Icons.Filled.Close else Icons.Filled.Share

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Your main content here

        // FAB (Floating Action Button) with text and icon
        ExtendedFloatingActionButton(
            onClick = {
                onStartServer()
                setIsBottomSheetOpen(!isBottomSheetOpen)
                      },
            icon = { Icon(
                painterResource(id = R.drawable.bluetooth_searching), "Extended floating action button.") },
            text = { Text(text = "Start Server") },
            modifier = Modifier.padding(16.dp)


        )

        // Bottom Sheet
        if (isBottomSheetOpen) {
            BottomSheet(
                onDismiss = { setIsBottomSheetOpen(false) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit
) {

    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.primary,
       dragHandle = { BottomSheetDefaults.DragHandle(
           color = MaterialTheme.colorScheme.inversePrimary
       ) },
        onDismissRequest = {
           onDismiss()
        },
    ) {

            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "waiting for connection...")
                RippleLoadingAnimation()
            }



    }

}

@Preview
@Composable
fun PreviewComposableWithFabAndBottomSheet() {
    FabWithBottomSheet({})
}
