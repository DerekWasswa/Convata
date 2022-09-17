package com.rosen.convata.data.repository

import com.rosen.convata.data.models.Currency
import com.rosen.convata.data.models.Rates
import com.rosen.convata.utils.Resource

interface ConversionRepository {

    suspend fun fetchCurrencies(): Resource<Currency>
    suspend fun fetchRates(options: Map<String, Any>): Resource<Rates>
    suspend fun getLocalCurrencies(): List<Currency>
    suspend fun getLocalRates(base: String): List<Rates>

}