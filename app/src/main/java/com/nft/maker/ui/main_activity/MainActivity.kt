package com.nft.maker.ui.main_activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.nft.maker.BaseActivity
import com.nft.maker.R
import com.nft.maker.databinding.ActivityMainBinding
import com.nft.maker.ui.claim_nft_acitivity.ClaimNftActivity
import com.nft.maker.ui.dasboard_activity.DashBoardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    lateinit var navController: NavController


    override val layoutId: View
        get() {
            activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
            return activityMainBinding.root
        }
    override val tag: String?
        get() = MainActivity::class.java.simpleName

    override fun created(savedInstance: Bundle?) {

        val intent = intent
        val action = intent.action
        val data: Uri? = intent.data


        if (data.toString().startsWith("https://nft-maker-adac1.web.app/nft/detail/claim")) {
            val nftId: String? = getIntent().data?.lastPathSegment
            if (nftId != null) {
                val i = Intent(this, ClaimNftActivity::class.java)
                i.putExtra("nft_id", nftId)
                startActivity(i)
                finish()
            }
        }else{
            if (preferenceHelper.authToken.isNotEmpty()) {
                val i = Intent(this, DashBoardActivity::class.java)
                startActivity(i)
                finish()
            }
        }
        init()
    }

    private fun init() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }


}