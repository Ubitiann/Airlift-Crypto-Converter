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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airlift.airliftcryptoconverter.R
import com.airlift.airliftcryptoconverter.adapter.CryptoAdapter
import com.airlift.airliftcryptoconverter.databinding.CryptoListFragmentBinding
import com.airlift.airliftcryptoconverter.interfaces.ConvertCurrencyMoveHandler
import com.airlift.airliftcryptoconverter.model.Currency
import com.airlift.airliftcryptoconverter.repository.CryptoRepository
import com.airlift.airliftcryptoconverter.utils.Resource
import com.airlift.airliftcryptoconverter.viewmodel.CryptoListViewModel
import com.airlift.airliftcryptoconverter.viewmodel.CryptoListViewModelFactory

class CryptoListFragment : Fragment(), ConvertCurrencyMoveHandler {

    //declaration
    private lateinit var binding: CryptoListFragmentBinding
    private lateinit var repository: CryptoRepository
    private lateinit var viewModel: CryptoListViewModel
    private lateinit var cryptoAdapter: CryptoAdapter
    private lateinit var currencyList: List<Currency>
    private var isResponseOk: Boolean = false
    private val TAG = "CryptoListFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.crypto_list_fragment, container, false);
        binding.clickListener = this
        return binding.root
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
        setUpRecyclerView()

        //observing view model
        viewModel.currencyList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> { //is success
                    hideProgressBar()
                    response.data?.let {
                        cryptoAdapter.differ.submitList(it.data.toList())
                        currencyList = it.data.toList();
                        binding.btnConvertCurrency.setEnabled(true)
                        isResponseOk = true
                    }
                }
                is Resource.Error -> { //is error
                    binding.btnConvertCurrency.setEnabled(false)
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
    }

    //progress bar hiding logic
    private fun hideProgressBar() {
        binding.progressBar.setVisibility(View.GONE)
    }

    //progress bar showing logic
    private fun showProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE)
    }

    //method for setting up recyclerview
    private fun setUpRecyclerView() {
        cryptoAdapter = CryptoAdapter()
        binding.recyclerView.apply {
            adapter = cryptoAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    // transaction to convert currency fragment
    override fun onFragTransaction(view: View) {
        when (view.id) {
            R.id.btnConvertCurrency -> {
                if (isResponseOk) {
                    var bundle = bundleOf("currencyList" to currencyList)
                    val navController = findNavController()
                    navController.navigate(R.id.cryptoConvertFragment, bundle)
                }
            }
        }
    }
}