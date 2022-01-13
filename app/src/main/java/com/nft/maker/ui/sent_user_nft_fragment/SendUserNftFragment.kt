package com.nft.maker.ui.sent_user_nft_fragment

import android.content.Intent
import android.view.*
import android.widget.EditText
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nft.maker.model.mobile_contact_model.MobileContactList
import com.nft.maker.BaseFragment
import com.nft.maker.R
import com.nft.maker.databinding.FragmentSendUserNftBinding
import com.nft.maker.extension.Extension.getQueryTextChangeStateFlow
import com.nft.maker.model.Data
import com.nft.maker.ui.dasboard_activity.DashBoardActivity
import com.nft.maker.ui.send_nft_dasboard_fragment.SendNftDasboardFragment
import com.nft.maker.utils.AlertDialogs
import com.nft.maker.utils.ValidationAlert
import com.nft.maker.utils.retrieveAllContacts
import com.nft.maker.utils.searchContactByName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class SendUserNftFragment : BaseFragment(), CoroutineScope {
    private lateinit var fragmentSendUserNftBinding: FragmentSendUserNftBinding
    private lateinit var contactsAdapter: ContactsAdapter
    private lateinit var contactViewModel: ContactViewModel
    lateinit var sendUserNftViewModel: SendUserNftViewModel
    var uuid: String = ""
    var key: String? = ""

    private var offset: Int = 0
    private var loading = true
    var pastVisiblesItems: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0

    private lateinit var linearLayoutManager: LinearLayoutManager

    lateinit var contactArrayList: ArrayList<MobileContactList>
    lateinit var contactArrayLists: ArrayList<MobileContactList>
    override val layoutId: View
        get() {
            fragmentSendUserNftBinding =
                FragmentSendUserNftBinding.inflate(layoutInflater)
            return fragmentSendUserNftBinding.root
        }
    override val layoutTag: String?
        get() = SendNftDasboardFragment::class.java.simpleName

    override fun initUI(view: View) {
        contactArrayList = arrayListOf()
        contactArrayLists = arrayListOf()
        initiate()
        clickListeners()
        setObservers()

    }

    private fun initiate() {
        key = arguments?.getString("key")
        if (key.equals("SendNft")) {
            val data = arguments?.getSerializable("data") as Data
            uuid = data.uuid

        } else if (key.equals("preview")) {
            uuid = arguments?.getString("uuid").toString()

        }
        sendUserNftViewModel = ViewModelProvider(this).get(SendUserNftViewModel::class.java)

        contactViewModel = ViewModelProvider(requireActivity()).get(ContactViewModel::class.java)

        linearLayoutManager = LinearLayoutManager(context)
        fragmentSendUserNftBinding.recyclerView.layoutManager = linearLayoutManager
        fragmentSendUserNftBinding.recyclerView.setHasFixedSize(true)
        contactsAdapter = ContactsAdapter(
            {}, {}, requireContext(), contactArrayList
        )
        fragmentSendUserNftBinding.recyclerView.adapter = contactsAdapter

        launch {
            fragmentSendUserNftBinding.searchBar.getQueryTextChangeStateFlow().filter { query ->

                if (query.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        contactsAdapter.setNewContactsList(contactArrayList)
                        fragmentSendUserNftBinding.totalContacts.text = contactArrayList.size.toString() + " contacts found"
                        fragmentSendUserNftBinding.searchBar.text.clear()
                        showLog(fragmentSendUserNftBinding.searchBar.text.toString() + " heloo  ")
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
                            if (fragmentSendUserNftBinding.searchBar.text.toString().isNotEmpty()){
                                val contacts = requireContext().searchContactByName(searchPattern = value)
                                contactsAdapter.setNewContactsList(contacts)
                                fragmentSendUserNftBinding.totalContacts.text = contacts.size.toString() + " contacts found"
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


        setAlertDialog(AlertDialogs.getProgressDialog(requireContext(), true))

        mBaseActivity?.requestReadContactsPermission() {

            if (it) {
                val contacts = requireContext().retrieveAllContacts(offset = 0)
                contactArrayList.addAll(contacts)
                contactArrayLists.addAll(contacts)
                contactsAdapter.setContactsList(contactArrayList)
                fragmentSendUserNftBinding.totalContacts.text = contactArrayList.size.toString() + " contacts found"

            } else {
                ValidationAlert.getValidationDialog(
                    requireContext(),
                    false,
                    "Alert",
                    "Permissions are required to continue"
                )
            }

            getAlertDialog().dismiss()
        }

//        contactViewModel.init(requireContext())
//        contactViewModel.getContacts().observe(viewLifecycleOwner) {
//            if (isAlertInitialized()) {
//                getAlertDialog().dismiss()
//            }
//            fragmentSendUserNftBinding.totalContacts.text = "" + it.size + " contact found"
//            contactArrayList.addAll(it)
//            contactsAdapter.notifyDataSetChanged()
//
//        }


        fragmentSendUserNftBinding.recyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = linearLayoutManager.getChildCount()
                    totalItemCount = linearLayoutManager.getItemCount()
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            fragmentSendUserNftBinding.progressBar.visibility = View.VISIBLE
                            loading = false
                            offset = offset.plus(30)
                            val contacts = requireContext().retrieveAllContacts(offset = offset)
                            contactArrayList.addAll(contacts)
                            contactArrayLists.addAll(contacts)
                            contactsAdapter.setContactsList(contactArrayList)
                            fragmentSendUserNftBinding.totalContacts.text = contactArrayList.size.toString() + " contacts found"


                            loading = true
                            fragmentSendUserNftBinding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        })
    }


    private fun clickListeners() {
        fragmentSendUserNftBinding.backButton.setOnClickListener {
            if (key.equals("preview")) {
                findNavController().popBackStack(R.id.homeDashboardFragment, true)

            } else {
                findNavController().popBackStack()

            }
        }

        fragmentSendUserNftBinding.giftNft.setOnClickListener {

            if (contactsAdapter.getSelectedContact() == null) {
                ValidationAlert.getValidationDialog(
                    requireContext(),
                    false,
                    "Alert",
                    "Please select contact"
                )

            } else {
                var email = contactsAdapter.getSelectedContact()
                val emails = email.substring(0, email.length - 1)
                sendUserNftViewModel.sendNftData(uuid, emails)


            }

        }

//        fragmentSendUserNftBinding.searchBar.setQueryHint("Search People")
//        fragmentSendUserNftBinding.searchBar.setIconified(false)
//        fragmentSendUserNftBinding.searchBar.clearFocus()
//        fragmentSendUserNftBinding.searchBar.setIconifiedByDefault(false)
//        val searchEditText: EditText =
//            fragmentSendUserNftBinding.searchBar.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
//        searchEditText.setTextColor(resources.getColor(android.R.color.black))
//        searchEditText.setHintTextColor(resources.getColor(R.color.light_grey_color))
//        fragmentSendUserNftBinding.searchBar.setOnQueryTextListener(object :
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


    }

    private fun setObservers() {

        sendUserNftViewModel.getInternetLiveData().observe(this, {
            ValidationAlert.getValidationDialog(requireContext(), false, "Alert", it)

        })

        sendUserNftViewModel.getErrorLiveData().observe(this, {
            if (it != null) {
                ValidationAlert.getValidationDialog(requireContext(), false, "Alert", it)

            }
        })

        sendUserNftViewModel.getLoadingLiveData().observe(this, {
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

        sendUserNftViewModel.getSendMutableLiveData().observe(this, {

            val i = Intent(requireContext(), DashBoardActivity::class.java)
            startActivity(i)
            requireActivity().finish()


        })
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO


}