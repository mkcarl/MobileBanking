package com.example.mobilebanking

import Transaction
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilebanking.adapter.TransactionAdapter
import com.example.mobilebanking.data.TransactionDatasource
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG = "TransactionHistoryPage"

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionHistoryPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionHistoryPage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val db = Firebase.firestore
    private lateinit var username : String
    private lateinit var accNum : String
    private lateinit var labelAccNum : TextView

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
        val myView = inflater.inflate(R.layout.fragment_transaction_history_page, container, false)
        username = arguments?.getString("username").toString()
        labelAccNum = myView.findViewById(R.id.text_historyAccNum)


        db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { users ->
                for (user in users) {
                    accNum = user.data["account_number"] as String
                    labelAccNum.text = getString(R.string.history_account_number, accNum)
                    val recyclerTransaction: RecyclerView =
                        myView.findViewById(R.id.recycle_historyHistory)
                }
            }
            .addOnFailureListener{exception->
                Log.w(TAG, "Error getting document", exception)
            }.addOnCompleteListener{_->

                val ls = mutableListOf<Transaction>()
                // receiving
                db.collection("transactions")
                    .whereEqualTo("receiver_acc", accNum)
                    .get()
                    .addOnSuccessListener() { transactions ->
                        for (transaction in transactions) {
                            ls.add(
                                Transaction(
                                    receiver = accNum,
                                    sender = transaction.data["sender_acc"] as String,
                                    datetime = transaction.data["datetime"] as Timestamp,
                                    amount = (transaction.data["amount"] as Long).toDouble(),
                                    details = transaction.data["details"] as String
                                )
                            )
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting document", exception)
                    }
                    .addOnCompleteListener { _ ->
                        db.collection("transactions")
                            .whereEqualTo("sender_acc", accNum)
                            .get()
                            .addOnSuccessListener { transactions ->
                                for (transaction in transactions) {
                                    ls.add(
                                        Transaction(
                                            sender = accNum,
                                            receiver = transaction.data["receiver_acc"] as String,
                                            datetime = transaction.data["datetime"] as Timestamp,
                                            amount = 0 - (transaction.data["amount"] as Long).toDouble(),
                                            details = transaction.data["details"] as String
                                        )
                                    )
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.w(TAG, "Error getting document", exception)
                            }
                            .addOnCompleteListener{ _ ->
                                ls.sortBy { it.datetime }
                                val recyclerTransaction = myView.findViewById<RecyclerView>(R.id.recycle_historyHistory)
                                recyclerTransaction.adapter = TransactionAdapter(ls)
                            }
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
         * @return A new instance of fragment TransactionHistoryPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransactionHistoryPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}