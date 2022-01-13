package com.nft.maker.model.send_nft_api_model

data class Data(
    val category: Any,
    val created_at: String,
    val description: Any,
    val id: Int,
    val image: String,
    val name: String,
    val properties: Any,
    val status: Boolean,
    val updated_at: String,
    val user_id: Int,
    val uuid: String
)