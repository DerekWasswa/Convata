package com.rosen.convata.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rosen.convata.data.models.Conversion
import com.rosen.convata.data.models.Currency
import com.rosen.convata.data.models.Rates
import com.rosen.convata.data.repository.ConversionRepository
import com.rosen.convata.utils.Resource
import com.rosen.convata.utils.compactDecimalFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConversionViewModel(
    private val conversionRepository: ConversionRepository
    ) : ViewModel() {

    private val _supportedCurrencies = MutableLiveData<Resource<Currency>>()
    val supportedCurrencies : LiveData<Resource<Currency>>
        get() = _supportedCurrencies

    fun fetchSupportedCurrencies() {
        viewModelScope.launch(Dispatchers.IO) {
            _supportedCurrencies.run {
                postValue(conversionRepository.fetchCurrencies())
            }
        }
    }

    private val _latestRates = MutableLiveData<Resource<Rates>>()
    val latestRates : LiveData<Resource<Rates>>
        get() = _latestRates

    fun fetchLatestRates(base: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _latestRates.run {
                val options: MutableMap<String, Any> = mutableMapOf()
                options["base"] = base
                postValue(conversionRepository.fetchRates(options))
            }
        }
    }

    fun computeConversions(rates: Rates, baseAmount: String): List<Conversion> {
        val conversions : MutableList<Conversion> = mutableListOf()

        for (rate in rates.rates) {
            val amount = baseAmount.toDouble()
            val baseCurrencyRate = rate.value
            val conversion = amount * baseCurrencyRate

            conversions.add(Conversion(rate.key, compactDecimalFormat(conversion), String.format("%.1f", conversion)))
        }

        return conversions
    }

}