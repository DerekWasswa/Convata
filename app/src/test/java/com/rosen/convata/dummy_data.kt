package com.rosen.convata

import com.rosen.convata.data.models.Currency
import com.rosen.convata.data.models.Rates
import com.rosen.convata.data.models.Symbol

val dummyCurrencies = Currency(currencies = mapOf(
    "AED" to Symbol(
        description = "United Arab Emirates Dirham",
        code = "AED"
    ),
    "AFN" to Symbol(
        description = "Afghan Afghani",
        code = "AFN"
    ))
)

val dummyRates = Rates(
    date = "now",
    base = "USD",
    rates = mapOf(
        "AED" to 3.672538,
        "AFN" to 66.879999
    )
)