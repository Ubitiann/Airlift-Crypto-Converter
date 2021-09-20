package com.airlift.airliftcryptoconverter.remote

import com.airlift.airliftcryptoconverter.model.CurrencyDataResponse
import com.airlift.airliftcryptoconverter.utils.Constants.Companion.PAGE_LIMIT
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {

    //for getting currency list
    @GET("tickers/")
     fun getCurrencyList() : Call<CurrencyDataResponse>
}