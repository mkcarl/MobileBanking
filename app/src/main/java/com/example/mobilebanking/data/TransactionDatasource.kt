package com.example.mobilebanking.data

import Transaction

class TransactionDatasource {

    fun loadTransactions() : List<Transaction>{
        return listOf<Transaction>(
            Transaction("12/12/2021", "08:43", 500.4),
            Transaction("13/12/2021", "07:22", 1324.0),
            Transaction("14/12/2021", "09:30", 556.4),
            Transaction("15/12/2021", "12:43", 34.4)
        )
    }
}