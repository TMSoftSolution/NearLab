package com.nft.maker.ui.preview_nft_fragment

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.core.view.isVisible
import com.nft.maker.BaseFragment
import com.nft.maker.databinding.FragmentPreviewNftBinding
import com.nft.maker.ui.dasboard_activity.DashBoardActivity
import com.nft.maker.ui.my_nft_dashboard_fragment.MyNftDashboardFragment
import com.bumptech.glide.Glide

import com.nft.maker.utils.AlertDialogs
import com.nft.maker.utils.ValidationAlert
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nft.maker.R
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.*


@AndroidEntryPoint
class PreviewNftFragment : BaseFragment() {
    lateinit var previewNftViewModel: PreviewNftViewModel
    private lateinit var fragmentPreviewNftBinding: FragmentPreviewNftBinding
    var key: String? = ""
    var onClickKey: String? = ""
    override val layoutId: View
        get() {
            fragmentPreviewNftBinding =
                FragmentPreviewNftBinding.inflate(layoutInflater)
            return fragmentPreviewNftBinding.root
        }

    override val layoutTag: String?
        get() = MyNftDashboardFragment::class.java.simpleName

    override fun initUI(view: View) {
        initiate()
        checkPermissions()
        setListener()
        setObservers()
        onBackPressed()

    }

    private fun initiate() {
        previewNftViewModel = ViewModelProvider(this).get(PreviewNftViewModel::class.java)

    }
    fun onBackPressed() {
        fragmentPreviewNftBinding.backButton.setOnClickListener {
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


    private fun setListener() {
        key = arguments?.getString("key")
        val image = arguments?.getString("image", "")!!
        val tittle = arguments?.getString("tittle", "")!!
        val description = arguments?.getString("description", "")!!
        fragmentPreviewNftBinding.titleText.text = tittle
        fragmentPreviewNftBinding.description.text = description
        Glide.with(requireActivity())
            .load(image)
            .into(fragmentPreviewNftBinding.previewImage)
        if (key == "true") {
            fragmentPreviewNftBinding.cardNft.visibility = View.GONE
        } else {
            fragmentPreviewNftBinding.cardNft.visibility = View.VISIBLE

        }
        fragmentPreviewNftBinding.cardNft.setOnClickListener {
            onClickKey="Send"
            previewNftViewModel.sendPreviewData(
                tittle,
                image,
                description,
                "Digitals Arts",
                requireContext(),
                "false"
            )


        }

        fragmentPreviewNftBinding.card1.setOnClickListener {
            onClickKey="Mine"
            if (key == "true") {
                previewNftViewModel.sendPreviewData(
                    tittle,
                    image,
                    description,
                    "Digital Arts",
                    requireContext(),
                    "true"
                )

            } else {
                previewNftViewModel.sendPreviewData(
                    tittle,
                    image,
                    description,
                    "Digitals Arts",
                    requireContext(),
                    "false"
                )

            }


        }

    }

    private fun setObservers() {

        previewNftViewModel.getInternetLiveData().observe(this, {
            ValidationAlert.getValidationDialog(requireContext(), false, "Alert", it)

        })

        previewNftViewModel.getErrorLiveData().observe(this, {
            if (it != null) {
                ValidationAlert.getValidationDialog(requireContext(), false, "Alert", it)

            }
        })


        previewNftViewModel.getLoadingLiveData().observe(this, {
            if (it)
                setAlertDialog(AlertDialogs.getProgressDialog(requireContext(), true))
            else {
                if (isAlertInitialized())
                    getAlertDialog().dismiss()
            }
        })

        previewNftViewModel.getPreviewMutableLiveData().observe(this, {

            if (key == "true") {
                val i = Intent(requireContext(), DashBoardActivity::class.java)
                startActivity(i)
                requireActivity().finish()

            } else {
                if (onClickKey.equals("Mine")) {
                    val i = Intent(requireContext(), DashBoardActivity::class.java)
                    startActivity(i)
                    requireActivity().finish()

                }else if (onClickKey.equals("Send")){
                    val bundle=Bundle().apply {
                        putString("uuid",it.data.uuid)
                        putString("key","preview")
                    }
                    findNavController().navigate(R.id.action_previewNftFragment_to_sendUserNftFragment,bundle)

                }

            }


        })
    }

    private fun checkPermissions() {
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                    } else if (report.isAnyPermissionPermanentlyDenied) {
                        ValidationAlert.getValidationDialog(
                            requireContext(),
                            false,
                            "Alert",
                            "Permissions are required to continue"
                        )

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

}


