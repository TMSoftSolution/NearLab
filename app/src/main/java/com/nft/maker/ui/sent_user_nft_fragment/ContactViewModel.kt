package com.nft.maker.ui.sent_user_nft_fragment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nft.maker.model.mobile_contact_model.MobileContactList
import com.nft.maker.network.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactViewModel() : ViewModel() {

    lateinit var mContext: Context;
    lateinit var mRepository: ContactRepository;
    var contacts = MutableLiveData<ArrayList<MobileContactList>>()
    var connectycubeUser = MutableLiveData<MobileContactList>()


    fun init(context: Context) {
        mRepository = ContactRepository(context)
        mContext = context



    }

    fun sendDialogue(text: MobileContactList) {
        connectycubeUser.postValue(text)
    }

    fun getDialogue(): LiveData<MobileContactList> {
        return connectycubeUser
    }

    @JvmName("getContacts1")
    fun getContacts(): MutableLiveData<ArrayList<MobileContactList>> {
        viewModelScope.launch(Dispatchers.IO) {
            contacts.postValue(mRepository.fetchContacts());
        }
        return contacts;
    }

}