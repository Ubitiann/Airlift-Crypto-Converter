package com.airlift.airliftcryptoconverter.view.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airlift.airliftcryptoconverter.R
import com.airlift.airliftcryptoconverter.adapter.CryptoAdapter
import com.airlift.airliftcryptoconverter.model.Currency
import com.airlift.airliftcryptoconverter.repository.CryptoRepository
import com.airlift.airliftcryptoconverter.utils.Resource
import com.airlift.airliftcryptoconverter.viewmodel.CryptoListViewModel
import com.airlift.airliftcryptoconverter.viewmodel.CryptoListViewModelFactory

class CryptoListFragment : Fragment() {

    //declaration
    private lateinit var repository: CryptoRepository
    private lateinit var viewModel: CryptoListViewModel
    private lateinit var cryptoAdapter: CryptoAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnConvertCurreny: Button
    private lateinit var currencyList: List<Currency>
    private val TAG = "CryptoListFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.crypto_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //handling view model
        repository = CryptoRepository()
        val cryptoListViewModelFactory =
            CryptoListViewModelFactory(requireActivity().application, repository)
        viewModel =
            ViewModelProvider(this, cryptoListViewModelFactory).get(CryptoListViewModel::class.java)

        //setting up recyclerview
        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        btnConvertCurreny = view.findViewById(R.id.btnConvertCurrency)
        setUpRecyclerView()

        //observing view model
        viewModel.currencyList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        cryptoAdapter.differ.submitList(it.data.toList())
                        currencyList = it.data.toList();
                        btnConvertCurreny.setEnabled(true)
                    }
                }
                is Resource.Error -> {
                    btnConvertCurreny.setEnabled(false)
                    hideProgressBar()
                    Toast.makeText(
                        context,
                        activity?.resources?.getString(R.string.checkInternetConnection),
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d(TAG, "Error has Occured ${response.message}")
                }
                is Resource.Loading -> showProgressBar()
            }
        })

        btnConvertCurreny.setOnClickListener {
            var bundle = bundleOf("currencyList" to currencyList)
            val navController = findNavController()
            navController.navigate(R.id.cryptoConvertFragment, bundle)
        }
    }

    //progress bar hiding logic
    private fun hideProgressBar() {
        progressBar.setVisibility(View.GONE)
    }

    //progress bar showing logic
    private fun showProgressBar() {
        progressBar.setVisibility(View.VISIBLE)
    }

    //method for setting up recyclerview
    private fun setUpRecyclerView() {
        cryptoAdapter = CryptoAdapter()
        recyclerView.apply {
            adapter = cryptoAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}