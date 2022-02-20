package com.example.mobilebanking.entities

import com.google.firebase.ktx.Firebase

data class User(
    var account_number : String = "",
    var balance : Double = 0.0,
    var bank_name : String = "",
    var uid : String = "",
    var username : String = ""
)
