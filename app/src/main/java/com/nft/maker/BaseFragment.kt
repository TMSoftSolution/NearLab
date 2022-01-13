package com.nft.maker

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nft.maker.utils.PreferenceHelper
import com.google.gson.Gson

abstract class BaseFragment : Fragment() {
    var mBaseActivity: BaseActivity? = null
    private lateinit var view: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            mBaseActivity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.view = layoutId
        initUI(view)
        return view
    }


    fun navigate(navigateId: Int, bundle: Bundle) {
        findNavController().navigate(navigateId, bundle)
    }

    fun activityNavigation(context: Context?, activityClass: Class<*>, isFinish: Boolean) {
        mBaseActivity?.activityNavigation(context, activityClass, isFinish)
    }

    fun locationPermission(permissionCallback: (Boolean)->Unit={}) {
        mBaseActivity?.locationPermission(permissionCallback)
    }

    abstract val layoutId: View
    abstract val layoutTag: String?
    abstract fun initUI(view: View)

    fun showLog(message : String){
        mBaseActivity?.showLog(message)
    }

    fun showToast(message : String){
        mBaseActivity?.showToast(message)
    }

    fun showErrorSnackBar(message: String , parentView : View ){
        mBaseActivity?.showErrorSnackBar(message, parentView)
    }

    override fun getView(): View {
        return view
    }
    fun isAlertInitialized():Boolean{

        if (mBaseActivity!!.alertDialog.isShowing){
            return true
        }

        return false
    }

    fun setAlertDialog(alertDialog: AlertDialog){
        mBaseActivity!!.alertDialog = alertDialog
    }

    fun getAlertDialog() : AlertDialog = mBaseActivity!!.alertDialog

    fun gson() : Gson = mBaseActivity!!.gSON

    fun preferenceHelper() : PreferenceHelper = mBaseActivity!!.preferenceHelper
}