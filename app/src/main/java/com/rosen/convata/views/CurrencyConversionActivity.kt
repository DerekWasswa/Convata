package com.rosen.convata.views

import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rosen.convata.BuildConfig
import com.rosen.convata.R
import com.rosen.convata.adapters.ConversionSelection
import com.rosen.convata.adapters.ConversionsAdapter
import com.rosen.convata.data.models.Conversion
import com.rosen.convata.data.models.Symbol
import com.rosen.convata.databinding.ActivityCurrencyConversionBinding
import com.rosen.convata.utils.Constants.TAG
import com.rosen.convata.utils.EspressoIdlingResource
import com.rosen.convata.utils.Resource
import com.rosen.convata.utils.txtChanged
import com.rosen.convata.viewmodels.ConversionViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import org.koin.androidx.viewmodel.ext.android.viewModel


@OptIn(FlowPreview::class)
class CurrencyConversionActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCurrencyConversionBinding
    private val conversionViewModel by viewModel<ConversionViewModel>()
    private lateinit var conversionsAdapter: ConversionsAdapter
    private lateinit var supportedCurrenciesAdapterStr: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_currency_conversion)
        binding.showInstruction = true
        observeCurrencies()
        initCurrencyConversions()
        observeConversions()
    }

    private fun observeCurrencies() {
        conversionViewModel.supportedCurrencies.observe(this) { (status, data, error) ->
            when (status) {
                Resource.Status.LOADING -> {}
                Resource.Status.SUCCESS -> {
                    data?.let { mData ->
                        initSupportedCurrencies(mData.currencies)
                    }
                }
                Resource.Status.ERROR -> {
                    error?.let { mError ->
                        Log.d(TAG, mError)
                    }
                }
            }
        }

        conversionViewModel.fetchSupportedCurrencies()
    }

    private fun initSupportedCurrencies(supportedCurrencies: Map<String, Symbol>) {
        supportedCurrencies.keys.toList().let { currencies ->
            if (currencies.isNotEmpty()) {
                supportedCurrenciesAdapterStr = ArrayAdapter(this@CurrencyConversionActivity, R.layout.supported_currency_item, currencies)
                binding.baseCurrencyLabel.setAdapter(supportedCurrenciesAdapterStr)
            }
        }

        (binding.baseCurrencySpinner.editText as AutoCompleteTextView).onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                supportedCurrenciesAdapterStr.getItem(position)?.let { baseCurrency ->
                    loadingConversions(baseCurrency)
                }
            }

        binding.amount.txtChanged()
            .debounce(500)
            .distinctUntilChanged()
            .onEach {
                val baseCurrency = binding.baseCurrencyLabel.text.toString()
                if (baseCurrency.isNotEmpty()) {
                    EspressoIdlingResource.increment()
                    loadingConversions(baseCurrency)
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun loadingConversions(baseCurrency: String) {
        if (binding.amount.text.toString().isNotEmpty()) {
            binding.showInstruction = false
            binding.loadingConversions = true
            conversionViewModel.fetchLatestRates(baseCurrency)
        }
    }

    private fun initCurrencyConversions() {
        conversionsAdapter = ConversionsAdapter(conversionSelection)
        binding.currencyList.apply {
            this.layoutManager = GridLayoutManager(this@CurrencyConversionActivity,3)
        }
        binding.currencyList.adapter = conversionsAdapter
    }

    private val conversionSelection = object: ConversionSelection {
        override fun selectedConversion(conversion: Conversion) {
            val baseCurrency = binding.baseCurrencyLabel.text.toString()
            val amount = binding.amount.text.toString()

            val message = "$amount $baseCurrency converts to ${conversion.currencySymbol} ${conversion.amountWithPrecision}"
            val snack = Snackbar.make(binding.root,message, Snackbar.LENGTH_LONG)
            snack.show()
        }
    }

    private fun observeConversions() {
        conversionViewModel.latestRates.observe(this) { (status, data, error) ->
            when (status) {
                Resource.Status.LOADING -> {}
                Resource.Status.SUCCESS -> {
                    data?.let { mData ->
                        binding.loadingConversions = false
                        val amount = binding.amount.text.toString()
                        conversionsAdapter.addCurrencyConversions(conversionViewModel.computeConversions(mData, amount))
                        EspressoIdlingResource.decrement()
                    }
                }
                Resource.Status.ERROR -> {
                    binding.loadingConversions = false
                    binding.showInstruction = true
                    error?.let { mError ->
                        Log.d(TAG, mError)
                    }
                    EspressoIdlingResource.decrement()
                }
            }
        }
    }
}