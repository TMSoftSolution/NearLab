package com.nft.maker.ui.on_boarding_email_fragment

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nft.maker.BaseFragment
import com.nft.maker.R
import com.nft.maker.databinding.FragmentOnBoardingEmailBinding
import com.nft.maker.ui.claim_nft_acitivity.ClaimNftActivity
import com.nft.maker.ui.dasboard_activity.DashBoardActivity
import com.nft.maker.utils.AlertDialogs
import com.nft.maker.utils.Constants
import com.nft.maker.utils.ValidationAlert
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class OnBoardingEmailFragment : BaseFragment() {
    private lateinit var fragmentOnBoardingEmailBinding: FragmentOnBoardingEmailBinding
    lateinit var loginViewModel: SignUpViewModel
    var nftId: String = ""


    override val layoutId: View
        get() {
            fragmentOnBoardingEmailBinding = FragmentOnBoardingEmailBinding.inflate(layoutInflater)
            return fragmentOnBoardingEmailBinding.root
        }
    override val layoutTag: String?
        get() = OnBoardingEmailFragment::class.java.simpleName

    override fun initUI(view: View) {

        initiate()
        clickListener()
        setObservers()

    }

    private fun initiate() {
        loginViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        nftId= mBaseActivity?.intent?.getStringExtra ("nft_id") ?: ""



    }

    private fun clickListener() {
        fragmentOnBoardingEmailBinding.view15.setOnClickListener {
            if (fragmentOnBoardingEmailBinding.emailTextField.hint.equals("Email Address")) {

                if (TextUtils.isEmpty(fragmentOnBoardingEmailBinding.emailTextField.text.toString())) {
                    ValidationAlert.getValidationDialog(
                        requireContext(), false,
                        "Alert", "Please enter email!"
                    )
                } else if (!Constants.isValidEmailId(fragmentOnBoardingEmailBinding.emailTextField.text.toString())) {
                    ValidationAlert.getValidationDialog(
                        requireContext(), false,
                        "Alert", "Please enter valid email!"
                    )
                } else {
                    val bundles: Bundle = Bundle().apply {
                        putString(
                            "email",
                            fragmentOnBoardingEmailBinding.emailTextField.text.toString()
                        )
                        putString("type", "email")
                        putString("nft_id", nftId)


                    }
                    findNavController().navigate(
                        R.id.action_onBoardingEmailFragment_to_creatNearFragment,
                        bundles
                    )
                    fragmentOnBoardingEmailBinding.emailTextField.setText("")



                }
            } else if (fragmentOnBoardingEmailBinding.emailTextField.hint.equals("Phone")) {
                if (TextUtils.isEmpty(fragmentOnBoardingEmailBinding.emailTextField.text.toString())) {
                    ValidationAlert.getValidationDialog(
                        requireContext(), false,
                        "Alert", "Please enter phone!"
                    )
                } else {
                    val bundles: Bundle = Bundle().apply {
                        putString(
                            "email",
                            fragmentOnBoardingEmailBinding.emailTextField.text.toString()
                        )
                        putString("type", "phone")
                        putString("nft_id", nftId)

                    }
                    findNavController().navigate(
                        R.id.action_onBoardingEmailFragment_to_creatNearFragment,
                        bundles
                    )
                    fragmentOnBoardingEmailBinding.emailTextField.setText("")

                }
            }

        }
        fragmentOnBoardingEmailBinding.onBoardEmail.setOnClickListener {
            fragmentOnBoardingEmailBinding.emailTextField.inputType =
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

            fragmentOnBoardingEmailBinding.onBoardEmail.background =
                resources.getDrawable(R.drawable.login_bacground)
            fragmentOnBoardingEmailBinding.onBoardPhone.background = null

            fragmentOnBoardingEmailBinding.emailTextField.hint = "Email Address"
        }

        fragmentOnBoardingEmailBinding.onBoardPhone.setOnClickListener {
            fragmentOnBoardingEmailBinding.emailTextField.inputType = InputType.TYPE_CLASS_PHONE

            fragmentOnBoardingEmailBinding.onBoardPhone.background =
                resources.getDrawable(R.drawable.login_bacground)
            fragmentOnBoardingEmailBinding.onBoardEmail.background = null
            fragmentOnBoardingEmailBinding.emailTextField.hint = "Phone"

        }
    }

    private fun setObservers() {

        loginViewModel.getInternetLiveData().observe(this, {
            ValidationAlert.getValidationDialog(requireContext(), false, "Alert", it)

        })

        loginViewModel.getErrorLiveData().observe(this, {

        })

        loginViewModel.getLoadingLiveData().observe(this, {
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

        loginViewModel.getLoginMutableLiveData().observe(this, {

            preferenceHelper().userEmail = it.data.email ?: ""
            preferenceHelper().userId = it.data.id?: 0
            preferenceHelper().fullName = it.data.full_name ?: ""
            preferenceHelper().accountID = it.data.account_id ?: ""
            if (!nftId.equals("null")) {
                val i = Intent(requireActivity(), ClaimNftActivity::class.java)
                i.putExtra("nft_id", nftId)
                startActivity(i)
                requireActivity().finish()
            } else {
                val i = Intent(requireContext(), DashBoardActivity::class.java)
                startActivity(i)
                requireActivity().finish()
            }


        })
    }

//    override val coroutineContext: CoroutineContext
//        get() = Dispatchers.IO


}