package com.example.mobilebanking

import MyViewModel
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.FirebaseFirestoreKtxRegistrar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.NumberFormat
import java.time.LocalDateTime
import java.util.*

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
        txtBalance.text = getString(R.string.transfer2_current_balance, model.getBalance())
        btnSend.isEnabled = false
        btnOTP.isEnabled = false


        editAmount.editText?.doAfterTextChanged {
            val regex = """\d+""".toRegex()
            if (! regex.matches(editAmount.editText?.text.toString())){
                editAmount.editText?.error = "Invalid amount"
                btnOTP.isEnabled = false
            }
            else {
                btnOTP.isEnabled = true
                editAmount.editText?.error = null
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
            val bundle = Bundle()
            bundle.putBoolean("transfer_success", editOTP.editText?.text.toString() == "000111")
            bundle.putDouble("amount", editAmount.editText?.text.toString().toDouble())
            bundle.putString("account_number", arguments?.getString("recipient_account"))
            val frag = FundTransfer3()
            frag.arguments = bundle

            val transactionDetails = hashMapOf(
                "amount" to editAmount.editText?.text.toString().toDouble(),
                "datetime" to Timestamp.now(),
                "details" to "transfer",
                "receiver_acc" to arguments?.getString("recipient_account"),
                "sender_acc" to model.getAccountNumber()
            )

            db.collection("test-transaction")
                .add(transactionDetails)
                .addOnSuccessListener {
                    Log.d(TAG, transactionDetails.toString())
                }

            var senderBal : Double = 0.0
            var receiverBal : Double = 0.0

            db.collection("users")
                .whereEqualTo("account_number", model.getAccountNumber())
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val users = task.result
                        if (users != null) {
                            for (user in users)
                                senderBal = user["balance"].toString().toDouble()
                        }
                    }
                    else{
                        Log.w(TAG, "task failed")
                    }
                }

            db.collection("users")
                .whereEqualTo("account_number", arguments?.getString("recipient_account"))
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val users = task.result
                        if (users != null) {
                            for (user in users)
                                receiverBal = user["balance"].toString().toDouble()
                        }
                    }
                    else{
                        Log.w(TAG, "task failed")
                    }
                }

            Log.d(TAG, "Sender bal : $senderBal")
            Log.d(TAG, "Receiver bal : $receiverBal")

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