package com.nft.maker.ui.enter_verification_code_fragment

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import com.nft.maker.BaseFragment
import com.nft.maker.databinding.FragmentEnterVerificationCodeBinding
import com.nft.maker.R


class EnterVerificationCodeFragment : BaseFragment() {
    private lateinit var fragmentEnterVerificationCodeBinding: FragmentEnterVerificationCodeBinding
    override val layoutId: View
        get() {
            fragmentEnterVerificationCodeBinding =
                FragmentEnterVerificationCodeBinding.inflate(layoutInflater)
            return fragmentEnterVerificationCodeBinding.root
        }
    override val layoutTag: String?
        get() = EnterVerificationCodeFragment::class.java.simpleName

    override fun initUI(view: View) {

        setClickListener()
    }

    private fun setClickListener() {

        fragmentEnterVerificationCodeBinding.vcSquare.setOnTextChangedListener {
            if (!TextUtils.isEmpty(it)
            ) {
                fragmentEnterVerificationCodeBinding.verifyButton.setTextColor(Color.parseColor("#FFFFFF"))
                fragmentEnterVerificationCodeBinding.verifyButton.setBackground(resources.getDrawable(R.drawable.button_dark_bg))

                fragmentEnterVerificationCodeBinding.verifyButton.isEnabled = true

                // set here your backgournd to button
            }
            else {
                fragmentEnterVerificationCodeBinding.verifyButton.setTextColor(Color.parseColor("#ff7f7f7f"))
                fragmentEnterVerificationCodeBinding.verifyButton.isEnabled = false
                fragmentEnterVerificationCodeBinding.verifyButton.setBackground(resources.getDrawable(R.drawable.button_light_bg))


            }
        }

//        fragmentEnterVerificationCodeBinding.verifyButton.setOnClickListener {
//            findNavController().navigate(R.id.action_enterVerificationCodeFragment_to_creatNearFragment)
//
//        }
    }

}