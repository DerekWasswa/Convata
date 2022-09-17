package com.rosen.convata.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rosen.convata.data.models.Conversion
import com.rosen.convata.databinding.ConversionCurrencyItemBinding

class ConversionsAdapter(private val conversionSelection: ConversionSelection) :
    RecyclerView.Adapter<BaseViewHolder>() {
    private val currencyConversions: MutableList<Conversion> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding: ConversionCurrencyItemBinding = ConversionCurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = currencyConversions.size

    // Main ViewHolder
    inner class CurrencyViewHolder internal constructor(binding: ConversionCurrencyItemBinding) : BaseViewHolder(binding.root) {
        private val mBinding: ConversionCurrencyItemBinding = binding

        override fun onBind(position: Int) {
            val conversion: Conversion = currencyConversions[position]
            mBinding.conversion = conversion
            mBinding.root.setOnClickListener { conversionSelection.selectedConversion(conversion) }
            mBinding.executePendingBindings()
        }
    }

    fun addCurrencyConversions(currencies: List<Conversion>?) {
        currencyConversions.clear()
        currencyConversions.addAll(currencies!!)
        notifyDataSetChanged()
    }

    fun clearItems() {
        currencyConversions.clear()
    }

}

interface ConversionSelection {
    fun selectedConversion(conversion: Conversion)
}

abstract class BaseViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
    abstract fun onBind(position: Int)
}