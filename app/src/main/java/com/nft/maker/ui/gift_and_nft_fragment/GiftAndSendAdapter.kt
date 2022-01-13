package com.nft.maker.ui.gift_and_nft_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nft.maker.model.mobile_contact_model.MobileContactList
import com.nft.maker.R
import com.nft.maker.model.send_user_model.Contacts
import com.nft.maker.utils.UtilsFunction
import java.lang.ref.WeakReference

class GiftAndSendAdapter(
    var clickCallMenu: (MobileContactList) -> Unit = {},
    var clickCallItem: (MobileContactList) -> Unit = {},
    var context: Context,
    mobileContactList: ArrayList<MobileContactList>
) :

    RecyclerView.Adapter<RecyclerView.ViewHolder?>(), Filterable {
    var mobileContactList: ArrayList<MobileContactList> = mobileContactList
    var contactListFiltered: ArrayList<MobileContactList> = mobileContactList

    var mContextWeakReference: WeakReference<Context> = WeakReference(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = mContextWeakReference.get()
        return if (viewType == SECTION_VIEW) {
            SectionHeaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.contact_alphabet_view, parent, false)
            )
        } else ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_layout_gift_and_nft, parent, false), context
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (mobileContactList[position].isSection) {
            SECTION_VIEW
        } else {
            CONTENT_VIEW
        }
    }

    fun setContactsList(list: List<MobileContactList>){
        mobileContactList.addAll(list)
        notifyDataSetChanged()

    }

    fun setNewContactsList(list: List<MobileContactList>){
        mobileContactList.clear()
        mobileContactList = ArrayList(list)
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val context = mContextWeakReference.get() ?: return
        if (SECTION_VIEW == getItemViewType(position)) {
            val sectionHeaderViewHolder = holder as SectionHeaderViewHolder
            sectionHeaderViewHolder.headerTitleTextview.text =
                mobileContactList[position].contactName
            return
        }
        val itemViewHolder = holder as ItemViewHolder
        itemViewHolder.name.text = mobileContactList[position].contactName
        // itemViewHolder.useName.text = mobileContactList[position].contactNumber
        itemViewHolder.useName.text = mobileContactList[position].contactNumber
        if (mobileContactList[position].contactImage != null)
            itemViewHolder.titleImage.setImageBitmap(mobileContactList[position].contactImage)
        else
            itemViewHolder.titleImage.setImageResource(R.drawable.ic_avatar)


//        itemViewHolder.menu.setOnClickListener {
//            clickCallMenu.invoke(mobileContactList[position])
//        }
//        itemViewHolder.itemView.setOnClickListener {
//            clickCallItem.invoke(mobileContactList[position])
//
//        }
        if (mobileContactList[position].checked) {
            itemViewHolder.more_contacts.setImageResource(R.drawable.ic_blue_tick)

        } else {
            itemViewHolder.more_contacts.setImageResource(R.drawable.ic_ellipse_grey)

        }
        itemViewHolder.itemView.setOnClickListener {
            if (mobileContactList[position].checked) {
                itemViewHolder.more_contacts.setImageResource(R.drawable.ic_ellipse_grey)
                mobileContactList[position].checked = false

            } else {
                itemViewHolder.more_contacts.setImageResource(R.drawable.ic_blue_tick)
                mobileContactList[position].checked = true

            }

        }


    }

    override fun getItemCount(): Int {
        return mobileContactList.size
    }


    class ItemViewHolder(itemView: View, context: Context?) :
        RecyclerView.ViewHolder(itemView) {
        val titleImage: de.hdodenhof.circleimageview.CircleImageView =
            itemView.findViewById(R.id.contact_image)
        val name: TextView = itemView.findViewById(R.id.contact_name)
        val useName: TextView = itemView.findViewById(R.id.contact_userName)
        val more_contacts: ImageView = itemView.findViewById(R.id.more_contacts)
    }

    fun getSelectedContact(): ArrayList<Contacts> {
        val selected: ArrayList<MobileContactList> = ArrayList()
        val contactsList: ArrayList<Contacts> = ArrayList()

        for (i in 0 until mobileContactList.size) {
            if (mobileContactList.get(i).checked) {
                selected.add(mobileContactList.get(i))

                val base64 = if (mobileContactList.get(i).contactImage != null) UtilsFunction.compressBitmap(mobileContactList.get(i).contactImage!!) else null
                val number=   mobileContactList.get(i).contactNumber.replace(" ","")
                var sendUser = Contacts(
                    mobileContactList.get(i).contactName,
                    number,
                    base64,
                    mobileContactList.get(i).email
                )

                contactsList.add(sendUser)


            }
        }
        return contactsList
    }

    inner class SectionHeaderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var headerTitleTextview: TextView = itemView.findViewById<View>(R.id.text_bar) as TextView
    }

    companion object {
        const val SECTION_VIEW = 0
        const val CONTENT_VIEW = 1
    }


    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    mobileContactList = contactListFiltered
                } else {
                    val filteredList: ArrayList<MobileContactList> = ArrayList()
                    for (row in contactListFiltered) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.contactName.toLowerCase()
                                .contains(charString.toLowerCase()) || row.contactName
                                .contains(charSequence)
                        ) {
                            filteredList.add(row)
                        }
                    }
                    mobileContactList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = mobileContactList
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults
            ) {
                mobileContactList = filterResults.values as ArrayList<MobileContactList>
                notifyDataSetChanged()
            }
        }
    }

}