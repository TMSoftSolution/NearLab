package com.nft.maker.network

import com.nft.maker.model.MyNftModel
import com.nft.maker.model.check_account_model.CheckAccountModel
import com.nft.maker.model.claim_image_model.ClaimImageModel
import com.nft.maker.model.nft_detail.NftDetail
import com.nft.maker.model.send_nft_api_model.SendNftApiModel
import com.nft.maker.model.send_nft_mode_dashboard.SendNftModel
import com.nft.maker.model.send_user_model.AddedContact
import com.nft.maker.model.send_user_model.Contacts
import com.nft.maker.model.sign_up_model.SignUpModel
import com.nft.maker.model.upload_preview_nft_model.PreviewNftModel
import com.nft.maker.model.user_detail_model.UserDetailModel
import okhttp3.RequestBody

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    @Multipart
    @POST("login")
    suspend fun loginEmail(@Part("user[email]") email: RequestBody): Response<SignUpModel>

    @Multipart
    @POST("login")
    suspend fun loginNumber(@Part("user[phone_no]") email: RequestBody): Response<SignUpModel>

    @Multipart
    @POST("signup")
    suspend fun registerUserEmail(
        @Part("user[email]") email: RequestBody,
        @Part("user[full_name]") fullName: RequestBody,
        @Part("user[account_id]") accountID: RequestBody
    ): Response<SignUpModel>


    @POST("contacts")
    suspend fun saveUserContactDetail(@Body contacts: List<Contacts>): AddedContact

    @Multipart
    @POST("signup")
    suspend fun registerUserNumber(
        @Part("user[phone_no]") phone: RequestBody,
        @Part("user[full_name]") fullName: RequestBody,
        @Part("user[account_id]") accountID: RequestBody
    ): Response<SignUpModel>


    @GET("/api/v1/user_images/fetch_user_image")
    suspend fun fetchNftDetails(@Query("uuid") uuid: String): Response<NftDetail>


    @Multipart
    @POST("user_images")
    suspend fun updateProfile(
        @Part("user_image[name]") tittle: RequestBody,
        @Part imageFile: MultipartBody.Part,
        @Part("user_image[description]") description: RequestBody,
        @Part("user_image[category]") category: RequestBody,
        @Part("send_nft") send_nft: RequestBody
    ): PreviewNftModel


    @GET("user_images")
    suspend fun getData(): MyNftModel

    @POST("user_images/send_image")
    suspend fun sendNftData(
        @Query("uuid") uuid: String,
        @Query("emails") emials: String
    ): SendNftApiModel

    @GET("nft_histories/sender_list")
    suspend fun getDataSendNft(): SendNftModel

    @GET("nft_histories/receiver_list")
    suspend fun getDataReceiveNft(): SendNftModel


    @POST("user_images/claim_image")
    suspend fun claimNftImage(
        @Query("user_id") id: Int,
        @Query("uuid") uuid: String
    ): ClaimImageModel


    @GET("check_account_id")
    suspend fun checkAccountID(@Query("account_id") account_id: String): CheckAccountModel

    @GET("users/details")
    suspend fun getUserDetail(): UserDetailModel
}