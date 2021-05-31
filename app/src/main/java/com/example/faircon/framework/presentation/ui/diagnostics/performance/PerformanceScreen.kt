package com.example.faircon.framework.presentation.ui.diagnostics.performance

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import com.example.faircon.SettingPreferences
import com.example.faircon.framework.presentation.theme.FairconTheme
import com.example.faircon.framework.presentation.ui.settings.SettingsViewModel

@Composable
fun PerformanceScreen(
    theme: SettingPreferences.Theme,
    isWifiAvailable: Boolean,
    scaffoldState: ScaffoldState
) {

    FairconTheme(
        theme = theme,
        scaffoldState = scaffoldState
    ) {

        val performanceViewModel = hiltNavGraphViewModel<PerformanceViewModel>()

        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState },
        ) {

            Box(modifier = Modifier.fillMaxSize()){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Performance"
                )
            }
        }
    }
}