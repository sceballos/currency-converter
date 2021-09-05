package com.ryokenlabs.currencyconverter.ui.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ryokenlabs.currencyconverter.R

class RatesAdapter(
    private val data: List<Pair<String, Double>>,
    private val context: Context?,
    val clickListener: (String) -> Unit
) : RecyclerView.Adapter<RatesAdapter.RatesViewHolder>() {
    var conversionRate = 1.0
    var conversionAmount = 1.0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RatesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.currency_rate_item_view,
            parent, false
        )
        return RatesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        val rate = data[position]
        holder.code.text = rate.first.replaceFirst("USD", "")
        holder.name.text = "%.2f".format((rate.second / conversionRate) * conversionAmount)
        holder.itemView.setOnClickListener { clickListener(rate.first) }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class RatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val code : TextView = itemView.findViewById(R.id.rates_currency_code)
        val name : TextView = itemView.findViewById(R.id.currency_rate_value)
    }
}