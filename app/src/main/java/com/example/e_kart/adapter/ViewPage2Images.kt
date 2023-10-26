package com.example.e_kart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_kart.databinding.ViewpagerImageItemBinding

class ViewPage2Images :RecyclerView.Adapter<ViewPage2Images.ViewHolder>() {

    class ViewHolder(val binding: ViewpagerImageItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(imagePath: String) {

            Glide
                .with(itemView)
                .load(imagePath)
                .into(binding.imageView)
        }
    }

    private  val diffCallBack = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {

            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ViewpagerImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.bind(image)
    }

}