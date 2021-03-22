package com.example.faircon.business.interactors.main.home

import android.content.Context
import android.net.wifi.WifiManager
import com.example.faircon.business.domain.state.*
import com.example.faircon.framework.presentation.ui.BaseApplication
import com.example.faircon.framework.presentation.ui.main.home.state.HomeViewState
import kotlinx.coroutines.flow.flow

class DisconnectFromFaircon(
    private val app: BaseApplication
) {
    fun execute(
        stateEvent: StateEvent
    ) = flow {

        val wifiManager = app.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = false

        emit(
            DataState.data(
                data = HomeViewState(connected = false),
                response = Response(
                    message = "Disconnected from Faircon",
                    uiType = UiType.None,
                    messageType = MessageType.Info
                ),
                stateEvent = stateEvent
            )
        )
    }
}
