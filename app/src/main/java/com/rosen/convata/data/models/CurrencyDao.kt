package com.rosen.convata.data.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM supported_currencies LIMIT 1")
    fun getCurrencies(): List<Currency>

    @Insert
    suspend fun insertCurrency(currency: Currency): Long

}