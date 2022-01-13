package com.nft.maker.ui.secure_your_fragmet

import android.content.Intent
import android.graphics.Color
import android.view.KeyEvent
import android.view.View
import androidx.navigation.fragment.findNavController
import com.nft.maker.BaseFragment
import com.nft.maker.R
import com.nft.maker.databinding.FragmentSecureYourAccountBinding
import com.nft.maker.ui.dasboard_activity.DashBoardActivity

class SecureYourAccountFragment : BaseFragment() {
    private lateinit var fragmentSecureYourAccountBinding: FragmentSecureYourAccountBinding
    override val layoutId: View
        get() {
            fragmentSecureYourAccountBinding =
                FragmentSecureYourAccountBinding.inflate(layoutInflater)
            return fragmentSecureYourAccountBinding.root
        }
    override val layoutTag: String?
        get() = SecureYourAccountFragment::class.java.simpleName

    override fun initUI(view: View) {
        initiate()
        setListener()
        onBackPressed()
    }

    private fun initiate() {


    }

    private fun setListener() {

        fragmentSecureYourAccountBinding.phoneBox.setOnClickListener {
            fragmentSecureYourAccountBinding.continueButton.setTextColor(Color.parseColor("#FFFFFF"))
            fragmentSecureYourAccountBinding.continueButton.setBackground(
                resources.getDrawable(
                    R.drawable.button_dark_bg
                )
            )
            fragmentSecureYourAccountBinding.continueButton.isEnabled = true
            fragmentSecureYourAccountBinding.phoneBox.setBackground(resources.getDrawable(R.drawable._rectangle))
            fragmentSecureYourAccountBinding.emailBox.setBackground(resources.getDrawable(R.drawable._rectangle_105))

        }
        fragmentSecureYourAccountBinding.emailBox.setOnClickListener {
            fragmentSecureYourAccountBinding.continueButton.setTextColor(Color.parseColor("#FFFFFF"))
            fragmentSecureYourAccountBinding.continueButton.setBackground(
                resources.getDrawable(
                    R.drawable.button_dark_bg
                )
            )
            fragmentSecureYourAccountBinding.continueButton.isEnabled = true
            fragmentSecureYourAccountBinding.emailBox.setBackground(resources.getDrawable(R.drawable._rectangle))
            fragmentSecureYourAccountBinding.phoneBox.setBackground(resources.getDrawable(R.drawable._rectangle_105))

        }
        fragmentSecureYourAccountBinding.continueButton.setOnClickListener {

            findNavController().navigate(R.id.action_secureYourAccountFragment_to_giftAndNftFragment)

        }
    }

    fun onBackPressed() {
//        fragmentGiftAndNftBinding.backButton.setOnClickListener {
//            findNavController().popBackStack()
//
//        }
        view.isFocusableInTouchMode = true;
        view.requestFocus()
        view.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        findNavController().popBackStack(R.id.creatNearFragment,false)
                        return true
                    }
                }
                return false
            }
        })
    }



}