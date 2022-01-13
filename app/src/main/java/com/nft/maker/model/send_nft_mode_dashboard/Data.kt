package com.nft.maker.model.send_nft_mode_dashboard

import java.io.Serializable

data class Data(
    val category: String,
    val created_at: String,
    val description: String,
    val explorer_url: String,
    val id: Int,
    val image: String,
    val name: String,
    val `receiver`: Receiver,
    val receiver_id: Int,
    val sender: Sender,
    val sender_id: Int,
    val status: Boolean,
    val is_nft_claimed: Boolean,
    val token_id: String,
    val updated_at: String,
    val user_image_id: Int,
    val uuid: String
):Serializable