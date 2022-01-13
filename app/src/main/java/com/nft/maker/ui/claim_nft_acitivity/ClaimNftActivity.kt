package com.nft.maker.ui.claim_nft_acitivity

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.cardview.widget.CardView
import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log

import android.view.Gravity
import android.view.Window
import android.widget.GridLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.nft.maker.BaseActivity
import com.nft.maker.R
import com.nft.maker.databinding.ActivityClaimNftBinding
import com.nft.maker.databinding.ActivityMainBinding
import com.nft.maker.model.send_nft_mode_dashboard.Data
import com.nft.maker.ui.claim_nft_fragment.ClaimNftViewModel
import com.nft.maker.ui.dasboard_activity.DashBoardActivity
import com.nft.maker.ui.main_activity.MainActivity
import com.nft.maker.ui.on_boarding_email_fragment.OnBoardingEmailFragment
import com.nft.maker.utils.AlertDialogs
import com.nft.maker.utils.Constants
import com.nft.maker.utils.ValidationAlert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClaimNftActivity : BaseActivity() {

    private lateinit var activityClaimNftBinding: ActivityClaimNftBinding
    lateinit var claimActivtiyViewModel: ClaimActivtiyViewModel
    var nftId: String = ""


    override val layoutId: View
        get() {
            activityClaimNftBinding = ActivityClaimNftBinding.inflate(layoutInflater)
            return activityClaimNftBinding.root
        }
    override val tag: String?
        get() = ClaimNftActivity::class.java.simpleName

    override fun created(savedInstance: Bundle?) {

        initiate()
        setListener()
        setObservers()
    }


    private fun initiate() {

        claimActivtiyViewModel = ViewModelProvider(this).get(ClaimActivtiyViewModel::class.java)

        intent?.let {
            nftId = intent.getStringExtra("nft_id") ?: ""
            nftId.let {
                claimActivtiyViewModel.fetchNftDetail(nftId)
            }
        }

    }


    private fun setListener() {
        activityClaimNftBinding.claimNftButton.setOnClickListener {
            if (preferenceHelper.authToken.isNotEmpty()) {
                val user_id = preferenceHelper.userId
                if (nftId.isNotEmpty()) {
                    claimActivtiyViewModel.claimNftImage(user_id, nftId, this)
                }
            } else {
                showAuthDialog()
            }


        }

    }

    private fun setObservers() {
        claimActivtiyViewModel.nftLiveData().observe(this, {
            it.data?.let { it1 ->
                it1.image?.isNotEmpty()?.let { empty ->
                    if (empty)
                        Glide.with(this).load(it1.image).into(activityClaimNftBinding.previewImage)
                }
                activityClaimNftBinding.titleText.text = it1.name
                activityClaimNftBinding.categoryName.text = it1.category
                activityClaimNftBinding.description.text = it1.description
                activityClaimNftBinding.text7.text = it1.explorer_url
                activityClaimNftBinding.tokenValue.text = it1.token_id
                activityClaimNftBinding.tvAddressValue.text = it1.explorer_url
                if(it1.is_nft_claimed){
                    activityClaimNftBinding.claimNftButton.visibility=View.GONE

                }else{
                    activityClaimNftBinding.claimNftButton.visibility=View.VISIBLE

                }

            }
        })


        claimActivtiyViewModel.getInternetLiveData().observe(this, {
            ValidationAlert.getValidationDialog(this, false, "Alert", it)

        })

        claimActivtiyViewModel.getErrorLiveData().observe(this, {
            ValidationAlert.getValidationDialog(this, false, "Alert", it)
        })


        claimActivtiyViewModel.getLoadingLiveData().observe(this, {
            if (it)
                alertDialog = AlertDialogs.getProgressDialog(this, true)
            else {
                if (isAlertDialogInitialized())
                    alertDialog.dismiss()
            }
        })

        claimActivtiyViewModel.getClaimMutableLiveData().observe(this, {

            val i = Intent(this, DashBoardActivity::class.java)
            startActivity(i)
            finish()


        })
    }

    private fun showAuthDialog() {

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.authentication_dialog_layout, null)
        dialogBuilder.setView(dialogView)

        val dialog = dialogBuilder.create()
        val window: Window? = dialog.window
        window?.setGravity(Gravity.BOTTOM)
        window?.setLayout(GridLayout.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        dialog.setTitle(null)
        dialog.setContentView(R.layout.authentication_dialog_layout)
        dialog.setCancelable(true)
        dialog.show()
        dialog.setCanceledOnTouchOutside(true)

        val loginBtn = dialogView.findViewById<TextView>(R.id.login)
        val createBtn = dialogView.findViewById<TextView>(R.id.create_account)

        loginBtn.setOnClickListener {
            dialog.dismiss()
            val i = Intent(this, MainActivity::class.java)
            Log.d("nft id",nftId)
            i.putExtra("nft_id", nftId)
            startActivity(i)
            finish()
        }

        createBtn.setOnClickListener {
            dialog.dismiss()
            Log.d("nft id",nftId)
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("nft_id", nftId)
            startActivity(i)
            finish()
        }


    }
}