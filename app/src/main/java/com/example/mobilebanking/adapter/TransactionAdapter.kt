package com.example.mobilebanking.adapter

import Transaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilebanking.R

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
        holder.date.text = item.date
        holder.time.text = item.time
        holder.amount.text = item.amount.toString()
    }

    override fun getItemCount(): Int {
        return dataset.size;
    }
}