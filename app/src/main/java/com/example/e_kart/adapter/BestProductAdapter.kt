package com.example.e_kart.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_kart.databinding.ProductRvItemBinding
import com.example.e_kart.datamodel.Product

class BestProductAdapter :RecyclerView.Adapter<BestProductAdapter.ViewHolder>() {
    class ViewHolder(val binding:ProductRvItemBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(product: Product){
            binding.apply {
                Glide
                    .with(itemView)
                    .load(product.images[0])
                    .into(productImage)

                tvTitle.text=product.name

                product.offerPercentage?.let {
                    val priceBeforeDiscount = product.price
                    val priceAfterDiscount= priceBeforeDiscount - (priceBeforeDiscount * product.offerPercentage/100)
                    newPrice.text ="Rs:${String.format("%.2f",priceAfterDiscount)}"
                    oldPrice.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG
                }
                if(product.offerPercentage== null){
                    newPrice.visibility=View.GONE
                }


                oldPrice.text="Rs:${product.price}"
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
        return ViewHolder(ProductRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product=differ.currentList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    var onClick : ((Product) -> Unit)? = null
}