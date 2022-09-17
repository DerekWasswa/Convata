package com.rosen.convata.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "rates")
data class Rates(
    @ColumnInfo(name = "date")
    @SerializedName("date")
    @Expose
    val date: String,

    @PrimaryKey
    @ColumnInfo(name = "base")
    @SerializedName("base")
    @Expose
    val base: String,

    @ColumnInfo(name = "rates")
    @SerializedName("rates")
    @Expose
    val rates: Map<String, Double>
)