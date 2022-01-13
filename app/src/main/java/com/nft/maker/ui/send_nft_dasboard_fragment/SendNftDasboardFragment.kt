package com.nft.maker.ui.send_nft_dasboard_fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nft.maker.BaseFragment
import com.nft.maker.R
import com.nft.maker.databinding.FragmentSendNftDasboardBinding
import com.nft.maker.model.send_nft_mode_dashboard.Data
import com.nft.maker.utils.AlertDialogs
import com.nft.maker.utils.ValidationAlert
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
 @AndroidEntryPoint
class SendNftDasboardFragment: BaseFragment() ,CoroutineScope{
    private lateinit var fragmentSendNftDasboardBinding: FragmentSendNftDasboardBinding
    private lateinit var sendNftViewModel: SendNftViewModel
    private lateinit var myNftAdapter: SendNftAdapter
    override val layoutId: View
        get() {
            fragmentSendNftDasboardBinding =
                FragmentSendNftDasboardBinding.inflate(layoutInflater)
            return fragmentSendNftDasboardBinding.root
        }
    override val layoutTag: String?
        get() = SendNftDasboardFragment::class.java.simpleName

    override fun initUI(view: View) {
        initiate()
        setRecyclerView()
        setListener()
        setObservers()

    }
   





    private fun initiate() {
        sendNftViewModel = ViewModelProvider(this).get(SendNftViewModel::class.java)

    }


    private fun setObservers() {
        sendNftViewModel.getAllDataMyNft()

        sendNftViewModel.getInternetLiveData().observe(this, {
            ValidationAlert.getValidationDialog(requireContext(), false, "Alert", it)

        })

        sendNftViewModel.getErrorLiveData().observe(this, {
            ValidationAlert.getValidationDialog(requireContext(), false, "Alert", it)
        })


        sendNftViewModel.getLoadingLiveData().observe(this, {
            if (it && mBaseActivity?.isAlertDialogInitialized() == false)
                setAlertDialog(AlertDialogs.getProgressDialog(requireContext(), false)) else {
                if (isAlertInitialized()) getAlertDialog().dismiss()
            }
        })

        sendNftViewModel.getMyNftMutableLiveData().observe(this, {

            if (it.data.isNotEmpty() ) {
                fragmentSendNftDasboardBinding.recyclerView.visibility=View.VISIBLE
                fragmentSendNftDasboardBinding.noRecord.visibility=View.GONE
                myNftAdapter.submitList(it.data)
            }else{
                fragmentSendNftDasboardBinding.recyclerView.visibility=View.GONE
                fragmentSendNftDasboardBinding.noRecord.visibility=View.VISIBLE

            }

        })
    }

    override fun onPause() {
        sendNftViewModel.getMyNftMutableLiveData().removeObservers(this)
        sendNftViewModel.getErrorLiveData().removeObservers(this)
        sendNftViewModel.getInternetLiveData().removeObservers(this)
        sendNftViewModel.getLoadingLiveData().removeObservers(this)
        super.onPause()
    }

    override fun onStop() {
        sendNftViewModel.getMyNftMutableLiveData().removeObservers(this)
        sendNftViewModel.getErrorLiveData().removeObservers(this)
        sendNftViewModel.getInternetLiveData().removeObservers(this)
        sendNftViewModel.getLoadingLiveData().removeObservers(this)
        super.onStop()
    }
    private fun setRecyclerView() {

        myNftAdapter = SendNftAdapter(
            requireContext(), { sendData(it) })

        fragmentSendNftDasboardBinding.recyclerView.adapter = myNftAdapter
        fragmentSendNftDasboardBinding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

    }

    private fun sendData(data: Data) {

        val bundles: Bundle = Bundle().apply {
            putSerializable("data", data)
            putString("key", "Sent")

        }
        sendNftViewModel.getMyNftMutableLiveData().removeObservers(this)
        sendNftViewModel.getErrorLiveData().removeObservers(this)
        sendNftViewModel.getInternetLiveData().removeObservers(this)
        sendNftViewModel.getLoadingLiveData().removeObservers(this)
        findNavController().navigate(
            R.id.action_homeDashboardFragment_to_claimNftFragment,
            bundles
        )
    }


    private fun setListener() {

        fragmentSendNftDasboardBinding.pullToRefresh.setOnRefreshListener {
            sendNftViewModel.getAllDataMyNft()
            fragmentSendNftDasboardBinding.pullToRefresh.isRefreshing = false;
        }

        fragmentSendNftDasboardBinding.createNftCard.setOnClickListener {
            sendNftViewModel.getMyNftMutableLiveData().removeObservers(this)
            sendNftViewModel.getErrorLiveData().removeObservers(this)
            sendNftViewModel.getInternetLiveData().removeObservers(this)
            sendNftViewModel.getLoadingLiveData().removeObservers(this)
            val bundle =Bundle().apply {
                putString("key","false")
            }
            findNavController().navigate(R.id.action_homeDashboardFragment_to_createNftFragment,bundle)

        }

        fragmentSendNftDasboardBinding.sendNftCard.setOnClickListener {
            sendNftViewModel.getMyNftMutableLiveData().removeObservers(this)
            sendNftViewModel.getErrorLiveData().removeObservers(this)
            sendNftViewModel.getInternetLiveData().removeObservers(this)
            sendNftViewModel.getLoadingLiveData().removeObservers(this)

            findNavController().navigate(R.id.action_homeDashboardFragment_to_layoutSentNftFragment)

        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO


}

    
