package com.nft.maker.ui.layout_sent_nft_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nft.maker.R
import com.nft.maker.model.Data

class LayoutSendNftAdapter(val context: Context):
    ListAdapter<Data, LayoutSendNftAdapter.StockViewHolder>(DiffCallback()){
    private var checkedPosition = 0

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
        val my_item_nft: ConstraintLayout = itemView.findViewById(R.id.my_item_nft)
        val txtName: TextView = itemView.findViewById(R.id.text1)
        val date: TextView = itemView.findViewById(R.id.text3)


        fun bind(product: Data){
            if (checkedPosition == -1) {
                my_item_nft.background=context.resources.getDrawable(R.drawable._rectangle_123);

            } else {
                if (checkedPosition == getAdapterPosition()) {
                    my_item_nft.background=context.resources.getDrawable(R.drawable._rectangle);

                } else {
                    my_item_nft.background=context.resources.getDrawable(R.drawable._rectangle_123);

                }
            }
            txtName.text = product.name
            date.text = product.created_at
            Glide.with(context)
                .load(product.image)
                .into(imgAvatar)


            itemView.setOnClickListener {
                my_item_nft.background=context.resources.getDrawable(R.drawable._rectangle);
                if (checkedPosition != getAdapterPosition()) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = getAdapterPosition();
                }
            }
        }

    }

    fun getSelected(): Data? {
        return if (checkedPosition != -1) {
            getItem(checkedPosition)
        } else null
    }
}