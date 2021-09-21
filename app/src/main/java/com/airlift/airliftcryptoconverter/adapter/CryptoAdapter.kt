package com.airlift.airliftcryptoconverter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airlift.airliftcryptoconverter.R
import com.airlift.airliftcryptoconverter.databinding.CryptoListItemBinding
import com.airlift.airliftcryptoconverter.model.Currency


class CryptoAdapter : RecyclerView.Adapter<CryptoAdapter.CoinViewHolder>() {

    inner class CoinViewHolder(val binding: CryptoListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: Currency) {
            with(itemView) {
                binding.txtName.text = currency.name
                binding.txtPrice.text = currency.price
                binding.txtSymbol.text = currency.symbol
                binding.txtRank.text = currency.rank
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val binding =
            CryptoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val currencyInfo = differ.currentList[position]
        holder.bind(currencyInfo)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}