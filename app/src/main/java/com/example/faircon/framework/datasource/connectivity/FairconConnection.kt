package com.example.faircon.framework.datasource.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.faircon.business.interactors.app.IsConnectedToFaircon
import com.example.faircon.framework.presentation.ui.BaseApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FairconConnection
@Inject
constructor(
    application: BaseApplication,
    private val isConnectedToFaircon: IsConnectedToFaircon
) {

    private val _available = MutableStateFlow<Boolean?>(null)
    val available: StateFlow<Boolean?> = _available

    private var networkCallback: ConnectivityManager.NetworkCallback
    private val connectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    init {
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            CoroutineScope(Dispatchers.IO).launch {
                _available.value = isConnectedToFaircon.execute()
            }
        }

        override fun onLost(network: Network) {
            CoroutineScope(Dispatchers.IO).launch {
                _available.value = isConnectedToFaircon.execute()
            }
        }
    }
}
