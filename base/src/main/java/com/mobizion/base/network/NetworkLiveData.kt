/**
 * created by tahir baig
 * 3 march 2022
 */
package com.mobizion.base.network

import android.app.Application
import android.content.Context
import android.net.*
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object NetworkLiveData:LiveData<NetworkInfo>() {

    private lateinit var application: Application
    private lateinit var networkRequest: NetworkRequest

    override fun onActive() {
        super.onActive()
        getDetails()
    }

    fun init(application: Application) {
        NetworkLiveData.application = application
        networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
    }

    private fun getDetails() {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val capabilities = cm.getNetworkCapabilities(network)
                val hasInternetCapability = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?:false
                val hasWifiCapability = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?:false
                if (hasInternetCapability){
                    CoroutineScope(IO).launch {
                        val hasInternet = execute()
                        if (hasInternet){
                            withContext(Main){
                                postValue(
                                    NetworkInfo(
                                    isActive = hasInternet,
                                    isWifi = hasWifiCapability
                                )
                                )
                            }
                        }else{
                            postValue(
                                NetworkInfo(
                                isActive = hasInternet,
                                isWifi = hasWifiCapability
                            )
                            )
                        }
                    }
                }else{
                    postValue(
                        NetworkInfo(
                        isActive = false,
                        isWifi = hasWifiCapability
                    )
                    )
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                postValue(
                    NetworkInfo(
                    isActive = false,
                    isWifi = false
                )
                )
            }

            override fun onUnavailable() {
                super.onUnavailable()
                postValue(
                    NetworkInfo(
                    isActive = false,
                    isWifi = false
                )
                )
            }
        })
    }


    fun execute():Boolean{
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8",53),1500)
            socket.close()
            Log.v("netowrkInfo","Network is connect")
            true
        }catch (exception:IOException){
            Log.v("netowrkInfo","Network is connect")
            false
        }
    }
}