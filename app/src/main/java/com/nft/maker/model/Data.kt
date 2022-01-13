package com.nft.maker.model

import java.io.Serializable

data class Data(
    val category: String,
    val claimed_explorer_url: String?,
    val claimed_nft_id: String?,
    val created_at: String?,
    val description: String?,
    val explorer_url: String?,
    val id: Int,
    val image: String?,
    val is_nft_claimed: Boolean,
    val is_nft_series_created: Boolean,
    val name: String,
    val nft_id: String,
    val properties:String?,
    val series_id: Any,
    val status: Boolean,
    val token_id: String?,
    val updated_at: String,
    val user_id: Int,
    val uuid: String
):Serializable