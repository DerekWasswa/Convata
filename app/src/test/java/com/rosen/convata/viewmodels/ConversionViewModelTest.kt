package com.rosen.convata.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.rosen.convata.data.models.Currency
import com.rosen.convata.data.models.Rates
import com.rosen.convata.dummyRates
import com.rosen.convata.getOrAwaitValue
import com.rosen.convata.repository.FakeConversionRepository
import com.rosen.convata.utils.ConnectionDetector
import com.rosen.convata.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyMap
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.coroutines.ContinuationInterceptor

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ConversionViewModelTest {

    private lateinit var conversionViewModel: ConversionViewModel
    private val conversionRepository = FakeConversionRepository()

    @Mock
    lateinit var supportedCurrenciesObserver: Observer<in Resource<Currency>>

    @Mock
    lateinit var latestRatesObserver: Observer<in Resource<Rates>>

    @Mock
    lateinit var connectionDetector: ConnectionDetector

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        conversionViewModel = ConversionViewModel(conversionRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    @Throws(java.lang.Exception::class)
    fun tests_fetch_supported_currencies() = runBlockingTest {
        try {
            // observe on the MutableLiveData with an observer
            conversionViewModel.supportedCurrencies.observeForever(supportedCurrenciesObserver)
            conversionViewModel.fetchSupportedCurrencies()

            // assertions
            val result = conversionViewModel.supportedCurrencies.getOrAwaitValue()
            assertEquals(Resource.Status.SUCCESS, result.status)
        } finally {
            conversionViewModel.supportedCurrencies.removeObserver(supportedCurrenciesObserver)
        }
    }


    @ExperimentalCoroutinesApi
    @Test
    @Throws(java.lang.Exception::class)
    fun tests_fetch_latest_rates() = runBlockingTest {
        try {
            // observe on the MutableLiveData with an observer
            conversionViewModel.latestRates.observeForever(latestRatesObserver)
            conversionViewModel.fetchLatestRates("")

            // assertions
            val result = conversionViewModel.latestRates.getOrAwaitValue()
            assertEquals(Resource.Status.SUCCESS, result.status)
        } finally {
            conversionViewModel.latestRates.removeObserver(latestRatesObserver)
        }
    }


    @ExperimentalCoroutinesApi
    @Test(expected = java.lang.Exception::class)
    fun tests_exception_on_fetch_supported_currencies() = runBlocking {
        Mockito.`when`(conversionRepository.fetchCurrencies())
            .thenThrow(Exception("Runtime Exception"))

        try {
            // observe on the MutableLiveData with an observer
            conversionViewModel.fetchSupportedCurrencies()
            conversionViewModel.supportedCurrencies.observeForever(supportedCurrenciesObserver)

            // assertions
            assertEquals(Resource.Status.ERROR, conversionViewModel.supportedCurrencies.value?.status)
        } finally {
            conversionViewModel.supportedCurrencies.removeObserver(supportedCurrenciesObserver)
        }
    }

    @ExperimentalCoroutinesApi
    @Test(expected = java.lang.Exception::class)
    fun tests_exception_on_fetch_latest_rates() = runBlocking {
        Mockito.`when`(conversionRepository.fetchRates(anyMap()))
            .thenThrow(Exception("Runtime Exception"))

        try {
            // observe on the MutableLiveData with an observer
            conversionViewModel.fetchLatestRates(anyString())
            conversionViewModel.latestRates.observeForever(latestRatesObserver)

            // assertions
            assertEquals(Resource.Status.ERROR, conversionViewModel.latestRates.value?.status)
        } finally {
            conversionViewModel.latestRates.removeObserver(latestRatesObserver)
        }
    }

    @Test
    fun tests_compute_conversions() {
        val result = conversionViewModel.computeConversions(
            dummyRates,
            "100",
        )
        assertEquals(2, result.size)
    }

}

@ExperimentalCoroutinesApi
class MainCoroutineRule : TestWatcher(), TestCoroutineScope by TestCoroutineScope() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(this.coroutineContext[ContinuationInterceptor] as CoroutineDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }

}