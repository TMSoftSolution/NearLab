package com.nft.maker.ui.layout_sent_nft_fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nft.maker.BaseFragment
import com.nft.maker.R
import com.nft.maker.databinding.FragmentLayoutSentNftBinding
import com.nft.maker.ui.recieved_nft_dasboard_fragment.RecievedNftDasboardFragment
import com.nft.maker.utils.AlertDialogs
import com.nft.maker.utils.ValidationAlert
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class LayoutSentNftFragment : BaseFragment(), CoroutineScope {
    private lateinit var fragmentLayoutSentNftBinding: FragmentLayoutSentNftBinding
    private lateinit var sendNftViewModel: SendNftViewModel
    private lateinit var sendNftAdapter: LayoutSendNftAdapter


    override val layoutId: View
        get() {
            fragmentLayoutSentNftBinding =
                FragmentLayoutSentNftBinding.inflate(layoutInflater)
            return fragmentLayoutSentNftBinding.root
        }
    override val layoutTag: String?
        get() = RecievedNftDasboardFragment::class.java.simpleName

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
            if (it)
                setAlertDialog(AlertDialogs.getProgressDialog(requireContext(), false)) else {
                if (isAlertInitialized())
                    getAlertDialog().dismiss()
            }
        })

        sendNftViewModel.getMyNftMutableLiveData().observe(this, {
            if (isAlertInitialized()) {
                getAlertDialog().dismiss()

            }
            if (it.data.isNotEmpty()) {
                fragmentLayoutSentNftBinding.recyclerView.visibility = View.VISIBLE
                fragmentLayoutSentNftBinding.noRecord.visibility = View.GONE
                sendNftAdapter.submitList(it.data)
            } else {
                fragmentLayoutSentNftBinding.recyclerView.visibility = View.INVISIBLE
                fragmentLayoutSentNftBinding.noRecord.visibility = View.VISIBLE

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

        sendNftAdapter = LayoutSendNftAdapter(
            requireContext()
        )

        fragmentLayoutSentNftBinding.recyclerView.adapter = sendNftAdapter
        fragmentLayoutSentNftBinding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

    }


    private fun setListener() {
        fragmentLayoutSentNftBinding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        fragmentLayoutSentNftBinding.blackButton.setOnClickListener {
            sendNftViewModel.getMyNftMutableLiveData().removeObservers(this)
            sendNftViewModel.getErrorLiveData().removeObservers(this)
            sendNftViewModel.getInternetLiveData().removeObservers(this)
            sendNftViewModel.getLoadingLiveData().removeObservers(this)
            if (sendNftAdapter.getSelected() == null) {
                ValidationAlert.getValidationDialog(
                    requireContext(),
                    false,
                    "Alert",
                    "Please select NFT"
                )

            } else {
                val bundle=Bundle().apply {
                    putSerializable("data", sendNftAdapter.getSelected())
                    putString("key","SendNft")
                }
                findNavController().navigate(
                    R.id.action_layoutSentNftFragment_to_sendUserNftFragment,
                    bundle
                )

            }

        }


    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO


}


