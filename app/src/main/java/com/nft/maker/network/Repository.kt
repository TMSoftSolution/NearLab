package com.nft.maker.network


import okhttp3.RequestBody
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService ) {

    suspend fun loginEmail(email: RequestBody) = apiService.loginEmail(email)

    suspend fun loginNumber(email: RequestBody) = apiService.loginNumber(email)


    suspend fun registerUserNumber(email: RequestBody,fullName: RequestBody,accountId: RequestBody) = apiService.registerUserNumber(email, fullName, accountId)

    suspend fun registerUserEmail(email: RequestBody,fullName: RequestBody,accountId: RequestBody) = apiService.registerUserEmail(email,fullName,accountId)
    suspend fun checkAccountID(accountId: String) = apiService.checkAccountID(accountId)
}