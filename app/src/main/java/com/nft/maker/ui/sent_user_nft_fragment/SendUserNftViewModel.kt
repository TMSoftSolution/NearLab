package com.nft.maker.ui.sent_user_nft_fragment

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nft.maker.BaseViewModel
import com.nft.maker.model.send_nft_api_model.SendNftApiModel
import com.nft.maker.model.upload_preview_nft_model.PreviewNftModel
import com.nft.maker.network.ApiService
import com.nft.maker.network.Repository
import com.nft.maker.utils.Constants
import com.nft.maker.utils.FileUtils
import com.nft.maker.utils.NetworkManager
import com.nft.maker.utils.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
@HiltViewModel
class SendUserNftViewModel @Inject constructor(
    private val repository: Repository,
    private val preferenceHelper: PreferenceHelper,
    private val networkManager: NetworkManager, application: Application
) : BaseViewModel(application) {


    private val loginMutableLiveData = MutableLiveData<SendNftApiModel>()

    fun sendNftData(uuid: String, emails: String) {
        loadingMutableLiveData.postValue(true)
        viewModelScope.launch(exceptionHandler) {

            if (networkManager.isConnected ){
                val httpClient = OkHttpClient.Builder()

                httpClient.addInterceptor(Interceptor { chain ->
                    val request: Request =
                        chain.request().newBuilder().header("Authorization", preferenceHelper.authToken).build()
                    chain.proceed(request)
                })
                val retrofit: Retrofit =
                    Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Constants.BASE_URL_DASHBOARD)
                        .client(httpClient.readTimeout(2 , TimeUnit.MINUTES).connectTimeout(2 , TimeUnit.MINUTES).build()).build()

                val service = retrofit.create(ApiService::class.java)
                val loginData = service.sendNftData(uuid,emails)
                loadingMutableLiveData.postValue(false)

                if(loginData.success){
                    loginMutableLiveData.postValue(loginData)
                }else{
                    errorMutableLiveData.postValue(loginData.message)
                }
            }else{
                loadingMutableLiveData.postValue(false)
                internetMutableLiveData.postValue(Constants.NO_INTERNET_CONNECTION)
            }
        }

    }

    fun getSendMutableLiveData(): LiveData<SendNftApiModel> {
        return loginMutableLiveData
    }

}