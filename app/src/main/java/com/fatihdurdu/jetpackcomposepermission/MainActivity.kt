package com.fatihdurdu.jetpackcomposepermission

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.fatihdurdu.jetpackcomposepermission.ui.theme.JetpackComposePermissionTheme
import com.google.accompanist.permissions.*

@ExperimentalPermissionsApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposePermissionTheme {
                // A surface container using the 'background' color from the theme
                FeatureThatRequiresPermissions()
            }
        }
    }
}

@Composable
@ExperimentalPermissionsApi
private fun FeatureThatRequiresPermissions() {

    Column() {

        // Camera permission state
        val cameraPermissionState = rememberPermissionState(
            Manifest.permission.CAMERA
        )

        //Write -Read Permission
        val multiplePermissionsState = rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )

        multiplePermissionsState.permissions.forEach { perm ->
            when (perm.permission) {
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                    getPermission(perm, "Write Permission")
                }
                Manifest.permission.READ_EXTERNAL_STORAGE -> {
                    getPermission(perm, "Read Permission")
                }
            }
        }

        getPermission(perm = cameraPermissionState, permissionText = "Camera Permission")
    }

}

@ExperimentalPermissionsApi
@Composable
private fun getPermission(
    perm: PermissionState,
    permissionText: String
) {
    when (perm.status) {
        is PermissionStatus.Granted -> {
            Text("$permissionText permission Granted")
        }
        is PermissionStatus.Denied -> {
            Column {
                val textToShow = if (perm.status.shouldShowRationale) {
                    "The $permissionText is important for this app. Please grant the permission."
                } else {
                    "The $permissionText required for this feature to be available. " +
                            "Please grant the permission"
                }
                Text(textToShow)
                Button(onClick = { perm.launchPermissionRequest() }) {
                    Text("Request $permissionText")
                }
            }
        }
    }
}



