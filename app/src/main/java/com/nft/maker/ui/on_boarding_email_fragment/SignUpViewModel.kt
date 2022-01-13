package com.nft.maker.ui.on_boarding_email_fragment

import android.app.Application
import androidx.lifecycle.*
import com.nft.maker.BaseViewModel
import com.nft.maker.model.sign_up_model.SignUpModel
import com.nft.maker.model.user_detail_model.UserDetailModel
import com.nft.maker.network.ApiService
import com.nft.maker.network.Repository
import com.nft.maker.utils.Constants
import com.nft.maker.utils.NetworkManager
import com.nft.maker.utils.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val mainRepository: Repository,
    private val preferenceHelper: PreferenceHelper,
    private val networkManager: NetworkManager, application: Application
) : BaseViewModel(application) {

    private val loginMutableLiveData = MutableLiveData<UserDetailModel>()

    fun loginUserAuthToken(authToken:String) {
        loadingMutableLiveData.postValue(true)
        viewModelScope.launch(exceptionHandler) {
            if (networkManager.isConnected) {
                val httpClient = OkHttpClient.Builder()
                httpClient.addInterceptor(Interceptor { chain ->
                    val request: Request =
                        chain.request().newBuilder().addHeader("Authorization", authToken).build()
                    chain.proceed(request)
                })
                val retrofit: Retrofit =
                    Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Constants.BASE_URL_DASHBOARD)
                        .client(httpClient.readTimeout(2 , TimeUnit.MINUTES).connectTimeout(2 , TimeUnit.MINUTES).build()).build()
                val service = retrofit.create(ApiService::class.java)
                val loginData = service.getUserDetail()
                loadingMutableLiveData.postValue(false)

                if (loginData.success) {
                    preferenceHelper.authToken = authToken
                    loginMutableLiveData.postValue(loginData)
                } else  {
                    errorMutableLiveData.postValue(loginData.message)
                }
            }

             else {
                loadingMutableLiveData.postValue(false)
                internetMutableLiveData.postValue(Constants.NO_INTERNET_CONNECTION)
            }
        }

    }



    fun getLoginMutableLiveData(): LiveData<UserDetailModel> {
        return loginMutableLiveData
    }

}