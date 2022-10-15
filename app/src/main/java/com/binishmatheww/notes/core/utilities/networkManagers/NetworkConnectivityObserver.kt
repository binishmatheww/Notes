package com.binishmatheww.notes.core.utilities.networkManagers

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import com.binishmatheww.notes.core.utilities.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

class NetworkConnectivityObserver(
    context: Context
): ConnectivityObserver {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var currentStatus = ConnectivityObserver.Status.UnSpecified

    override fun observe(): Flow<ConnectivityObserver.Status> {

        return callbackFlow {

            launch {

                withContext(Dispatchers.IO){

                    currentStatus = try{
                        val socket = SocketFactory.getDefault().createSocket()
                        socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
                        socket.close()
                        ConnectivityObserver.Status.Available
                    }catch (e: IOException){
                        //log("NetworkConnectivityObserver") { "Network : Could not ping google" }
                        ConnectivityObserver.Status.Unavailable
                    }

                }

                send(currentStatus)

            }

            val networkCallback = object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    //log("NetworkConnectivityObserver") { "Network : Available" }
                    launch {
                        currentStatus = ConnectivityObserver.Status.Available
                        send(currentStatus)
                    }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    //log("NetworkConnectivityObserver") { "Network : Losing" }
                    launch {
                        currentStatus = ConnectivityObserver.Status.Losing
                        send(currentStatus)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    //log("NetworkConnectivityObserver") { "Network : Lost" }
                    launch {
                        currentStatus = ConnectivityObserver.Status.Lost
                        send(currentStatus)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    //log("NetworkConnectivityObserver") { "Network : Unavailable" }
                    launch {
                        currentStatus = ConnectivityObserver.Status.Unavailable
                        send(currentStatus)
                    }
                }

            }

            val networkRequest = NetworkRequest.Builder()
                .addCapability(NET_CAPABILITY_INTERNET)
                .build()

            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }

        }.distinctUntilChanged()

    }

}