package com.rickrip.permissons_handling.presentation.enums

import android.Manifest


enum class PermissionType(
    val legacyName: String,
    val descriptionNormal: String,
    val descriptionPermaDeclined: String,
) {
    CAMERA_PERMISSION(
        Manifest.permission.CAMERA,
        descriptionNormal = "The app needs permission to use a Camera in order to make a photo",
        descriptionPermaDeclined = "It seems you permanently declined Camera permission." +
                "You can got to the app setting to grant it.",
    ),
    MIC_PERMISSION(
        Manifest.permission.RECORD_AUDIO,
        descriptionNormal = "The app needs permission to use a Microphone",
        descriptionPermaDeclined = "It seems you permanently declined Microphone permission." +
                "You can got to the app setting to grant it.",
    ),
    CALL_PERMISSION(
        Manifest.permission.CALL_PHONE,
        descriptionNormal = "The app needs permission to use a Phone calling",
        descriptionPermaDeclined = "It seems you permanently declined Phone Calling permission." +
                "You can got to the app setting to grant it.",
    ),
}