package com.rosen.convata.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class ConnectionDetector(private val context: Context) {
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val networkCapability = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }
}