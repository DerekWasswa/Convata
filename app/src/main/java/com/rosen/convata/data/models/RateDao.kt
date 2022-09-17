package com.rosen.convata.data.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RateDao {

    @Query("SELECT * FROM rates WHERE base = :baseStr LIMIT 1")
    fun getRates(baseStr: String): List<Rates>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: Rates): Long

}