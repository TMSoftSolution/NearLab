package com.nft.maker

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.multidex.BuildConfig
import com.nft.maker.utils.AlertDialogs
import com.nft.maker.utils.Constants
import com.nft.maker.utils.Constants.CLICK_POSITIVE
import com.nft.maker.utils.NetworkManager
import com.nft.maker.utils.PreferenceHelper
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.qifan.powerpermission.data.hasAllGranted
import com.qifan.powerpermission.data.hasPermanentDenied
import com.qifan.powerpermission.data.hasRational
import com.qifan.powerpermission.askPermissions

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity(){
    // Getters/Setters

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @Inject
    lateinit var gSON: Gson

    lateinit var alertDialog: AlertDialog

    private var permissionCallback: (Boolean) -> Unit = {}

    lateinit var networkManager: NetworkManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutId)
        created(savedInstanceState)
    }

    // Call Phone
    fun callPermission(permissionCallback: (Boolean) -> Unit = {}) {
//        this.permissionCallback = permissionCallback
        requestPhonePermission(permissionCallback)
    }


    private fun requestPhonePermission(permissionCallback: (Boolean) -> Unit = {}) {
//        this.permissionCallback = permissionCallback

        askPermissions(Manifest.permission.CALL_PHONE) { permissionResult ->
            when {
                permissionResult.hasAllGranted() -> {
                    permissionCallback(true)
                }
                permissionResult.hasRational() -> {
                    permissionCallback(false)
                }
                permissionResult.hasPermanentDenied() -> {
                    permissionCallback(false)
                    appSettingDialog()
                }
            }
        }
    }

    fun requestReadContactsPermission(permissionCallback: (Boolean) -> Unit = {}) {
//        this.permissionCallback = permissionCallback

        askPermissions(Manifest.permission.READ_CONTACTS) { permissionResult ->
            when {
                permissionResult.hasAllGranted() -> {
                    permissionCallback(true)
                }
                permissionResult.hasRational() -> {
                    permissionCallback(false)
                }
                permissionResult.hasPermanentDenied() -> {
                    permissionCallback(false)
                    appSettingDialog()
                }
            }
        }
    }


    // Permissions Camera , Location
    private fun locationEnabled(): Boolean {
        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        var networkEnabled = false
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return gpsEnabled || networkEnabled
    }

    fun locationPermission(permissionCallback: (Boolean) -> Unit = {}) {
        this.permissionCallback = permissionCallback
        when {
            locationEnabled() -> requestLocationPermission()
            else -> AlertDialogs.showAlertDialog(this, getString(R.string.location_setting),
                getString(R.string.enableLocation), getString(R.string.setting),
                getString(R.string.cancel), false, clickCallback = { clickCallback ->
                    if (clickCallback as Int == Constants.CLICK_POSITIVE) {

                        permissionCallback(false)

                        appSettingDialog.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

                    }
                })
        }
    }

    private val appSettingDialog =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data

                permissionCallback(true)
            }
        }

    private fun requestLocationPermission() {

        askPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) { permissionResult ->
            when {
                permissionResult.hasAllGranted() -> {
                    permissionCallback(true)
                }
                permissionResult.hasRational() -> {
                    permissionCallback(false)
                }
                permissionResult.hasPermanentDenied() -> {
                    permissionCallback(false)
                    appSettingDialog()
                }
            }
        }

//        val perms = arrayOf(
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//        if (EasyPermissions.hasPermissions(this, *perms)) {
//            // Already have permission, do the thing
//            permissionCallback(true)
//        } else {
//            // Do not have permissions, request them now
//            EasyPermissions.requestPermissions(
//                PermissionRequest.Builder(
//                    this,
//                    Constants.FINE_AND_COURSE_LOCATION_PERMISSION,
//                    *perms
//                )
//                    .setTheme(R.style.AlertDialogCustom)
//                    .build()
//            )
//        }
    }

    private fun appSettingDialog(){
        AlertDialogs.showAlertDialog(this , getString(R.string.required_permission) , getString(R.string.permission_denied_text) ,
            getString(R.string.setting) , getString(R.string.cancel) , false){
            if (it == CLICK_POSITIVE){
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                    )
                )
            }
        }
    }

    fun cameraPermission(permissionCallback: (Boolean) -> Unit = {}) {
        this.permissionCallback = permissionCallback
        requestCameraPermission()
    }

    private fun requestCameraPermission() {

        askPermissions(Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) { permissionResult ->
            when {
                permissionResult.hasAllGranted() -> {
                    permissionCallback(true)
                }
                permissionResult.hasRational() -> {
                    permissionCallback(false)
                }
                permissionResult.hasPermanentDenied() -> {
                    permissionCallback(false)
                    appSettingDialog()
                }
            }
        }
//        val perms = arrayOf(
//            Manifest.permission.CAMERA,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        )
//        if (EasyPermissions.hasPermissions(this, *perms)) {
//            // Already have permission, do the thing
//            permissionCallback(true)
//        } else {
//            // Do not have permissions, request them now
//            EasyPermissions.requestPermissions(
//                PermissionRequest.Builder(
//                    this,
//                    Constants.CAMERA_AND_READ_AND_WRITE_PERMISSION,
//                    *perms
//                )
//                    .setTheme(R.style.AlertDialogCustom)
//                    .build()
//            )
//        }
    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        // Forward results to EasyPermissions
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
//    }
//
//    override fun onPermissionsGranted(requestCode: Int, list: List<String>) {
//        // Some permissions have been granted
//        when (requestCode) {
//            Constants.CAMERA_AND_READ_AND_WRITE_PERMISSION -> permissionCallback(true)
//            Constants.FINE_AND_COURSE_LOCATION_PERMISSION -> permissionCallback(true)
//            Constants.CALL_PHONE_PERMISSION -> permissionCallback(true)
//        }
//    }
//
//    override fun onPermissionsDenied(requestCode: Int, list: List<String>) {
//        // Some permissions have been denied
//        when (requestCode) {
//            Constants.CAMERA_AND_READ_AND_WRITE_PERMISSION -> permissionCallback(false)
//            Constants.FINE_AND_COURSE_LOCATION_PERMISSION -> permissionCallback(false)
//            Constants.CALL_PHONE_PERMISSION -> permissionCallback(false)
//        }
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
//            AppSettingsDialog.Builder(this).build().show()
//        }
//    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Activity Navigation
    fun activityNavigation(
        context: Context?, activityClass: Class<*>?, isFinish: Boolean,
        dataName: List<String>, dataValues: List<Any>
    ) {

        val activityIntent = Intent(context, activityClass)
        val incrementData = 0
        for (intentValue in dataName) {
            activityIntent.putExtra(intentValue, gSON.toJson(dataValues[incrementData]))
        }
        startActivity(activityIntent)
        if (isFinish) finish()
    }

    fun activityNavigation(context: Context?, activityClass: Class<*>?, isFinish: Boolean) {

        startActivity(Intent(context, activityClass))
        if (isFinish) finish()
    }

    fun showErrorSnackBar(message: String, parentView: View) {

        val snackbar = Snackbar.make(parentView, message, Snackbar.LENGTH_LONG)

        val layout = snackbar.view as Snackbar.SnackbarLayout

        //inflate view
        val snackView: View = layoutInflater.inflate(R.layout.error_custom_view, null)

        // Configure the view
        val errorMessage: TextView = snackView.findViewById<View>(R.id.errorMessage) as TextView
        errorMessage.text = message

//        snackView.layoutParams = params
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)
        layout.addView(snackView, 0)
        snackbar.duration = 2000
        snackbar.show()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showLog(message: String) {
        Log.d(tag, message)
    }

    // Abstract Method
    protected abstract val layoutId: View?
    protected abstract val tag: String?
    fun isAlertDialogInitialized(): Boolean = ::alertDialog.isInitialized
    protected abstract fun created(savedInstance: Bundle?)
}