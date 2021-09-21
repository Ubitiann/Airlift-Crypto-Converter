package com.airlift.airliftcryptoconverter.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.airlift.airliftcryptoconverter.R

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 3 seconds delay for splash screen
        Handler(Looper.getMainLooper()).postDelayed(
            Runnable {
                try {
                    requireView().findNavController()
                        .navigate(R.id.action_splashFragment_to_currencyListFragment)
                } catch (e: Exception) {
                }
            }, 3000
        )
    }
}