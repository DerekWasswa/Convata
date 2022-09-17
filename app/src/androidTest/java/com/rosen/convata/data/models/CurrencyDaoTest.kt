package com.rosen.convata.data.models

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.rosen.convata.data.AppDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class CurrencyDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var appDatabase: AppDatabase
    private lateinit var currencyDao: CurrencyDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        currencyDao = appDatabase.currencyDao()
    }

    @Test
    fun tests_inserts_supported_currencies() = runBlocking {
        val currency = Currency(currencies = mapOf(
            "AED" to Symbol(
                description = "United Arab Emirates Dirham",
                code = "AED"
            ),
            "AFN" to Symbol(
                description = "Afghan Afghani",
                code = "AFN"
            )
        ))
        currencyDao.insertCurrency(currency)
        val results = currencyDao.getCurrencies()
        assertEquals(1, results.size)
    }

    @Test
    fun tests_fetches_supported_currencies() = runBlocking {
        val results = currencyDao.getCurrencies()
        assertEquals(0, results.size)
    }


    @After
    fun teardown() {
        appDatabase.close()
    }

}