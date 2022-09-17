package com.rosen.convata.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rosen.convata.data.models.*

@Database(entities = [Currency::class, Rates::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rateDao(): RateDao
    abstract fun currencyDao(): CurrencyDao
}