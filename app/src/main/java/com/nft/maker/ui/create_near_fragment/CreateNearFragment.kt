package com.nft.maker.ui.create_near_fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.findNavController
import com.nft.maker.BaseFragment
import com.nft.maker.R
import com.nft.maker.databinding.FragmentCreatNearBinding
import com.nft.maker.ui.claim_nft_acitivity.ClaimNftActivity
import com.nft.maker.utils.AlertDialogs
import com.nft.maker.utils.ValidationAlert
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

@AndroidEntryPoint
class CreatNearFragment : BaseFragment() {
    private lateinit var fragmentCreateNearBinding: FragmentCreatNearBinding
    lateinit var createNearViewModel: CreateNearViewModel
    var email: String = ""
    var type: String = ""
    var nftId: String = "";
    override val layoutId: View
        get() {
            fragmentCreateNearBinding = FragmentCreatNearBinding.inflate(layoutInflater)
            return fragmentCreateNearBinding.root
        }
    override val layoutTag: String?
        get() = CreatNearFragment::class.java.simpleName

    override fun initUI(view: View) {
        initiate()
        clickListener()
        changeButtonState()
        setObservers()
        onBackPressed()

    }

    fun onBackPressed() {
        view.isFocusableInTouchMode = true;
        view.requestFocus()
        view.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        findNavController().popBackStack(R.id.onBoardingEmailFragment, false)
                        return true
                    }
                }
                return false
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun initiate() {
        createNearViewModel = ViewModelProvider(this).get(CreateNearViewModel::class.java)
        type = arguments?.getString("type", "").toString()
        email = arguments?.getString("email", "").toString()
        nftId = arguments?.getString("nft_id", "") ?: ""

        if (type.equals("phone")) {
            val emailTwo = email.replace("+", "");
            fragmentCreateNearBinding.accountIdField.setText(emailTwo + ".near")
            createNearViewModel.checkAccountID(emailTwo + ".near")

        } else if (type.equals("email")) {
            val parts = email.split("@");
            val part1 = parts[0]; // 004
            val emailTwo = part1.replace(".", "");
            fragmentCreateNearBinding.accountIdField.setText(emailTwo + ".near")
            createNearViewModel.checkAccountID(emailTwo + ".near")


        }
//        fragmentCreateNearBinding.createButton.setTextColor(Color.parseColor("#FFFFFF"))
//        fragmentCreateNearBinding.createButton.setBackground(
//            resources.getDrawable(R.drawable.button_dark_bg)
//        )
//
//        fragmentCreateNearBinding.createButton.isEnabled = true

    }

    private fun clickListener() {
        fragmentCreateNearBinding.createButton.setOnClickListener {
            if (type.equals("phone")) {

                var emails: RequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), email)
                var fullNames: RequestBody =
                    RequestBody.create(
                        "text/plain".toMediaTypeOrNull(),
                        fragmentCreateNearBinding.fullNameField.text.toString()
                    )
                var accountIds: RequestBody =
                    RequestBody.create(
                        "text/plain".toMediaTypeOrNull(),
                        fragmentCreateNearBinding.accountIdField.text.toString()
                    )
                createNearViewModel.registerByNumber(
                    emails,
                    fullNames,
                    accountIds
                )
            } else if (type.equals("email")) {
                var emails: RequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), email)
                var fullNames: RequestBody =
                    RequestBody.create(
                        "text/plain".toMediaTypeOrNull(),
                        fragmentCreateNearBinding.fullNameField.text.toString()
                    )
                var accountIds: RequestBody =
                    RequestBody.create(
                        "text/plain".toMediaTypeOrNull(),
                        fragmentCreateNearBinding.accountIdField.text.toString()
                    )
                createNearViewModel.registerByEmail(
                    emails,
                    fullNames,
                    accountIds
                )
            }

        }
        fragmentCreateNearBinding.accountIdField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    fragmentCreateNearBinding.checkText.visibility = View.VISIBLE
                    createNearViewModel.checkAccountID(s.toString())
                } else {
                    fragmentCreateNearBinding.checkText.visibility = View.GONE
                }

            }
        })


    }

    private fun changeButtonState() {
        val editTexts = listOf(
            fragmentCreateNearBinding.fullNameField,
            fragmentCreateNearBinding.accountIdField
        )
        for (editText in editTexts) {
            editText.addTextChangedListener(
                object : TextWatcher {
                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
//                    if (!TextUtils.isEmpty(fragmentLoginBinding.emailTextField.text.toString()) && !TextUtils.isEmpty(fragmentLoginBinding.emailTextField.text.toString()) && !TextUtils.isEmpty(fragmentLoginBinding.emailTextField.text.toString())) {
//                    fragmentLoginBinding.btnLogin.isEnabled = (s.toString().trim().isNotEmpty());  // set here your backgournd to button
//                    }else {
//                        fragmentLoginBinding.btnLogin.setBackgroundColor(Color.RED)
//                    }
                    }

                    override fun beforeTextChanged(
                        s: CharSequence, start: Int, count: Int,
                        after: Int
                    ) {
                        // TODO Auto-generated method stub
                    }

                    override fun afterTextChanged(s: Editable) {
                        if (!TextUtils.isEmpty(fragmentCreateNearBinding.fullNameField.text.toString()) &&
                            !TextUtils.isEmpty(
                                fragmentCreateNearBinding.accountIdField.text.toString()
                            )
                        ) {
                            fragmentCreateNearBinding.createButton.setTextColor(Color.parseColor("#FFFFFF"))
                            fragmentCreateNearBinding.createButton.setBackground(
                                resources.getDrawable(
                                    R.drawable.button_dark_bg
                                )
                            )

                            fragmentCreateNearBinding.createButton.isEnabled =
                                (s.toString().trim().isNotEmpty());

                            // set here your backgournd to button
                        } else {
                            fragmentCreateNearBinding.createButton.setTextColor(Color.parseColor("#ff7f7f7f"))
                            fragmentCreateNearBinding.createButton.isEnabled = false
                            fragmentCreateNearBinding.createButton.setBackground(
                                resources.getDrawable(
                                    R.drawable.button_light_bg
                                )
                            )


                        }
                    }
                }
            )
        }
    }


    private fun setObservers() {

        createNearViewModel.getInternetLiveData().observe(this, {
            ValidationAlert.getValidationDialog(requireContext(), false, "Alert", it)

        })

        createNearViewModel.getErrorLiveData().observe(this, {
            if (it != null) {
                if (it.equals("Account Id Already Used.")) {
                    fragmentCreateNearBinding.checkText.text =
                        "Account ID is taken. Try something else."
                    fragmentCreateNearBinding.checkText.setTextColor(
                        requireContext().resources.getColor(
                            R.color.red
                        )
                    )
                } else {
                    ValidationAlert.getValidationDialog(requireContext(), false, "Alert", it)


                }

            }
        })

        createNearViewModel.getLoadingLiveData().observe(this, {
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

        createNearViewModel.getLoginMutableLiveData().observe(this, {
            preferenceHelper().userEmail = it.data?.email ?: ""
            preferenceHelper().userId = it.data?.id ?: 0
            preferenceHelper().fullName = it.data?.full_name ?: ""
            preferenceHelper().accountID = it.data?.account_id ?: ""
            if (nftId.isNotEmpty()) {
                val i = Intent(requireActivity(), ClaimNftActivity::class.java)
                i.putExtra("nft_id", nftId)
                startActivity(i)
                requireActivity().finish()

            } else {

                findNavController().navigate(R.id.action_creatNearFragment_to_secureYourAccountFragment)
            }


        })

        createNearViewModel.getCheckAccountMutableLiveData().observe(this, {

            fragmentCreateNearBinding.checkText.text = ""
            fragmentCreateNearBinding.checkText.text =
                "Congrats! " + fragmentCreateNearBinding.accountIdField.text.toString() + " is available"

            fragmentCreateNearBinding.checkText.setTextColor(
                requireContext().resources.getColor(
                    R.color.green
                )
            )


        })
    }

}