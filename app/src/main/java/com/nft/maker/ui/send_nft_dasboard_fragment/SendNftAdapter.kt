package com.nft.maker.ui.send_nft_dasboard_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nft.maker.R
import com.nft.maker.model.send_nft_mode_dashboard.Data

class SendNftAdapter(val context: Context, var clickCallback: (Data) -> Unit = {}) :
    ListAdapter<Data, SendNftAdapter.StockViewHolder>(DiffCallback()) {

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
                .inflate(R.layout.custom_layout_nft_sent_dasboard, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {

        holder.bind(getItem(position))
    }

    inner class StockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAvatar: ImageView = itemView.findViewById(R.id.image0)
        val txtName: TextView = itemView.findViewById(R.id.title)
        val date: TextView = itemView.findViewById(R.id.date_text)
        val description_text: TextView = itemView.findViewById(R.id.description_text)
        val category: TextView = itemView.findViewById(R.id.category)


        fun bind(product: Data) {
            txtName.text = product.name
            date.text = product.sender.created_at
            description_text.text = "Send to " + product.receiver?.email

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