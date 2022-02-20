package com.example.mobilebanking.entities

import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.LocalTime

data class Transaction(
    val amount : Double = 0.0,
    val datetime : Timestamp = Timestamp(0, 0),
    val details : String = "",
    val receiver_acc : String = "",
    val sender_acc : String = ""
) {
}