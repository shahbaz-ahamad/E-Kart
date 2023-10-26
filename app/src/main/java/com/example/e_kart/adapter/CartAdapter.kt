package com.example.e_kart.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_kart.databinding.CartProductItemBinding
import com.example.e_kart.datamodel.CartProduct

class CartAdapter  : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    class ViewHolder(val binding: CartProductItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(cartProduct: CartProduct){
            binding.apply {

                    Glide
                        .with(itemView)
                        .load(cartProduct.product.images[0])
                        .into(imageCart)


                productName.text=cartProduct.product.name
                productQuantity.text=cartProduct.quantity.toString()

                if(cartProduct.product.offerPercentage != null) {
                    cartProduct.product.offerPercentage?.let {
                        val priceBeforeDiscount = cartProduct.product.price
                        val priceAfterDiscount =
                            priceBeforeDiscount - (priceBeforeDiscount * cartProduct.product.offerPercentage / 100)
                        productPrice.text = "Rs:${String.format("%.2f", priceAfterDiscount)}"
                    }

                }else{
                    productPrice.text ="Rs:${cartProduct.product.price}"
                }


                productColor.setImageDrawable(ColorDrawable(cartProduct.selectedColor?: Color.TRANSPARENT))
                 productsizeText.text = cartProduct.selectedSize?:"".also { productSizebackground.setImageDrawable(ColorDrawable(Color.TRANSPARENT)) }

            }

        }
    }


    private val diffCallback = object : DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CartProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartProduct = differ.currentList[position]
        Log.d("Data", cartProduct.toString())
        holder.bind(cartProduct)
        holder.itemView.setOnClickListener {
            onProductclick?.invoke(cartProduct)
        }

        holder.binding.plus.setOnClickListener {
            onPlusclick?.invoke(cartProduct)
        }

        holder.binding.minus.setOnClickListener {
            onMinusclick?.invoke(cartProduct)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick?.invoke(cartProduct)
            true
        }
    }

    var onProductclick : ((CartProduct) -> Unit)? = null
    var onPlusclick : ((CartProduct) -> Unit)? = null
    var onMinusclick : ((CartProduct) -> Unit)? = null
    var onLongClick :((CartProduct) -> Unit)? = null
}
