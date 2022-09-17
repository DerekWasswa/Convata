package com.rosen.convata

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.rosen.convata.api.ConversionApi
import com.rosen.convata.data.AppDatabase
import com.rosen.convata.data.models.CurrencyDao
import com.rosen.convata.data.models.RateDao
import com.rosen.convata.data.repository.ConversionRepositoryImpl
import com.rosen.convata.utils.ConnectionDetector
import com.rosen.convata.viewmodels.ConversionViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ConvataTestApplication: Application() {

    private val testDatabaseModule = module {

        fun provideDatabase(application: Application): AppDatabase {
            return Room.inMemoryDatabaseBuilder(application, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        }

        fun provideCurrencyDao(database: AppDatabase): CurrencyDao {
            return  database.currencyDao()
        }

        fun provideRatesDao(database: AppDatabase): RateDao {
            return  database.rateDao()
        }

        single { provideDatabase(androidApplication()) }
        single { provideCurrencyDao(get()) }
        single { provideRatesDao(get()) }

    }

    private val testApiModule = module {
        fun provideConversionApi(retrofit: Retrofit): ConversionApi {
            return retrofit.create(ConversionApi::class.java)
        }
        single { provideConversionApi(get()) }
    }

    private val testNetworkModule = module {
        fun provideHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS)
                .build()
        }

        fun provideRetrofit(client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://127.0.0.1:8080/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        fun provideConnectionDetector(context: Context) : ConnectionDetector {
            return ConnectionDetector(context)
        }

        single { provideHttpClient() }
        single { provideRetrofit(get()) }
        single { provideConnectionDetector(androidContext()) }
    }

    private val testViewModelModule = module {
        viewModel { ConversionViewModel(conversionRepository = FakeConversionRepository()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ConvataTestApplication)
            modules(listOf(testDatabaseModule, testApiModule, testNetworkModule, testViewModelModule))
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }

}