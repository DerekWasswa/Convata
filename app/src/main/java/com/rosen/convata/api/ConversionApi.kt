package com.rosen.convata.api

import com.rosen.convata.data.models.Currency
import com.rosen.convata.data.models.Rates
import retrofit2.http.GET
import retrofit2.http.QueryMap

@JvmSuppressWildcards
interface ConversionApi {

    @GET("symbols")
    suspend fun getCurrencies(): Currency

    @GET("latest")
    suspend fun getLatestRates(@QueryMap options: Map<String, Any>): Rates

}