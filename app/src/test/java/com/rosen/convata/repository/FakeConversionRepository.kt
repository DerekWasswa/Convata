package com.rosen.convata.repository

import com.rosen.convata.data.models.Currency
import com.rosen.convata.data.models.Rates
import com.rosen.convata.data.repository.ConversionRepository
import com.rosen.convata.dummyCurrencies
import com.rosen.convata.dummyRates
import com.rosen.convata.utils.Resource

class FakeConversionRepository: ConversionRepository {
    override suspend fun fetchCurrencies(): Resource<Currency> {
        return Resource.success(dummyCurrencies)
    }

    override suspend fun fetchRates(options: Map<String, Any>): Resource<Rates> {
        return Resource.success(dummyRates)
    }

    override suspend fun getLocalCurrencies(): List<Currency> = listOf(dummyCurrencies)

    override suspend fun getLocalRates(base: String): List<Rates> = listOf(dummyRates)
}