package com.nft.maker.model.send_user_model

import okhttp3.MultipartBody
import java.io.File

data class Contacts(val name:String, val phone_no:String, val pic:String?, val email:String)