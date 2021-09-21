package com.airlift.airliftcryptoconverter.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.airlift.airliftcryptoconverter.R
import com.airlift.airliftcryptoconverter.databinding.ActivityCryptoListBinding

class CryptoListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityCryptoListBinding = DataBindingUtil.setContentView(this, R.layout.activity_crypto_list)
    }
}