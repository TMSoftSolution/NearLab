package com.nft.maker.ui.sent_user_nft_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nft.maker.R

class SendUserNftAdapter (private val clickCallback: (Any) -> Unit = {} ,private val callList: ArrayList<String>): RecyclerView.Adapter<SendUserNftAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_layout_gift_and_nft,parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val currentItem = callList[position]
//        holder.titleImage.setCircleBackgroundColorResource(currentItem.titleImage)
//        holder.name.text = currentItem.name
//        holder.date.text = currentItem.date
//        holder.callType.text = currentItem.callSource
//        holder.callStatus.text = currentItem.callStatus
//        holder.menu.setOnClickListener {
//            clickCallback.invoke(callList[position])
//        }
        holder.itemView.setOnClickListener {
            clickCallback.invoke(0)
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }
}