package com.example.e_kart.adapter

import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.e_kart.R
import com.example.e_kart.databinding.AddressRvItemBinding
import com.example.e_kart.datamodel.Address

class AddressAdapter : RecyclerView.Adapter<AddressAdapter.Viewholder>() {


    var selectedAddress = -1

    class Viewholder(val binding : AddressRvItemBinding ) :RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address, isSeleceted: Boolean) {

            binding.apply {
                buttonAddress.text = address.addressTitle

                if(isSeleceted){
                    buttonAddress.background=ColorDrawable(itemView.context.resources.getColor(R.color.g_blue))
                }else{
                    buttonAddress.background=ColorDrawable(itemView.context.resources.getColor(R.color.g_white))
                }
            }

        }

    }

        private val diffUtil = object : DiffUtil.ItemCallback<Address>(){
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.addressTitle == newItem.addressTitle
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return  oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        return Viewholder(AddressRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val address = differ.currentList[position]
        Log.d("add",address.toString())
        holder.bind(address,selectedAddress == position)

        holder.binding.buttonAddress.setOnClickListener {
            if(selectedAddress >= 0){
                notifyItemChanged(selectedAddress)
            }
            selectedAddress= holder.adapterPosition
            notifyItemChanged(selectedAddress)
            onclick?.invoke(address)
        }
    }

    var onclick : ((Address) -> Unit)? = null
}