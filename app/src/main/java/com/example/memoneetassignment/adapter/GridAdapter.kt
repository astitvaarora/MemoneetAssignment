package com.example.memoneetassignment.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memoneetassignment.databinding.PhotolayoutBinding

class GridAdapter(private val ls : List<Bitmap>):RecyclerView.Adapter<GridAdapter.ViewHolder>(){
    class ViewHolder(val binding:PhotolayoutBinding):RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        PhotolayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    )


    override fun getItemCount(): Int {
        return ls.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = ls[position]
        holder.binding.apply {
            imageView.setImageBitmap(item)
        }
    }


}