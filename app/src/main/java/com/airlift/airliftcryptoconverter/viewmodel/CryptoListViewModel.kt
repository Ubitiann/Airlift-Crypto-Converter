package com.airlift.airliftcryptoconverter.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.airlift.airliftcryptoconverter.AirliftCryptoConverterApplication
import com.airlift.airliftcryptoconverter.model.CurrencyDataResponse
import com.airlift.airliftcryptoconverter.repository.CryptoRepository
import com.airlift.airliftcryptoconverter.utils.Constants
import com.airlift.airliftcryptoconverter.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class CryptoListViewModel(
    app: Application,
    private val cryptoRepository: CryptoRepository
) : AndroidViewModel(app) {
    //declaration
    val currencyList: MutableLiveData<Resource<CurrencyDataResponse>> = MutableLiveData()
    private var currencyDataResponse: CurrencyDataResponse? = null

    //getting currency list from rest API
    init {
        getCurrencyList()
    }

    //getting currency list
    private fun getCurrencyList() {
        currencyList.value = Resource.Loading()
        try {
            if (isInternetConnected()) {
                val call = cryptoRepository.getCurrencyList()
                call.enqueue(object : Callback<CurrencyDataResponse> {
                    override fun onResponse(
                        call: Call<CurrencyDataResponse>,
                        response: Response<CurrencyDataResponse>
                    ) {
                        currencyList.value = handleCurrencyResponse(response)//handling response
                    }

                    override fun onFailure(call: Call<CurrencyDataResponse>, t: Throwable) {
                        when (t) {
                            is IOException -> currencyList.value =
                                Resource.Error("No internet Connection")
                            else -> currencyList.value =
                                Resource.Error("Conversion Error")//handling no response/error
                        }
                    }
                })
            } else {//handling no response/error
                currencyList.value = Resource.Error("No internet Connection")
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> currencyList.value = Resource.Error("No internet Connection")
                else -> currencyList.value = Resource.Error("Conversion Error")
            }
        }
    }

    //handling response from rest API
    private fun handleCurrencyResponse(response: Response<CurrencyDataResponse>): Resource<CurrencyDataResponse> {
        if (response.isSuccessful) {
            response.body()?.let { currencyResponse ->
                // set initial response
                if (currencyDataResponse == null)
                    currencyDataResponse = currencyResponse
                else {
                    val newCurrency = currencyResponse.data
                    val oldCurrency = currencyDataResponse?.data
                    oldCurrency?.addAll(newCurrency)
                }
                return Resource.Success(currencyDataResponse ?: currencyResponse)

            }
        }
        return Resource.Error(response.message())
    }

    //checking internet is connected or not
    private fun isInternetConnected(): Boolean {
        val connectivityManager: ConnectivityManager =
            getApplication<AirliftCryptoConverterApplication>()
                .getSystemService(
                    Context.CONNECTIVITY_SERVICE
                ) as ConnectivityManager

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> return true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> return true
                else -> return false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> return true
                    TYPE_MOBILE -> return true
                    TYPE_ETHERNET -> return true
                    else -> return false
                }
            }
            return false
        }
    }
}