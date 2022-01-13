package com.nft.maker.ui.claim_nft_fragment

import android.content.Intent
import android.view.View
import com.nft.maker.BaseFragment
import com.nft.maker.databinding.FragmentClaimNftBinding
import com.nft.maker.ui.create_nft_fragment.CreateNftFragment
import com.nft.maker.ui.dasboard_activity.DashBoardActivity

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nft.maker.utils.AlertDialogs
import com.nft.maker.utils.ValidationAlert
import com.bumptech.glide.Glide
import com.nft.maker.model.send_nft_mode_dashboard.Data
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClaimNftFragment : BaseFragment() {
    private lateinit var fragmentClaimNftBinding: FragmentClaimNftBinding
    lateinit var claimNftViewModel: ClaimNftViewModel
    lateinit var data: Data
    override val layoutId: View
        get() {
            fragmentClaimNftBinding =
                FragmentClaimNftBinding.inflate(layoutInflater)
            return fragmentClaimNftBinding.root
        }
    override val layoutTag: String?
        get() = CreateNftFragment::class.java.simpleName

    override fun initUI(view: View) {
        initiate()
        setListener()
        setObservers()
    }


    private fun initiate() {
        claimNftViewModel = ViewModelProvider(this).get(ClaimNftViewModel::class.java)
      val  key = arguments?.getString("key")
        if (key.equals("My")){
            data = arguments?.getSerializable("data") as Data
            fragmentClaimNftBinding.claimNftButton.visibility=View.GONE

        }else if (key.equals("Receive")){
            data = arguments?.getSerializable("data") as Data
            fragmentClaimNftBinding.claimNftButton.visibility=View.VISIBLE
            if (data.is_nft_claimed) {
                fragmentClaimNftBinding.claimNftButton.visibility = View.GONE
            } else {
                fragmentClaimNftBinding.claimNftButton.visibility = View.VISIBLE

            }


        }
        else if (key.equals("Sent")){
            data = arguments?.getSerializable("data") as Data
            fragmentClaimNftBinding.claimNftButton.visibility=View.GONE


        }
        fragmentClaimNftBinding.titleText.text = data.name
        fragmentClaimNftBinding.tokenValue.text = data.token_id
        fragmentClaimNftBinding.tvAddressValue.text = data.explorer_url
        fragmentClaimNftBinding.description.text = data.description
        fragmentClaimNftBinding.personName.text = data.sender.full_name
        fragmentClaimNftBinding.text7.text = data.explorer_url
        Glide.with(requireActivity())
            .load(data.image)
            .into(fragmentClaimNftBinding.previewImage)


    }


    private fun setListener() {
        fragmentClaimNftBinding.claimNftButton.setOnClickListener {
            val user_id = preferenceHelper().userId
            val uuid = data.uuid
            claimNftViewModel.claimNftImage(user_id, uuid, requireContext())

        }
        fragmentClaimNftBinding.backButton.setOnClickListener {
           findNavController().popBackStack()

        }

    }

    private fun setObservers() {


        claimNftViewModel.getInternetLiveData().observe(this, {
            ValidationAlert.getValidationDialog(requireContext(), false, "Alert", it)

        })

        claimNftViewModel.getErrorLiveData().observe(this, {
            ValidationAlert.getValidationDialog(requireContext(), false, "Alert", it)
        })


        claimNftViewModel.getLoadingLiveData().observe(this, {
            if (it)
                setAlertDialog(AlertDialogs.getProgressDialog(requireContext(), true))
            else {
                if (isAlertInitialized())
                    getAlertDialog().dismiss()
            }
        })

        claimNftViewModel.getClaimMutableLiveData().observe(this, {

            val i = Intent(requireContext(), DashBoardActivity::class.java)
            startActivity(i)
            requireActivity().finish()


        })
    }

}