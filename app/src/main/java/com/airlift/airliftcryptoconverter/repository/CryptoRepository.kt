package com.airlift.airliftcryptoconverter.repository

import com.airlift.airliftcryptoconverter.remote.RetrofitClient

class CryptoRepository {
    // get all currencies
    fun getCurrencyList() =
        RetrofitClient.api.getCurrencyList()
}