package com.example.mobilebanking.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.firebase.Timestamp

@Parcelize
data class Transaction(
    val amount : Double = 0.0,
    val datetime : Timestamp = Timestamp(0, 0),
    val details : String = "",
    val receiver_acc : String = "",
    val sender_acc : String = ""
) : Parcelable