package com.nft.maker.model.send_nft_mode_dashboard

data class Receiver(
    val account_id: String?,
    val created_at: String,
    val email: String?,
    val explorer_url: String?,
    val full_name: String?,
    val id: Int,
    val is_registered: Boolean,
    val is_wallet_created: Boolean,
    val nft_id: String?,
    val phone_no: String,
    val updated_at: String
)