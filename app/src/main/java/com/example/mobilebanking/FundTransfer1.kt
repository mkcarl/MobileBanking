package com.example.mobilebanking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import com.google.android.material.textfield.TextInputLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FundTransfer1.newInstance] factory method to
 * create an instance of this fragment.
 */
class FundTransfer1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_fund_transfer1, container, false)
        val btnNext: Button = myView.findViewById(R.id.button_transfer1Next)
        val editBanks : TextInputLayout = myView.findViewById(R.id.editText_transfer1BankName)

        btnNext.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.linear_main, FundTransfer2())
                commit()
            }
        }

        val banks = listOf<String>("Maybank", "Ambank", "CIMB Bank", "Bank Simpanan Nasional", "Bank of China", "HSBC Bank", "OCBC Bank")
        val adapter = ArrayAdapter(requireContext(), R.layout.bank_list_item, banks.sorted())
        (editBanks.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        return myView
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FundTransfer1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FundTransfer1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}