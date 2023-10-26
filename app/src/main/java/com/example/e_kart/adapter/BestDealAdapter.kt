package com.example.e_kart.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.bumptech.glide.Glide
import com.example.e_kart.databinding.BestDealsRvItemBinding
import com.example.e_kart.datamodel.Product

class BestDealAdapter :RecyclerView.Adapter<BestDealAdapter.ViewHolder>() {


    class ViewHolder(val binding:BestDealsRvItemBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product){
            Glide
                .with(itemView)
                .load(product.images[0])
                .into(binding.imgBestDeal)


            binding.apply {
                product.offerPercentage?.let {
                    val priceBeforeDiscount = product.price
                    val priceAfterDiscount= priceBeforeDiscount - (priceBeforeDiscount * product.offerPercentage/100)
                    tvNewPRice.text ="Rs:${String.format("%.2f",priceAfterDiscount)}"
                    tvOldPrice.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG
                }

                if(product.offerPercentage== null){
                    tvNewPRice.visibility= View.GONE
                }

                tvOldPrice.text=product.price.toString()
                tvDealProductName.text=product.name
            }
        }
    }


    private  val diffCallback =object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(BestDealsRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    var onClick : ((Product) -> Unit)? = null
}