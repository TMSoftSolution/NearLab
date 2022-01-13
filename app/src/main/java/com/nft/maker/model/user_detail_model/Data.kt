package com.nft.maker.model.user_detail_model

data class Data(
    val account_id: String,
    val created_at: String,
    val email: String,
    val explorer_url: String,
    val full_name: String,
    val id: Int,
    val is_registered: Boolean,
    val is_wallet_created: Boolean,
    val near_key: String,
    val nft_id: String,
    val phone_no: String,
    val updated_at: String
)