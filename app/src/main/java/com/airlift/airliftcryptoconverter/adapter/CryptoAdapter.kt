package com.airlift.airliftcryptoconverter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airlift.airliftcryptoconverter.R
import com.airlift.airliftcryptoconverter.model.Currency


class CryptoAdapter : RecyclerView.Adapter<CryptoAdapter.CoinViewHolder>() {

        inner class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val txtRank: TextView = itemView.findViewById(R.id.txt_rank)
            val txtSymbol: TextView = itemView.findViewById(R.id.txt_symbol)
            val txtName: TextView = itemView.findViewById(R.id.txt_name)
            val txtPrice: TextView = itemView.findViewById(R.id.txt_price)
        }

        private val differCallback = object: DiffUtil.ItemCallback<Currency>(){
            override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
                return oldItem.symbol == newItem.symbol
            }

            override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
                return  oldItem == newItem
            }

        }

        val differ = AsyncListDiffer(this, differCallback)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
            return CoinViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.crypto_list_item,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
            val coinInfo = differ.currentList[position]

            holder.apply {
                txtName.text = coinInfo.name
                txtPrice.text = coinInfo.price
                txtSymbol.text = coinInfo.symbol
                txtRank.text = coinInfo.rank

            }

        }

        override fun getItemCount(): Int {
            return differ.currentList.size
        }


}