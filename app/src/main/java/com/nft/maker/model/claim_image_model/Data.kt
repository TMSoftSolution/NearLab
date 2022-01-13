package com.nft.maker.model.claim_image_model

data class Data(
    val created_at: String,
    val id: Int,
    val image: String,
    val name: String,
    val status: Boolean,
    val updated_at: String,
    val user_id: Int,
    val uuid: String
)