package com.nft.maker.model.send_nft_mode_dashboard

data class SendNftModel(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
)