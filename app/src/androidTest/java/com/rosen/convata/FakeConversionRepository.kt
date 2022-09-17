package com.rosen.convata

import com.rosen.convata.data.models.Currency
import com.rosen.convata.data.models.Rates
import com.rosen.convata.data.models.Symbol
import com.rosen.convata.data.repository.ConversionRepository
import com.rosen.convata.utils.Resource

class FakeConversionRepository: ConversionRepository {
    override suspend fun fetchCurrencies(): Resource<Currency> {
        return Resource.success(
            Currency(currencies = mapOf(
                "AED" to Symbol(
                    description = "United Arab Emirates Dirham",
                    code = "AED"
                ),
                "AFN" to Symbol(
                    description = "Afghan Afghani",
                    code = "AFN"
                )
            )
            )
        )
    }

    override suspend fun fetchRates(options: Map<String, Any>): Resource<Rates> {
        return Resource.success(
            Rates(
                date = "now",
                base = "USD",
                rates = mapOf(
                    "AED" to 3.672538,
                    "AFN" to 66.879999
                ))
        )
    }

    override suspend fun getLocalCurrencies(): List<Currency> = listOf(
        Currency(currencies = mapOf(
            "AED" to Symbol(
                description = "United Arab Emirates Dirham",
                code = "AED"
            ),
            "AFN" to Symbol(
                description = "Afghan Afghani",
                code = "AFN"
            )
        )
        )
    )

    override suspend fun getLocalRates(base: String): List<Rates> = listOf(
        Rates(
            date = "now",
            base = "USD",
            rates = mapOf(
                "AED" to 3.672538,
                "AFN" to 66.879999
            ))
    )
}