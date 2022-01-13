package com.nft.maker.model.sign_up_model

data class Data(
    val account_id: String,
    val created_at: String,
    val email: String,
    val full_name: String?,
    val id: Int,
    val phone_no: String,
    val updated_at: String
)