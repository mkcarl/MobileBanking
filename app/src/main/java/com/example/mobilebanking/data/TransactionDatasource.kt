package com.example.mobilebanking.data

import com.example.mobilebanking.entities.Transaction
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "TransactionDatasource"

/*
* NOT IN USE
* */
class TransactionDatasource {

//    fun loadTransactions() : List<com.example.mobilebanking.entities.Transaction>{
//        return listOf<com.example.mobilebanking.entities.Transaction>(
//            com.example.mobilebanking.entities.Transaction("12/12/2021", "08:43", 500.4),
//            com.example.mobilebanking.entities.Transaction("13/12/2021", "07:22", 1324.0),
//            com.example.mobilebanking.entities.Transaction("14/12/2021", "09:30", 556.4),
//            com.example.mobilebanking.entities.Transaction("15/12/2021", "12:43", 34.4)
//        )
//    }

    fun loadTransactionsFromFirestore(accNum : String) : List<Transaction>{
        val db = Firebase.firestore
        val ls = mutableListOf<Transaction>()


        return ls
    }
}