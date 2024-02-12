package com.prplmnstr.bluetoothchat.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PermissionDialog(
    onOkClick: () -> Unit,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier
){


    AlertDialog(onDismissRequest =
        onDismissClick
    ,
        confirmButton =  {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.fillMaxWidth())
                Text(
                    text = "Grant Permission",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onOkClick()
                            onDismissClick()
                        }
                        .padding( 16.dp)
                )
            }
        },
        title = {
            Text(text = "Permission required")
        },
        text = {
            Text(text = "For devices with android version < 11.0 "+
                   "the app require location permission."
            )
        },
        modifier =  modifier
    )
}

@Preview
@Composable
fun PermissionDialogPreview() {
    PermissionDialog(
        onOkClick = { /* Define onOkClick action */ },
        onDismissClick = { /* Define onDismissClick action */ }
    )
}
