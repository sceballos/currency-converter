package com.ryokenlabs.currencyconverter.ui.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ryokenlabs.currencyconverter.R

class CurrenciesAdapter(
    private val data: List<Pair<String, String>>,
    private val context: Context?,
    val clickListener: (String) -> Unit
) : RecyclerView.Adapter<CurrenciesAdapter.CurrenciesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrenciesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.currency_item_view,
            parent, false
        )
        return CurrenciesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CurrenciesViewHolder, position: Int) {
        val currency = data[position]
        holder.code.text = currency.first
        holder.name.text = currency.second
        holder.itemView.setOnClickListener { clickListener(currency.first) }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class CurrenciesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val code : TextView = itemView.findViewById(R.id.currency_code_tv)
        val name : TextView = itemView.findViewById(R.id.currency_name_tv)
    }
}