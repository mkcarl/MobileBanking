package com.example.mobilebanking

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "FundTransfer1"
/**
 * A simple [Fragment] subclass.
 * Use the [FundTransfer1.newInstance] factory method to
 * create an instance of this fragment.
 */
class FundTransfer1 : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    val db = Firebase.firestore

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
        val editAccount : TextInputLayout = myView.findViewById(R.id.editText_transfer1RecipientAcc)
        val nav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation_mainNav)

        var recipientAcc : String? = null
        var bankName : String? = null

        btnNext.setOnClickListener {

            Log.d(TAG, "bank name :  ${editBanks.editText?.text.toString()}")
            Log.d(TAG, "Acc num :  ${editAccount.editText?.text.toString()}")
            recipientAcc = editAccount.editText?.text.toString()
            bankName = editBanks.editText?.text.toString()

            db.collection("users")
                .whereEqualTo("account_number", editAccount.editText?.text.toString())
                .whereEqualTo("bank_name", editBanks.editText?.text.toString())
                .get()
                .addOnSuccessListener { users ->
                    if (users.size() != 0){
                        Log.d(TAG, "there is user")
                        for (i in 0 until nav?.menu?.size()!!) {
                            nav.menu.getItem(i).isEnabled = false
                            nav.menu.getItem(i).isCheckable = false
                        }

                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.linear_main, FundTransfer2())
                            commit()
                        }
                    }
                    else{
                        Log.d(TAG, "There is none")
                        Toast.makeText(context, "Account does not exist",
                            Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener{exception->
                    Log.w(TAG, "Error getting document", exception)
                }.addOnCompleteListener { _->
                    Log.d(TAG, "Query complete")
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