package com.nft.maker.model.send_nft_api_model

data class SendNftApiModel(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
)