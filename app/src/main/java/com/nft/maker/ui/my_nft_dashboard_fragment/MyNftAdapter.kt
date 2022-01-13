package com.nft.maker.ui.my_nft_dashboard_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nft.maker.R
import com.bumptech.glide.Glide
import com.nft.maker.model.Data

class MyNftAdapter(val context:Context,var clickCallback: (Data) -> Unit = {}):
    ListAdapter<Data, MyNftAdapter.StockViewHolder>(DiffCallback()){

    class DiffCallback : DiffUtil.ItemCallback<Data>() {

        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        return StockViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_layout_my_nft_dashboard, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {

        holder.bind(getItem(position))
    }

    inner class StockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAvatar: ImageView = itemView.findViewById(R.id.item_image)
        val txtName: TextView = itemView.findViewById(R.id.text1)
        val date: TextView = itemView.findViewById(R.id.text3)


        fun bind(product: Data){

            txtName.text = product.name
            date.text = product.created_at
//        holder.callType.text = currentItem.callSource
//        holder.callStatus.text = currentItem.callStatus
            Glide.with(context)
                .load(product.image)
                .into(imgAvatar)
            itemView.setOnClickListener {
                clickCallback.invoke(product)
            }
        }

    }
}