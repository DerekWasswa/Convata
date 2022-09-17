package com.rosen.convata.utils

import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

//Compact Decimal Format
//Leverage on 29th June, 2022 at 4:00am
//https://gist.github.com/nprk/999faa0d324e54bd59bae7def933b495
// format currency in abbreviated forms suitable for limited space real estate
fun compactDecimalFormat(number: Number): String {
    val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')

    val numValue = number.toLong()
    val value = floor(log10(numValue.toDouble())).toInt()
    val base = value / 3

    return if (value >= 3 && base < suffix.size) {
        DecimalFormat("#0.0").format(numValue / 10.0.pow((base * 3).toDouble())) + suffix[base]
    } else {
        DecimalFormat("#,##0").format(numValue)
    }
}