package com.example.mobilebanking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilebanking.News
import com.example.mobilebanking.R

class NewsAdapter(private val dataset : List<News>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(){

    class NewsViewHolder(private val view : View) : RecyclerView.ViewHolder(view){
        val newsTitle : TextView = view.findViewById(R.id.text_newsTitle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_list_item, parent, false)
        return NewsViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = dataset[position]
        holder.newsTitle.text = item.title
    }

    override fun getItemCount(): Int {
        return dataset.size;
    }

}