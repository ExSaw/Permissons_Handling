package com.rickrip.permissons_handling.presentation.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rickrip.permissons_handling.presentation.composable.PermissionDialog
import com.rickrip.permissons_handling.presentation.enums.PermissionType
import com.rickrip.permissons_handling.presentation.viewmodel.MainViewModel
import com.rickrip.permissons_handling.ui.theme.Permissons_HandlingTheme

class MainActivity : ComponentActivity() {

    private val permissionsToRequest = arrayOf(
        PermissionType.CAMERA_PERMISSION.legacyName,
        PermissionType.CALL_PERMISSION.legacyName
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Permissons_HandlingTheme {
                val viewModel = viewModel<MainViewModel>()
                val dialogQueue = viewModel.visiblePermissionDialogsQueue

                /**
                 * * in a real app don't use
                 * cameraPermissionResultLauncher and multiplePermissionResultLauncher
                 * on a single screen
                 */
                val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        viewModel.onPermissionResult(
                            permission = PermissionType.CAMERA_PERMISSION,
                            isGranted = isGranted
                        )
                    }
                )

                val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { perms ->
                        permissionsToRequest.forEach { permission ->
                            PermissionType.entries
                                .find { it.legacyName == permission }
                                ?.let {
                                    viewModel.onPermissionResult(
                                        permission = it,
                                        isGranted = perms[permission] == true
                                    )
                                }
                        }
                    }
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(onClick = {
                        cameraPermissionResultLauncher.launch(
                            PermissionType.CAMERA_PERMISSION.legacyName
                        )
                    }) {
                        Text(text = "Request one permission")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        multiplePermissionResultLauncher.launch(permissionsToRequest)
                    }
                    ) {
                        Text(text = "Request multiple permissions")
                    }
                }

                /**
                 * * https://www.youtube.com/watch?v=D3JCtaK8LSU
                 * * shouldShowRequestPermissionRationale returns false for the very first request too
                 * and false if permission wasn't granted permanently !
                 */
                dialogQueue
                    .reversed()
                    .forEach { permission ->
                        PermissionDialog(
                            permission = permission,
                            isPermanentlyDeclined = this
                                .shouldShowRequestPermissionRationale(permission.legacyName),
                            onDismiss = viewModel::dismissDialog,
                            onConfirm = {
                                viewModel.dismissDialog()
                                multiplePermissionResultLauncher.launch(
                                    arrayOf(permission.legacyName)
                                )
                            },
                            onGoToAppSettingsClick = {
                                openAppSetting()
                            }
                        )
                    }
            }
        }
    }
}

fun Activity.openAppSetting() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also { startActivity(it) }
}