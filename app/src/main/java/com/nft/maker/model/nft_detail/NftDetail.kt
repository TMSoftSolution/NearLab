package com.nft.maker.model.nft_detail

import com.nft.maker.model.Data

data class NftDetail(
    val `data`: Data?,
    val message: String,
    val success: Boolean
)