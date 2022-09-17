package com.rosen.convata.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "supported_currencies")
data class Currency(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "currencies")
    @SerializedName("symbols")
    @Expose
    val currencies: Map<String, Symbol>
)

data class Symbol (
    val description: String,
    val code: String
)