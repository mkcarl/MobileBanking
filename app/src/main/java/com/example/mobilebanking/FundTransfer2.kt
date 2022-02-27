package com.example.mobilebanking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "FundTransfer2"
/**
 * A simple [Fragment] subclass.
 * Use the [FundTransfer2.newInstance] factory method to
 * create an instance of this fragment.
 */
class FundTransfer2 : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    val model : MyViewModel by activityViewModels()
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, model.getReceivedUser().value.toString())
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
        val myView = inflater.inflate(R.layout.fragment_fund_transfer2, container, false)
        val btnSend:Button = myView.findViewById(R.id.button_transfer2Send)
        val btnOTP:Button = myView.findViewById(R.id.button_transfer2RequstOTP)
        val txtTransfer : TextView = myView.findViewById(R.id.text_transfer2TransferringTo)
        val txtBalance : TextView = myView.findViewById(R.id.text_transfer2CurrentBalance)
        val editAmount : TextInputLayout = myView.findViewById(R.id.editText_transfer2Amount)
        val editOTP : TextInputLayout = myView.findViewById(R.id.editText_transfer2OTP)


        txtTransfer.text = getString(R.string.transfer2_transferring_to, arguments?.getString("recipient_account"), arguments?.getString("bank_name"))
        model.getUser().observe(viewLifecycleOwner, Observer {
            txtBalance.text = getString(R.string.transfer2_current_balance, it.balance)
        })

        btnSend.isEnabled = false
        btnOTP.isEnabled = false



        editAmount.editText?.doAfterTextChanged {
            if (!editAmount.editText?.text.isNullOrEmpty()) {
                val sendAmount: Double = editAmount.editText!!.text.toString().toDouble()
                val regex = """^\d+\.?\d{1,2}${'$'}|^\d+${'$'}""".toRegex()
                if (!regex.matches(editAmount.editText?.text.toString()) || sendAmount == 0.0) {
                    editAmount.editText?.error = "Invalid amount"
                    btnOTP.isEnabled = false
                    editOTP.editText!!.isEnabled = false

                } else if (model.getUser().value!!.balance - sendAmount < 0) {
                    btnOTP.isEnabled = false
                    editAmount.editText!!.error = "Insufficient balance!"
                    editOTP.editText!!.isEnabled = false
                } else {
                    btnOTP.isEnabled = true
                    editAmount.editText?.error = null
                    editOTP.editText!!.isEnabled = true
                }
            }

        }

        editOTP.editText?.doAfterTextChanged {
            btnSend.isEnabled = true
        }

        btnOTP.setOnClickListener {
            editAmount.editText?.isEnabled = false
            Toast.makeText(context, "OTP is 000111",
                Toast.LENGTH_LONG).show()
        }

        btnSend.setOnClickListener {
            var isSuccessfulTransfer = editOTP.editText?.text.toString() == "000111"

            val transactionDetails = hashMapOf(
                "amount" to editAmount.editText?.text.toString().toDouble(),
                "datetime" to Timestamp.now(),
                "details" to "transfer",
                "receiver_acc" to arguments?.getString("recipient_account"),
                "sender_acc" to model.getUser().value!!.account_number
            )

            val sendAmount : Double = editAmount.editText?.text.toString().toDouble()
            Log.d(TAG, "send amount : $sendAmount")

            model.getUser().value!!.balance -= sendAmount
            model.getReceivedUser().value!!.balance += sendAmount

            if (isSuccessfulTransfer) {
                model.updateUser()
            }
            val frag = FundTransfer3()
            val bundle = Bundle()
            bundle.putBoolean("transfer_success", isSuccessfulTransfer)
            bundle.putDouble("amount", editAmount.editText?.text.toString().toDouble())
            bundle.putString("account_number", arguments?.getString("recipient_account"))
            frag.arguments = bundle
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.linear_main, frag)
                commit()
            }
        }

        return myView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FundTransfer2.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FundTransfer2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}