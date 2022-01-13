package com.nft.maker.ui.recieved_nft_dasboard_fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nft.maker.BaseFragment
import com.nft.maker.R
import com.nft.maker.databinding.FragmentRecievedNftDasboardBinding
import com.nft.maker.model.send_nft_mode_dashboard.Data
import com.nft.maker.utils.AlertDialogs
import com.nft.maker.utils.ValidationAlert
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
@AndroidEntryPoint
class RecievedNftDasboardFragment : BaseFragment(),CoroutineScope {
    private lateinit var receiveNftViewModel: ReceiveNftViewModel
    private lateinit var receivedNftAdapter: ReceivedNftAdapter
    private lateinit var fragmentRecievedNftDasboardBinding: FragmentRecievedNftDasboardBinding
    override val layoutId: View
        get() {
            fragmentRecievedNftDasboardBinding =
                FragmentRecievedNftDasboardBinding.inflate(layoutInflater)
            return fragmentRecievedNftDasboardBinding.root
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
        receiveNftViewModel = ViewModelProvider(this).get(ReceiveNftViewModel::class.java)

    }


    private fun setObservers() {
        receiveNftViewModel.getAllDataMyNft()

        receiveNftViewModel.getInternetLiveData().observe(this, {
            ValidationAlert.getValidationDialog(requireContext(), false, "Alert", it)

        })

        receiveNftViewModel.getErrorLiveData().observe(this, {
            ValidationAlert.getValidationDialog(requireContext(), false, "Alert", it)
        })


        receiveNftViewModel.getLoadingLiveData().observe(this, {
            if (it && mBaseActivity?.isAlertDialogInitialized() == false)
                setAlertDialog(AlertDialogs.getProgressDialog(requireContext(), false)) else {
                if (isAlertInitialized()) getAlertDialog().dismiss()
            }
        })

        receiveNftViewModel.getMyNftMutableLiveData().observe(this, {

            if (it.data.isNotEmpty()) {
                fragmentRecievedNftDasboardBinding.recyclerView.visibility=View.VISIBLE
                fragmentRecievedNftDasboardBinding.noRecord.visibility=View.GONE
                receivedNftAdapter.submitList(it.data)
            }else{
                fragmentRecievedNftDasboardBinding.recyclerView.visibility=View.GONE
                fragmentRecievedNftDasboardBinding.noRecord.visibility=View.VISIBLE

            }

        })
    }

    override fun onPause() {
        receiveNftViewModel.getMyNftMutableLiveData().removeObservers(this)
        receiveNftViewModel.getErrorLiveData().removeObservers(this)
        receiveNftViewModel.getInternetLiveData().removeObservers(this)
        receiveNftViewModel.getLoadingLiveData().removeObservers(this)
        super.onPause()
    }

    override fun onStop() {
        receiveNftViewModel.getMyNftMutableLiveData().removeObservers(this)
        receiveNftViewModel.getErrorLiveData().removeObservers(this)
        receiveNftViewModel.getInternetLiveData().removeObservers(this)
        receiveNftViewModel.getLoadingLiveData().removeObservers(this)
        super.onStop()
    }
    private fun setRecyclerView() {

        receivedNftAdapter = ReceivedNftAdapter(
            requireContext(), {
                sendData(it)

            },

            )

        fragmentRecievedNftDasboardBinding.recyclerView.adapter = receivedNftAdapter
        fragmentRecievedNftDasboardBinding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

    }

    private fun sendData(data: Data) {

        val bundles: Bundle = Bundle().apply {
            putSerializable("data", data)
            putString("key", "Receive")

        }
        receiveNftViewModel.getMyNftMutableLiveData().removeObservers(this)
        receiveNftViewModel.getErrorLiveData().removeObservers(this)
        receiveNftViewModel.getInternetLiveData().removeObservers(this)
        receiveNftViewModel.getLoadingLiveData().removeObservers(this)
        findNavController().navigate(
            R.id.action_homeDashboardFragment_to_claimNftFragment,
            bundles
        )
    }


    private fun setListener() {

        fragmentRecievedNftDasboardBinding.pullToRefresh.setOnRefreshListener {
            receiveNftViewModel.getAllDataMyNft()
            fragmentRecievedNftDasboardBinding.pullToRefresh.isRefreshing = false;
        }


        fragmentRecievedNftDasboardBinding.createNftCard.setOnClickListener {
            receiveNftViewModel.getMyNftMutableLiveData().removeObservers(this)
            receiveNftViewModel.getErrorLiveData().removeObservers(this)
            receiveNftViewModel.getInternetLiveData().removeObservers(this)
            receiveNftViewModel.getLoadingLiveData().removeObservers(this)
            val bundle =Bundle().apply {
                putString("key","false")
            }
            findNavController().navigate(R.id.action_homeDashboardFragment_to_createNftFragment,bundle)

        }

        fragmentRecievedNftDasboardBinding.sendNftCard.setOnClickListener {
            receiveNftViewModel.getMyNftMutableLiveData().removeObservers(this)
            receiveNftViewModel.getErrorLiveData().removeObservers(this)
            receiveNftViewModel.getInternetLiveData().removeObservers(this)
            receiveNftViewModel.getLoadingLiveData().removeObservers(this)

            findNavController().navigate(R.id.action_homeDashboardFragment_to_layoutSentNftFragment)

        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO


}