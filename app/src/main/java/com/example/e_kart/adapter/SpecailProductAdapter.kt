package com.example.e_kart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_kart.databinding.SpecialRvItemBinding
import com.example.e_kart.datamodel.Product

class SpecailProductAdapter :RecyclerView.Adapter<SpecailProductAdapter.ViewHolder>() {
    class ViewHolder(val binding:SpecialRvItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
            binding.apply {
                Glide
                    .with(itemView)
                    .load(product.images[0])
                    .into(imgAd)

                tvAdName.text=product.name
                tvAdPrice.text=product.price.toString()
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SpecialRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onclick?.invoke(product)
        }
    }

    var onclick : ((Product) -> Unit)? = null
}