package com.nft.maker.model.mobile_contact_model

import android.graphics.Bitmap

data class MobileContactList(var contactId:String, var contactName:String, var contactNumber:String,var email:String, var contactImage: Bitmap?,var isSection: Boolean,var checked: Boolean) {

}