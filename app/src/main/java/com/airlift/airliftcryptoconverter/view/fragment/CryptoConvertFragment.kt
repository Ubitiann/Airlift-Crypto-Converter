package com.airlift.airliftcryptoconverter.view.fragment

import android.icu.number.IntegerWidth
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.airlift.airliftcryptoconverter.R
import com.airlift.airliftcryptoconverter.model.Currency
import com.airlift.airliftcryptoconverter.repository.CryptoRepository
import com.airlift.airliftcryptoconverter.utils.Resource
import com.airlift.airliftcryptoconverter.viewmodel.CryptoListViewModel
import com.airlift.airliftcryptoconverter.viewmodel.CryptoListViewModelFactory
import retrofit2.Response.error
import java.lang.Exception
import java.lang.NumberFormatException

/**
 * A simple [Fragment] subclass.
 * Use the [CryptoConvertFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CryptoConvertFragment : Fragment() {

    //declaration
    private lateinit var fromSpinner: Spinner
    private lateinit var toSpinner: Spinner
    private lateinit var fromCurrencyEditText: EditText
    private lateinit var toCurrencyEditText: EditText
    private lateinit var repository: CryptoRepository
    private lateinit var viewModel: CryptoListViewModel
    private lateinit var currencyList: List<Currency>
    private lateinit var btnConvert: Button
    private lateinit var fromCurrency: String
    private lateinit var toCurrency: String
    private lateinit var navController: NavController
    private lateinit var tempList: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crypto_convert, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initialize to and from currency
        fromCurrency = "BTC"
        toCurrency = "BTC"

        //setting nav controller
        navController = findNavController()

        //initializing spinner
        fromSpinner = view.findViewById(R.id.fromDropdown)
        toSpinner = view.findViewById(R.id.toDropdown)
        btnConvert = view.findViewById(R.id.btnConvert)
        fromCurrencyEditText = view.findViewById(R.id.fromCurrency)
        toCurrencyEditText = view.findViewById(R.id.toCurrency)

        //handling view model
        repository = CryptoRepository()
        val cryptoListViewModelFactory =
            CryptoListViewModelFactory(requireActivity().application, repository)
        viewModel =
            ViewModelProvider(this, cryptoListViewModelFactory).get(CryptoListViewModel::class.java)

        //observing view model
        viewModel.currencyList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        currencyList = it.data.toList();
                        //setting spinner for currencies
                        settingCurrencySpinner();

                        //setting button convert click listener
                        if (currencyList != null) {
                            if (currencyList.size > 0) {
                                btnConvert.setOnClickListener {
                                    convertCurrency()
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(
                        context,
                        activity?.resources?.getString(R.string.errorMessage),
                        Toast.LENGTH_LONG
                    ).show()
                    navController.popBackStack()
                }
            }
        })
    }

    //method for converting currency
    private fun convertCurrency() {
        try {
            var fromRate: String = "0"
            var toRate: String = "0"
            for (i in 0 until currencyList.size) {
                if (fromCurrency.equals(currencyList[i].symbol.toString())) {
                    fromRate = currencyList[i].price.toString()
                    break
                }
            }
            for (i in 0 until currencyList.size) {
                if (toCurrency.equals(currencyList[i].symbol)) {
                    toRate = currencyList[i].price.toString()
                    break
                }
            }

            var result =
                Integer.parseInt(fromCurrencyEditText.text.toString()) * Integer.parseInt(fromRate.toString()) / Integer.parseInt(
                    toRate
                )
            toCurrencyEditText.setText(result.toString())
        } catch (e: NumberFormatException) {
            fromCurrencyEditText.setText("0")
            toCurrencyEditText.setText("0")
        }
    }

    private fun settingCurrencySpinner() {
        tempList = listOf<String>().toMutableList()

        for (i in currencyList.indices) {
            tempList.add(currencyList[i].symbol.toString())
        }

        val arrayAdapter = ArrayAdapter(
            activity?.applicationContext!!,
            android.R.layout.simple_spinner_item,
            tempList
        )

        fromSpinner.adapter = arrayAdapter
        fromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                fromCurrency = tempList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

        toSpinner.adapter = arrayAdapter
        toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                toCurrency = tempList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

    }
}