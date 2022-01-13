package com.nft.maker.ui.my_nft_dashboard_fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nft.maker.BaseFragment
import com.nft.maker.R
import com.nft.maker.databinding.FragmentMyNftDashboardBinding
import com.nft.maker.model.Data
import com.nft.maker.utils.AlertDialogs
import com.nft.maker.utils.ValidationAlert
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class MyNftDashboardFragment : BaseFragment(), CoroutineScope {
    private lateinit var fragmentMyNftDashboardFragmentBindings: FragmentMyNftDashboardBinding
    private lateinit var myNftViewModel: MyNftViewModel
    private lateinit var myNftAdapter: MyNftAdapter

    override val layoutId: View
        get() {
            fragmentMyNftDashboardFragmentBindings =
                FragmentMyNftDashboardBinding.inflate(layoutInflater)
            return fragmentMyNftDashboardFragmentBindings.root
        }

    override val layoutTag: String?
        get() = MyNftDashboardFragment::class.java.simpleName

    override fun initUI(view: View) {
        initiate()
        setRecyclerView()
        setListener()
        setObservers()

    }


    private fun initiate() {
        myNftViewModel = ViewModelProvider(this).get(MyNftViewModel::class.java)

    }


    private fun setObservers() {
        myNftViewModel.getAllDataMyNft()

        myNftViewModel.getInternetLiveData().observe(this, {
            ValidationAlert.getValidationDialog(requireContext(), false, "Alert", it)

        })

        myNftViewModel.getErrorLiveData().observe(this, {
            ValidationAlert.getValidationDialog(requireContext(), false, "Alert", it)
        })


        myNftViewModel.getLoadingLiveData().observe(this, {
            if (it)
                setAlertDialog(AlertDialogs.getProgressDialog(requireContext(), false)) else {
                if (isAlertInitialized())
                    getAlertDialog().dismiss()
            }
        })

        myNftViewModel.getMyNftMutableLiveData().observe(this, {
            if (it.data.isNotEmpty()) {
                fragmentMyNftDashboardFragmentBindings.recyclerView.visibility = View.VISIBLE
                fragmentMyNftDashboardFragmentBindings.noRecord.visibility = View.GONE
                myNftAdapter.submitList(it.data)
            } else {
                fragmentMyNftDashboardFragmentBindings.recyclerView.visibility = View.GONE
                fragmentMyNftDashboardFragmentBindings.noRecord.visibility = View.VISIBLE

            }

        })
    }

    override fun onPause() {
        myNftViewModel.getMyNftMutableLiveData().removeObservers(this)
        myNftViewModel.getErrorLiveData().removeObservers(this)
        myNftViewModel.getInternetLiveData().removeObservers(this)
        myNftViewModel.getLoadingLiveData().removeObservers(this)
        super.onPause()
    }

    override fun onStop() {
        myNftViewModel.getMyNftMutableLiveData().removeObservers(this)
        myNftViewModel.getErrorLiveData().removeObservers(this)
        myNftViewModel.getInternetLiveData().removeObservers(this)
        myNftViewModel.getLoadingLiveData().removeObservers(this)
        super.onStop()
    }

    private fun setRecyclerView() {

        myNftAdapter = MyNftAdapter(
            requireContext(),
            {
                sendData(it)

            },

            )

        fragmentMyNftDashboardFragmentBindings.recyclerView.adapter = myNftAdapter
        fragmentMyNftDashboardFragmentBindings.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

    }

    private fun sendData(data: Data) {

        val bundles: Bundle = Bundle().apply {
            putSerializable("data", data)
            putString("key", "My")
        }
        myNftViewModel.getMyNftMutableLiveData().removeObservers(this)
        myNftViewModel.getErrorLiveData().removeObservers(this)
        myNftViewModel.getInternetLiveData().removeObservers(this)
        myNftViewModel.getLoadingLiveData().removeObservers(this)
//        findNavController().navigate(
//            R.id.action_homeDashboardFragment_to_claimNftFragment,
//            bundles
//        )
    }


    private fun setListener() {
        fragmentMyNftDashboardFragmentBindings.createNftCard.setOnClickListener {
            myNftViewModel.getMyNftMutableLiveData().removeObservers(this)
            myNftViewModel.getErrorLiveData().removeObservers(this)
            myNftViewModel.getInternetLiveData().removeObservers(this)
            myNftViewModel.getLoadingLiveData().removeObservers(this)
            val bundle = Bundle().apply {
                putString("key", "false")
            }
            findNavController().navigate(
                R.id.action_homeDashboardFragment_to_createNftFragment,
                bundle
            )

        }

        fragmentMyNftDashboardFragmentBindings.pullToRefresh.setOnRefreshListener {
            myNftViewModel.getAllDataMyNft()
            fragmentMyNftDashboardFragmentBindings.pullToRefresh.isRefreshing = false;
        }

        fragmentMyNftDashboardFragmentBindings.sendNftCard.setOnClickListener {
            myNftViewModel.getMyNftMutableLiveData().removeObservers(this)
            myNftViewModel.getErrorLiveData().removeObservers(this)
            myNftViewModel.getInternetLiveData().removeObservers(this)
            myNftViewModel.getLoadingLiveData().removeObservers(this)

            findNavController().navigate(R.id.action_homeDashboardFragment_to_layoutSentNftFragment)

        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO


}
