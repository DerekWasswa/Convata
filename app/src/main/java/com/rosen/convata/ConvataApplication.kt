package com.rosen.convata

import android.app.Application
import com.rosen.convata.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ConvataApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@ConvataApplication)
            modules(listOf(databaseModule, apiModule, networkModule, viewModelModule))
        }

    }

}