package com.airlift.airliftcryptoconverter.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.airlift.airliftcryptoconverter.repository.CryptoRepository

class CryptoListViewModelFactory(
   private val app: Application,
   private val repository: CryptoRepository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CryptoListViewModel(app, repository) as T
    }
}