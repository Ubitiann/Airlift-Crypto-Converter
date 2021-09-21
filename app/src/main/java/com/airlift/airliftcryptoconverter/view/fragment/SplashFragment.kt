package com.airlift.airliftcryptoconverter.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.airlift.airliftcryptoconverter.R
import com.airlift.airliftcryptoconverter.databinding.CryptoListFragmentBinding
import com.airlift.airliftcryptoconverter.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    //declaration
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false);
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 1.5 seconds delay for splash screen
        Handler(Looper.getMainLooper()).postDelayed(
            Runnable {
                try {
                    requireView().findNavController()
                        .navigate(R.id.action_splashFragment_to_currencyListFragment)
                } catch (e: Exception) {
                }
            }, 1500
        )
    }
}