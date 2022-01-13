package com.nft.maker.model.check_account_model

data class CheckAccountModel(
    val message: String,
    val status: Int,
    val success: Boolean
)