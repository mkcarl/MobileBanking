package com.example.mobilebanking.data

import Transaction
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "TransactionDatasource"

class TransactionDatasource {

//    fun loadTransactions() : List<Transaction>{
//        return listOf<Transaction>(
//            Transaction("12/12/2021", "08:43", 500.4),
//            Transaction("13/12/2021", "07:22", 1324.0),
//            Transaction("14/12/2021", "09:30", 556.4),
//            Transaction("15/12/2021", "12:43", 34.4)
//        )
//    }

    fun loadTransactionsFromFirestore(accNum : String) : List<Transaction>{
        val db = Firebase.firestore
        val ls = mutableListOf<Transaction>()

        // receiving
        db.collection("transactions")
            .whereEqualTo("receiver_acc", accNum)
            .get()
            .addOnSuccessListener() { transactions ->
                for (transaction in transactions){
                    ls.add(Transaction(
                        receiver = accNum,
                        sender = transaction.data["sender_acc"] as String,
                        datetime = transaction.data["datetime"] as Timestamp,
                        amount = (transaction.data["amount"] as Long).toDouble(),
                        details =  transaction.data["details"] as String
                    ))
                }
            }
            .addOnFailureListener{exception->
                Log.w(TAG, "Error getting document", exception)
            }
            .addOnCompleteListener{ task ->
                println(ls)
            }

//        // sending
//        db.collection("transactions")
//            .whereEqualTo("receiver_acc", accNum)
//            .get()
//            .addOnSuccessListener { transactions ->
//                for (transaction in transactions){
//
//                    ls.add(Transaction(
//                        sender = accNum,
//                        receiver = transaction.data["receiver_acc"] as String,
//                        datetime = transaction.data["datetime"] as Timestamp,
//                        amount = 0 - (transaction.data["amount"] as Long).toDouble(),
//                        details =  transaction.data["details"] as String
//                    ))
//                }
//            }
//            .addOnFailureListener{exception->
//                Log.w(TAG, "Error getting document", exception)
//            }
//
//        ls.sortBy {
//            it.datetime
//        }
//        Log.d(TAG, "List of transaction is $ls")
        return ls
    }
}