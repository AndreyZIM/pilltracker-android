package com.bergenproduction.pilltracker.ui

import android.Manifest
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bergenproduction.pilltracker.core.ui.designsystem.theme.PillTrackerTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@OptIn(ExperimentalPermissionsApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val appState = rememberAppState(viewModel.getTheme())

            val isInitiated = viewModel.isInitiated.collectAsStateWithLifecycle()
            val notificationPermissionState =
                rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)

            val theme = appState.currentTheme.collectAsStateWithLifecycle()

            val navController = appState.navController

            val requestNotificationPermission = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                viewModel.isPermissionGranted(isGranted)
            }
            LaunchedEffect(notificationPermissionState) {
                if (!notificationPermissionState.status.isGranted && notificationPermissionState.status.shouldShowRationale) {
                    // Show rationale if needed
                } else {
                    requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

            PillTrackerTheme(
                theme = theme.value
            ) {
                if (isInitiated.value) {
                    PreparationsApp(
                        appState = appState,
                        navController = navController,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            text = "Инициализация",
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}