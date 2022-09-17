package com.rosen.convata.data.repository

import com.rosen.convata.api.ConversionApi
import com.rosen.convata.data.models.CurrencyDao
import com.rosen.convata.data.models.RateDao
import com.rosen.convata.dummyCurrencies
import com.rosen.convata.dummyRates
import com.rosen.convata.utils.ConnectionDetector
import com.rosen.convata.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.anyMap
import org.mockito.Mockito.anyString
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ConversionRepositoryImplTest {

    private lateinit var conversionRepositoryImpl: ConversionRepositoryImpl

    @Mock
    lateinit var conversionApi: ConversionApi

    @Mock
    lateinit var ratesDao: RateDao

    @Mock
    lateinit var currencyDao: CurrencyDao

    @Mock
    lateinit var connectionDetector: ConnectionDetector

    @Before
    fun setUp() {
        conversionRepositoryImpl = ConversionRepositoryImpl(conversionApi, connectionDetector, ratesDao, currencyDao)
    }

    @ExperimentalCoroutinesApi
    @Test()
    fun tests_fetch_network_currencies() = runBlocking {
        Mockito.`when`(connectionDetector.isNetworkAvailable())
            .thenReturn(true)

        Mockito.`when`(conversionApi.getCurrencies())
            .thenReturn(dummyCurrencies)

        val result = conversionRepositoryImpl.fetchCurrencies()

        // assertions
        assertEquals(Resource.success(dummyCurrencies), result)
    }

    @ExperimentalCoroutinesApi
    @Test(expected = java.lang.Exception::class)
    fun tests_exception_on_fetch_network_currencies() = runBlocking {
        Mockito.`when`(conversionRepositoryImpl.fetchCurrencies())
            .thenThrow(Exception("Network Exception"))

        val result = conversionRepositoryImpl.fetchCurrencies()

        // assertions
        assertEquals(Resource.Status.ERROR, result.status)
    }

    @ExperimentalCoroutinesApi
    @Test()
    fun tests_fetch_local_currencies() = runBlocking {
        Mockito.`when`(connectionDetector.isNetworkAvailable())
            .thenReturn(false)

        Mockito.`when`(currencyDao.getCurrencies())
            .thenReturn(listOf(dummyCurrencies))

        val result = conversionRepositoryImpl.fetchCurrencies()

        // assertions
        assertEquals(Resource.success(dummyCurrencies), result)
    }

    @ExperimentalCoroutinesApi
    @Test(expected = java.lang.Exception::class)
    fun tests_exception_on_fetch_local_currencies() = runBlocking {
        Mockito.`when`(conversionRepositoryImpl.fetchCurrencies())
            .thenThrow(Exception("RuntimeException"))

        val result = conversionRepositoryImpl.fetchCurrencies()

        // assertions
        assertEquals(Resource.Status.ERROR, result.status)
    }

    @ExperimentalCoroutinesApi
    @Test()
    fun tests_fetch_network_latest_rates() = runBlocking {
        Mockito.`when`(connectionDetector.isNetworkAvailable())
            .thenReturn(true)

        Mockito.`when`(conversionApi.getLatestRates(anyMap()))
            .thenReturn(dummyRates)

        val result = conversionRepositoryImpl.fetchRates(mapOf())

        // assertions
        assertEquals(Resource.success(dummyRates), result)
    }

    @ExperimentalCoroutinesApi
    @Test(expected = java.lang.Exception::class)
    fun tests_exception_on_fetch_network_latest_rates() = runBlocking {
        Mockito.`when`(conversionRepositoryImpl.fetchRates(anyMap()))
            .thenThrow(Exception("Network Exception"))

        val result = conversionRepositoryImpl.fetchRates(mapOf())

        // assertions
        assertEquals(Resource.Status.ERROR, result.status)
    }

    @ExperimentalCoroutinesApi
    @Test()
    fun tests_fetch_local_latest_rates() = runBlocking {
        Mockito.`when`(connectionDetector.isNetworkAvailable())
            .thenReturn(false)

        Mockito.`when`(ratesDao.getRates(anyString()))
            .thenReturn(listOf(dummyRates))

        val result = conversionRepositoryImpl.fetchRates(mapOf())

        // assertions
        assertEquals(Resource.success(dummyRates), result)
    }

    @ExperimentalCoroutinesApi
    @Test(expected = java.lang.Exception::class)
    fun tests_exception_on_fetch_local_latest_rates() = runBlocking {
        Mockito.`when`(conversionRepositoryImpl.fetchRates(anyMap()))
            .thenThrow(Exception("RuntimeException"))

        val result = conversionRepositoryImpl.fetchRates(mapOf())

        // assertions
        assertEquals(Resource.Status.ERROR, result.status)
    }

    @ExperimentalCoroutinesApi
    @Test()
    fun tests_get_local_currencies() = runBlocking {
        Mockito.`when`(currencyDao.getCurrencies())
            .thenReturn(listOf(dummyCurrencies))

        val result = conversionRepositoryImpl.getLocalCurrencies()

        // assertions
        assertEquals(1, result.size)
        assertEquals(2, result.first().currencies.keys.size)
    }

    @ExperimentalCoroutinesApi
    @Test()
    fun tests_get_local_rates() = runBlocking {
        Mockito.`when`(ratesDao.getRates(anyString()))
            .thenReturn(listOf(dummyRates))

        val result = conversionRepositoryImpl.getLocalRates("USD")

        // assertions
        assertEquals(1, result.size)
        assertEquals("USD", result.first().base)
        assertEquals(2, result.first().rates.keys.size)
    }

}