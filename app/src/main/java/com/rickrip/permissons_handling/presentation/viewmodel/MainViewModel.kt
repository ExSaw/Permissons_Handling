package com.rickrip.permissons_handling.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.rickrip.permissons_handling.presentation.enums.PermissionType

class MainViewModel : ViewModel() {

    val visiblePermissionDialogsQueue = mutableStateListOf<PermissionType>()

    fun dismissDialog() {
        visiblePermissionDialogsQueue.removeFirst()
    }

    fun onPermissionResult(
        permission: PermissionType,
        isGranted: Boolean,
    ) {
        if (!isGranted && !visiblePermissionDialogsQueue.contains(permission)) {
            visiblePermissionDialogsQueue.add(permission)
        }
    }

}