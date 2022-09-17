package com.rosen.convata.data.models

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun toSymbol(value: String): Map<String, Symbol> =
        Gson().fromJson(value, object : TypeToken<Map<String, Symbol>>() {}.type)

    @TypeConverter
    fun symbolToString(value: Map<String, Symbol>): String =
        Gson().toJson(value, object : TypeToken<Map<String, Symbol>>() {}.type)

    @TypeConverter
    fun toHashMap(value: String): Map<String, String> =
        Gson().fromJson(value, object : TypeToken<Map<String, String>>() {}.type)

    @TypeConverter
    fun hashMapToString(value: Map<String, String>): String =
        Gson().toJson(value, object : TypeToken<Map<String, String>>() {}.type)

    @TypeConverter
    fun toRates(value: String): Map<String, Double> =
        Gson().fromJson(value, object : TypeToken<Map<String, Double>>() {}.type)

    @TypeConverter
    fun ratesToString(value: Map<String, Double>): String =
        Gson().toJson(value, object : TypeToken<Map<String, Double>>() {}.type)

}