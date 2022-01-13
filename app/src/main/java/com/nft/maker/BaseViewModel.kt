package com.nft.maker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler


open class BaseViewModel(application: Application) : AndroidViewModel(application){

    // Generic Live Data
     var internetMutableLiveData = MutableLiveData<String>()
     var loadingMutableLiveData = MutableLiveData<Boolean>()
     var errorMutableLiveData = MutableLiveData<String>()

    var exceptionHandler = CoroutineExceptionHandler { _, exception ->
        loadingMutableLiveData.postValue(false)
        errorMutableLiveData.postValue(exception.localizedMessage)
    }

    fun getInternetLiveData(): LiveData<String> {
        return internetMutableLiveData
    }

    fun getLoadingLiveData(): LiveData<Boolean> {
        return loadingMutableLiveData
    }

    fun getErrorLiveData(): LiveData<String> {
        return errorMutableLiveData
    }
}