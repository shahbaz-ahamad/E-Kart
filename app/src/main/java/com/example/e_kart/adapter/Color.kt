package com.example.e_kart.adapter

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.e_kart.databinding.ColorRvItemBinding

class Color : RecyclerView.Adapter<Color.ViewHolder>() {

      var selectedPosition = -1
    inner class ViewHolder(val binding: ColorRvItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(color: Int, position: Int) {


            val imageDrawable = ColorDrawable(color)
            binding.imageColor.setImageDrawable(imageDrawable)
            if(position == selectedPosition){ //color is selected
                binding.apply {
                    imageShadow.visibility= View.VISIBLE
                    imageColor.visibility=View.VISIBLE
                    icon.visibility=View.VISIBLE
                }
            }
            else{//color is not selectef
                binding.apply {
                    imageShadow.visibility= View.INVISIBLE
                    imageColor.visibility=View.VISIBLE
                    icon.visibility=View.INVISIBLE
                }
            }
        }


    }

    private val diffcallBack = object : DiffUtil.ItemCallback<Int>(){
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return  oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return  oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffcallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ColorRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val color = differ.currentList[position]
        holder.bind(color,position)

        holder.itemView.setOnClickListener {
            if(selectedPosition >= 0){
                notifyItemChanged(selectedPosition)
            }

            selectedPosition=holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(color)
        }
    }

    var onItemClick : ((Int) -> Unit)? = null


}