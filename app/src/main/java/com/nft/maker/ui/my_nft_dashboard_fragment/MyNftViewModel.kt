package com.nft.maker.ui.my_nft_dashboard_fragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nft.maker.BaseViewModel
import com.nft.maker.model.MyNftModel
import com.nft.maker.network.ApiService
import com.nft.maker.network.Repository
import com.nft.maker.utils.Constants
import com.nft.maker.utils.NetworkManager
import com.nft.maker.utils.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
@HiltViewModel
class MyNftViewModel @Inject constructor(
    private val dashboardRepository: Repository,
    private val preferenceHelper: PreferenceHelper,
    private val networkManager: NetworkManager, application: Application
) : BaseViewModel(application) {


    private val loginMutableLiveData = MutableLiveData<MyNftModel>()

    fun getAllDataMyNft() {
        loadingMutableLiveData.postValue(true)
        viewModelScope.launch(exceptionHandler) {
            if (networkManager.isConnected){
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
                val loginData = service.getData()
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

    fun getMyNftMutableLiveData(): LiveData<MyNftModel> {
        return loginMutableLiveData
    }

}