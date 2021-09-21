package com.airlift.airliftcryptoconverter.remote

import com.airlift.airliftcryptoconverter.model.CurrencyDataResponse
import retrofit2.Call
import retrofit2.http.GET

interface CurrencyApiService {

    //for getting currency list
    @GET("tickers/")
     fun getCurrencyList() : Call<CurrencyDataResponse>
}