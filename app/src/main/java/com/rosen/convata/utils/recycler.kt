package com.rosen.convata.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rosen.convata.adapters.ConversionsAdapter
import com.rosen.convata.data.models.Conversion

object RecyclerViewBindings {

    @JvmStatic
    @BindingAdapter("conversions")
    fun setConversions(recyclerView: RecyclerView, currencyConversions: List<Conversion>?) {
        val adapter: ConversionsAdapter? = recyclerView.adapter as ConversionsAdapter?
        if (adapter != null && currencyConversions != null) {
            adapter.clearItems()
            adapter.addCurrencyConversions(currencyConversions)
            adapter.notifyDataSetChanged()
        }
    }

}