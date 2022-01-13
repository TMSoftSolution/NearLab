package com.nft.maker.ui.create_near_fragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nft.maker.BaseViewModel
import com.nft.maker.model.check_account_model.CheckAccountModel
import com.nft.maker.model.sign_up_model.SignUpModel
import com.nft.maker.network.Repository
import com.nft.maker.utils.Constants
import com.nft.maker.utils.NetworkManager
import com.nft.maker.utils.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class CreateNearViewModel @Inject constructor(
    private val mainRepository: Repository,
    private val preferenceHelper: PreferenceHelper,
    private val networkManager: NetworkManager, application: Application
) : BaseViewModel(application) {


    private val loginMutableLiveData = MutableLiveData<SignUpModel>()
    private val checkAccountMutableLiveData = MutableLiveData<CheckAccountModel>()

    fun registerByEmail(number: RequestBody, fullName: RequestBody, accountId: RequestBody) {
        loadingMutableLiveData.postValue(true)
        viewModelScope.launch(exceptionHandler) {
            if (networkManager.isConnected) {
                val RegisterData = mainRepository.registerUserEmail(number, fullName, accountId)
                loadingMutableLiveData.postValue(false)
                if (RegisterData.body()?.success == true) {
                    preferenceHelper.authToken = RegisterData.headers().get("Authorization").toString()
                    loginMutableLiveData.postValue(RegisterData.body())
                }
                else {
                    errorMutableLiveData.postValue(RegisterData.body()?.message)
                }

            } else {
                loadingMutableLiveData.postValue(false)
                internetMutableLiveData.postValue(Constants.NO_INTERNET_CONNECTION)
            }
        }

    }

    fun registerByNumber(email: RequestBody, fullName: RequestBody, accountId: RequestBody) {
        loadingMutableLiveData.postValue(true)
        viewModelScope.launch(exceptionHandler) {
            if (networkManager.isConnected) {
                val RegisterData = mainRepository.registerUserNumber(email, fullName, accountId)
                loadingMutableLiveData.postValue(false)
                if (RegisterData.body()?.success == true) {
                    preferenceHelper.authToken = RegisterData.headers().get("Authorization").toString()
                    loginMutableLiveData.postValue(RegisterData.body())
                } else {
                    errorMutableLiveData.postValue(RegisterData.body()?.message)
                }


            } else {
                loadingMutableLiveData.postValue(false)
                internetMutableLiveData.postValue(Constants.NO_INTERNET_CONNECTION)
            }
        }

    }


    fun checkAccountID(accountId: String) {
        viewModelScope.launch(exceptionHandler) {
            if (networkManager.isConnected) {
                val RegisterData = mainRepository.checkAccountID(accountId)
                if (RegisterData.success) {
                    checkAccountMutableLiveData.postValue(RegisterData)
                } else {
                    errorMutableLiveData.postValue(RegisterData.message)
                }


            } else {
                internetMutableLiveData.postValue(Constants.NO_INTERNET_CONNECTION)
            }
        }

    }

    fun getLoginMutableLiveData(): LiveData<SignUpModel> {
        return loginMutableLiveData
    }

    fun getCheckAccountMutableLiveData(): LiveData<CheckAccountModel> {
        return checkAccountMutableLiveData
    }

}