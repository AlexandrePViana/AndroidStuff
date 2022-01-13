package com.example.androidprojectocrprice

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.example.androidprojectocrprice.Results

class MyAdapter(private val dataList: MutableList<Results>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>()  {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val currentItem = dataList[position]
        Picasso.get()
            .load(currentItem.imageUrl)
            .into(holder.TitleImage)
        holder.NomeProd.text = currentItem.title
        holder.PriceProd.text = currentItem.price

    }

    override fun getItemCount(): Int
    {
        return dataList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        val TitleImage : ImageView = itemView.findViewById(R.id.imageView2)
        val NomeProd : TextView = itemView.findViewById(R.id.textView2)
        val PriceProd : TextView = itemView.findViewById(R.id.textView3)
    }
}