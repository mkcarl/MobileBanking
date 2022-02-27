package com.example.mobilebanking.entities

data class User(
    var account_number : String = "",
    var balance : Double = 0.0,
    var bank_name : String = "",
    var uid : String = "",
    var username : String = ""
)
