package com.example.mobilebanking.adapter

import Transaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilebanking.R
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class TransactionAdapter(private val dataset : List<Transaction>) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>(){

    class TransactionViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val date : TextView = view.findViewById(R.id.text_historyDate)
        val time : TextView = view.findViewById(R.id.text_historyTime)
        val amount : TextView = view.findViewById(R.id.text_historyAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_history_item, parent, false)
        return TransactionViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = dataset[position]
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
        val timeFormatter = SimpleDateFormat("HH:mm:ss")
        holder.date.text = dateFormatter.format(item.datetime.toDate())
        holder.time.text = timeFormatter.format(item.datetime.toDate())
        holder.amount.text = String.format("RM%.02f", item.amount)
    }

    override fun getItemCount(): Int {
        return dataset.size;
    }
}