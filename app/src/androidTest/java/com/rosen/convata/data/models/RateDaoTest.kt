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
class RateDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var appDatabase: AppDatabase
    private lateinit var rateDao: RateDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        rateDao = appDatabase.rateDao()
    }

    @Test
    fun tests_inserts_rates() = runBlocking {
        val rates = Rates(
            date = "now",
            base = "USD",
            rates = mapOf(
                "AED" to 3.672538,
                "AFN" to 66.879999
            )
        )
        rateDao.insertRates(rates)
        val results = rateDao.getRates("USD")
        assertEquals(1, results.size)
    }

    @Test
    fun tests_fetches_rates() = runBlocking {
        val results = rateDao.getRates("USD")
        assertEquals(0, results.size)
    }


    @After
    fun teardown() {
        appDatabase.close()
    }

}