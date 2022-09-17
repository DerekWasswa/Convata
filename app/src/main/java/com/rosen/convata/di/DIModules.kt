package com.rosen.convata.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.rosen.convata.BuildConfig.DEBUG
import com.rosen.convata.api.ConversionApi
import com.rosen.convata.data.AppDatabase
import com.rosen.convata.data.models.CurrencyDao
import com.rosen.convata.data.models.RateDao
import com.rosen.convata.data.repository.ConversionRepositoryImpl
import com.rosen.convata.utils.ConnectionDetector
import com.rosen.convata.utils.Constants
import com.rosen.convata.viewmodels.ConversionViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {

    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "convata_db")
            .fallbackToDestructiveMigration()
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

val apiModule = module {
    fun provideConversionApi(retrofit: Retrofit): ConversionApi {
        return retrofit.create(ConversionApi::class.java)
    }
    single { provideConversionApi(get()) }
}

val networkModule = module {
    val connectTimeout : Long = 60
    val readTimeout : Long  = 60

    fun provideHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
        if (DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        okHttpClientBuilder.build()
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(client: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    fun provideConnectionDetector(context: Context) : ConnectionDetector {
        return ConnectionDetector(context)
    }

    single { provideHttpClient() }
    single {
        val baseUrl = Constants.BASE_URL
        provideRetrofit(get(), baseUrl)
    }
    single { provideConnectionDetector(androidContext()) }
}

val viewModelModule = module {
    viewModel {
        ConversionViewModel(ConversionRepositoryImpl(get(), get(), get(), get()))
    }
}