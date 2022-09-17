package com.rosen.convata.data.repository

import com.rosen.convata.api.ConversionApi
import com.rosen.convata.data.models.Currency
import com.rosen.convata.data.models.CurrencyDao
import com.rosen.convata.data.models.RateDao
import com.rosen.convata.data.models.Rates
import com.rosen.convata.utils.ConnectionDetector
import com.rosen.convata.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConversionRepositoryImpl(private val conversionApi: ConversionApi, private val connectionDetector: ConnectionDetector, private val ratesDao: RateDao, private val currencyDao: CurrencyDao): ConversionRepository {

    override suspend fun fetchCurrencies(): Resource<Currency> {
        return if (connectionDetector.isNetworkAvailable()) {
            try {
                val currency = conversionApi.getCurrencies()
                withContext(Dispatchers.IO) { currencyDao.insertCurrency(currency) }
                Resource.success(currency)
            } catch (e: Exception) {
                Resource.error(e.message?:"Fetching Supported Currencies Error!")
            }
        } else {
            val data = getLocalCurrencies()
            Resource.success(data.first())
        }
    }

    override suspend fun fetchRates(options: Map<String, Any>): Resource<Rates> {
        return if (connectionDetector.isNetworkAvailable()) {
            try {
                val response = conversionApi.getLatestRates(options)
                withContext(Dispatchers.IO) { ratesDao.insertRates(response) }
                Resource.success(response)
            } catch (e: Exception) {
                Resource.error(e.message?:"Fetching Latest Rates Error!")
            }
        } else {
            val data = getLocalRates(options["base"].toString())
            Resource.success(data.first())
        }
    }

    override suspend fun getLocalCurrencies(): List<Currency> {
        return withContext(Dispatchers.IO) {
            currencyDao.getCurrencies()
        }
    }

    override suspend fun getLocalRates(base: String): List<Rates> {
        return withContext(Dispatchers.IO) {
            ratesDao.getRates(base)
        }
    }

}