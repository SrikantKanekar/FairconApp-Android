package com.example.faircon.framework.datasource.connectivity

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import com.example.faircon.business.domain.util.printLogNetwork
import com.example.faircon.business.interactors.app.DoesNetworkHaveInternet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Save all available networks with an internet connection to a set [validNetworks].
 *
 * As long as the size of the set > 0, this LiveData emits true.
 * MinSdk = 21.
 */
class ConnectionLiveData(context: Context) : LiveData<Boolean>() {

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetworks: MutableSet<Network> = HashSet()

    private fun checkValidNetworks() {
        postValue(validNetworks.size > 0)
    }

    override fun onActive() {
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()
        cm.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onInactive() {
        cm.unregisterNetworkCallback(networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {

        //Called when a network is detected. If that network has internet, save it in the Set.
        override fun onAvailable(network: Network) {

            val networkCapabilities = cm.getNetworkCapabilities(network)
            val hasInternetCapability = networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)
            printLogNetwork(
                className = "ConnectionLiveData",
                message = "onAvailable: network:${network}, hasInternet:$hasInternetCapability"
            )

            if (hasInternetCapability == true) {
                // check if this network actually has internet
                CoroutineScope(Dispatchers.IO).launch {
                    val hasInternet = DoesNetworkHaveInternet.execute(network.socketFactory)
                    if (hasInternet) {
                        withContext(Dispatchers.Main) {
                            printLogNetwork(
                                className = "ConnectionLiveData",
                                message = "onAvailable: adding network. $network"
                            )
                            validNetworks.add(network)
                            checkValidNetworks()
                        }
                    }
                }
            }
        }

        //If the callback was registered with registerNetworkCallback()
        // it will be called for each network which no longer satisfies the criteria of the callback.
        override fun onLost(network: Network) {
            printLogNetwork("ConnectionLiveData", "onLost: $network")
            validNetworks.remove(network)
            checkValidNetworks()
        }
    }
}