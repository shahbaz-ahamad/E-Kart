package com.example.e_kart.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_kart.databinding.BillingProductRvItemBinding
import com.example.e_kart.datamodel.CartProduct

class BillingProductAdapter : RecyclerView.Adapter<BillingProductAdapter.viewholder>() {


    class viewholder (val binding:BillingProductRvItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(biilingProduct: CartProduct) {

            binding.apply {
                Glide
                    .with(itemView)
                    .load(biilingProduct.product.images[0])
                    .into(imageCartProduct)

                tvProductCartName.text=biilingProduct.product.name
                tvBillingProductQuantity.text = biilingProduct.quantity.toString()


                if(biilingProduct.product.offerPercentage != null) {
                    biilingProduct.product.offerPercentage?.let {
                        val priceBeforeDiscount = biilingProduct.product.price
                        val priceAfterDiscount =
                            priceBeforeDiscount - (priceBeforeDiscount * biilingProduct.product.offerPercentage / 100)
                        tvProductCartPrice.text = "Rs:${String.format("%.2f", priceAfterDiscount)}"
                    }

                }else{
                    tvProductCartPrice.text ="Rs:${biilingProduct.product.price}"
                }
                    imageCartProductColor.setImageDrawable(ColorDrawable(biilingProduct.selectedColor?: Color.TRANSPARENT))
                    tvCartProductSize.text = biilingProduct.selectedSize?:"".also { imageCartProductSize.setImageDrawable(
                    ColorDrawable(Color.TRANSPARENT)
                ) }
            }
        }

    }


    private val diffUtil = object : DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {

            return  oldItem.product == newItem.product
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return  oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffUtil)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(BillingProductRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val biilingProduct = differ.currentList[position]
        holder.bind(biilingProduct)
    }
}