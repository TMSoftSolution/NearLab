package com.nft.maker.ui.dasboard_activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.nft.maker.BaseActivity
import com.nft.maker.R
import com.nft.maker.databinding.ActivityDashBoardBinding
import com.nft.maker.utils.ValidationAlert
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nft.maker.ui.claim_nft_acitivity.ClaimNftActivity

class DashBoardActivity : BaseActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var activityDashBoardBinding: ActivityDashBoardBinding

    override val layoutId: View
        get() {
            activityDashBoardBinding = ActivityDashBoardBinding.inflate(layoutInflater)
            return activityDashBoardBinding.root
        }
    override val tag: String?
        get() = DashBoardActivity::class.java.simpleName

    override fun created(savedInstance: Bundle?) {
        init()
        checkPermissions()
    }


    private fun init() {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostContainer) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeDashboardFragment,

//                R.id.callDialerFragment,
//                R.id.chatContactScreenFragment,
//                R.id.accountScreenFragment
            )
        )

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//
//            if (destination.id == R.id.textSendFragment || destination.id == R.id.callingScreenFragment2 || destination.id == R.id.textMessageFragmentmain || destination.id == R.id.composeMessageFragment2) {
//
//                activityMainScreenLayoutBinding.bottomNavigationView.visibility = View.GONE
//            } else {
//
//                activityMainScreenLayoutBinding.bottomNavigationView.visibility = View.VISIBLE
//            }
//        }
    }


    private fun checkPermissions() {
        Dexter.withContext(this)
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
                            this@DashBoardActivity,
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