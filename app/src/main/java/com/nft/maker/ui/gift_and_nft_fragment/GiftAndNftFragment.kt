package com.nft.maker.ui.gift_and_nft_fragment

import android.Manifest
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nft.maker.model.mobile_contact_model.MobileContactList
import com.nft.maker.BaseFragment
import com.nft.maker.R
import com.nft.maker.databinding.FragmentGiftAndNftBinding
import com.nft.maker.ui.sent_user_nft_fragment.ContactViewModel
import com.nft.maker.utils.AlertDialogs
import com.nft.maker.utils.ValidationAlert
import com.nft.maker.utils.retrieveAllContacts
import dagger.hilt.android.AndroidEntryPoint
import kotlin.coroutines.CoroutineContext
import androidx.recyclerview.widget.RecyclerView
import com.nft.maker.extension.Extension.getQueryTextChangeStateFlow
import com.nft.maker.utils.searchContactByName
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter


@AndroidEntryPoint
class GiftAndNftFragment : BaseFragment(), CoroutineScope {
    private lateinit var fragmentGiftAndNftBinding: FragmentGiftAndNftBinding
    private lateinit var contactsAdapter: GiftAndSendAdapter
    private lateinit var contactViewModel: ContactViewModel
    lateinit var giftAndNftViewModel: GiftAndNftViewModel
    lateinit var contactArrayList: ArrayList<MobileContactList>

    private var offset : Int = 0
    private var loading = true
    var pastVisiblesItems : Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0

    private lateinit var linearLayoutManager : LinearLayoutManager

    override val layoutId: View
        get() {
            fragmentGiftAndNftBinding =
                FragmentGiftAndNftBinding.inflate(layoutInflater)
            return fragmentGiftAndNftBinding.root
        }
    override val layoutTag: String?
        get() = GiftAndNftFragment::class.java.simpleName

    override fun initUI(view: View) {
        contactArrayList = arrayListOf<MobileContactList>()
        initiate()
        clickListeners()
        setObservers()
        onBackPressed()

    }

    private fun initiate() {
        giftAndNftViewModel = ViewModelProvider(this).get(GiftAndNftViewModel::class.java)

        contactViewModel = ViewModelProvider(requireActivity()).get(ContactViewModel::class.java)

        linearLayoutManager =  LinearLayoutManager(context)
        fragmentGiftAndNftBinding.recyclerView.layoutManager = linearLayoutManager
        fragmentGiftAndNftBinding.recyclerView.setHasFixedSize(true)
        contactsAdapter = GiftAndSendAdapter(
            {}, {}, requireContext(), contactArrayList
        )
        fragmentGiftAndNftBinding.recyclerView.adapter = contactsAdapter

    }


    private fun clickListeners() {
        fragmentGiftAndNftBinding.totalContacts.setOnClickListener {
            checkPermissions()

        }

        fragmentGiftAndNftBinding.giftNft.setOnClickListener {

            if (contactsAdapter.getSelectedContact() == null) {
                ValidationAlert.getValidationDialog(
                    requireContext(),
                    false,
                    "Alert",
                    "Please select contact"
                )

            } else {
                var email = contactsAdapter.getSelectedContact().toMutableList()
                giftAndNftViewModel.sendUserData(email)


            }

        }

//        fragmentGiftAndNftBinding.searchBar.setQueryHint("Search People")
//        fragmentGiftAndNftBinding.searchBar.setIconified(false)
//        fragmentGiftAndNftBinding.searchBar.clearFocus()
//        fragmentGiftAndNftBinding.searchBar.setIconifiedByDefault(false)
//        val searchEditText: EditText =
//            fragmentGiftAndNftBinding.searchBar.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
//        searchEditText.setTextColor(resources.getColor(android.R.color.black))
//        searchEditText.setHintTextColor(resources.getColor(R.color.light_grey_color))
//        fragmentGiftAndNftBinding.searchBar.setOnQueryTextListener(object :
//            SearchView.OnQueryTextListener,
//            androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                // filter recycler view when query submitted
//                contactsAdapter.getFilter()?.filter(query)
//                return false
//            }
//
//            override fun onQueryTextChange(query: String?): Boolean {
//                // filter recycler view when text is changed
//                contactsAdapter.getFilter()?.filter(query)
//                return false
//            }
//        })

        launch {
            fragmentGiftAndNftBinding.searchBar.getQueryTextChangeStateFlow().filter { query ->

                if (query.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        contactsAdapter.setNewContactsList(contactArrayList)
                        fragmentGiftAndNftBinding.searchBar.text.clear()
                        showLog(fragmentGiftAndNftBinding.searchBar.text.toString() + " heloo  ")
                    }
                    return@filter false
                } else {
                    return@filter true
                }
            }.debounce(400).collect { value: String ->
                showLog(value)
                withContext(Dispatchers.Main) {
                    mBaseActivity?.requestReadContactsPermission() {

                        if (it) {
                            if (fragmentGiftAndNftBinding.searchBar.text.toString().isNotEmpty()){
                                val contacts = requireContext().searchContactByName(searchPattern = value)
                                contactsAdapter.setNewContactsList(contacts)
                            }
                        } else {
                            ValidationAlert.getValidationDialog(
                                requireContext(),
                                false,
                                "Alert",
                                "Permissions are required to continue"
                            )
                        }
                    }
                }
            }
        }

        fragmentGiftAndNftBinding.recyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = linearLayoutManager.getChildCount()
                    totalItemCount = linearLayoutManager.getItemCount()
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {

                            fragmentGiftAndNftBinding.progressBar.visibility = View.VISIBLE

                            loading = false

                            offset = offset.plus(30)
                            val contacts = requireContext().retrieveAllContacts(offset = offset)

                            contactArrayList.addAll(contacts)
                            contactsAdapter.setContactsList(contactArrayList)

                            loading = true
                            fragmentGiftAndNftBinding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        })

    }

    private fun checkPermissions() {


        mBaseActivity?.requestReadContactsPermission(){

            if (it){
                setAlertDialog(AlertDialogs.getProgressDialog(requireContext(), true))
                val contacts = requireContext().retrieveAllContacts(offset = 0)
                contactArrayList.addAll(contacts)
                contactsAdapter.setContactsList(contactArrayList)
                getAlertDialog().dismiss()
            }else{
                ValidationAlert.getValidationDialog(
                    requireContext(),
                    false,
                    "Alert",
                    "Permissions are required to continue"
                )
            }
        }
//        Dexter.withContext(requireContext())
//            .withPermissions(
//                Manifest.permission.READ_CONTACTS,
////                Manifest.permission.WRITE_CONTACTS,
////                Manifest.permission.READ_EXTERNAL_STORAGE,
////                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            )
//            .withListener(object : MultiplePermissionsListener {
//                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
//                    if (report!!.areAllPermissionsGranted()) {
//
//
//                    //                        contactViewModel.init(requireContext())
////                        contactViewModel.getContacts().observe(viewLifecycleOwner) {
////                            if (isAlertInitialized()) {
////                                getAlertDialog().dismiss()
////                            }
////                            contactArrayList.addAll(it)
////                            contactsAdapter.notifyDataSetChanged()
////
////                        }
//                    } else if (report.isAnyPermissionPermanentlyDenied) {
//                        ValidationAlert.getValidationDialog(
//                            requireContext(),
//                            false,
//                            "Alert",
//                            "Permissions are required to continue"
//                        )
//
//                    }
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    permission: MutableList<PermissionRequest>?,
//                    token: PermissionToken?
//                ) {
//                    token?.continuePermissionRequest()
//                }
//            }).check()
    }

    fun onBackPressed() {
        fragmentGiftAndNftBinding.backButton.setOnClickListener {
            findNavController().popBackStack()

        }
        view.isFocusableInTouchMode = true;
        view.requestFocus()
        view.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        findNavController().popBackStack()
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun setObservers() {

        giftAndNftViewModel.getInternetLiveData().observe(this, {
            ValidationAlert.getValidationDialog(requireContext(), false, "Alert", it)

        })

        giftAndNftViewModel.getErrorLiveData().observe(this, {
            if (it != null) {
                ValidationAlert.getValidationDialog(requireContext(), false, "Alert", it)

            }
        })

        giftAndNftViewModel.getLoadingLiveData().observe(this, {
            try {
                if (it)
                    setAlertDialog(AlertDialogs.getProgressDialog(requireContext(), true))
                else {
                    if (isAlertInitialized())
                        getAlertDialog().dismiss()
                }
            } catch (e: Exception) {

            }

        })

        giftAndNftViewModel.getSendUserMutableLiveData().observe(this, {
            val bundle =Bundle().apply {
                putString("key","true")
            }
            findNavController().navigate(R.id.action_giftAndNftFragment_to_createNftFragment2,bundle)


        })
    }


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO


}