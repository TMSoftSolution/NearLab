package com.nft.maker.ui.home_dashboard_fragment

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.nft.maker.BaseFragment
import com.nft.maker.databinding.FragmentHomeDashboardBinding
import com.nft.maker.ui.dasboard_activity.ViewPagerAdapter
import com.nft.maker.ui.my_nft_dashboard_fragment.MyNftDashboardFragment
import com.nft.maker.ui.recieved_nft_dasboard_fragment.RecievedNftDasboardFragment
import com.nft.maker.ui.send_nft_dasboard_fragment.SendNftDasboardFragment
import com.google.android.material.tabs.TabLayout

class HomeDashboardFragment : BaseFragment() {
    private lateinit var fragmentHomeDashboardBinding: FragmentHomeDashboardBinding
    private var myContext: FragmentActivity? = null
    var tablayout: TabLayout? = null
    var ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 2323


    override fun onAttach(context: Context) {
        myContext = activity as FragmentActivity
        super.onAttach(context)
    }

    override val layoutId: View
        get() {
            fragmentHomeDashboardBinding = FragmentHomeDashboardBinding.inflate(layoutInflater)
            return fragmentHomeDashboardBinding.root
        }
    override val layoutTag: String?
        get() = HomeDashboardFragment::class.java.simpleName

    override fun initUI(view: View) {
        initiate()

    }


    private fun initiate() {
        tabLayout()
        tablayout = fragmentHomeDashboardBinding.tabLayout
        tablayout!!.getTabAt(0)?.select()
        fragmentHomeDashboardBinding.accountId.text=preferenceHelper().accountID

    }


    private fun tabLayout() {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(MyNftDashboardFragment(), "My NFT's")
        adapter.addFragment(SendNftDasboardFragment(), "Sent")
        adapter.addFragment(RecievedNftDasboardFragment(), "Received")
        fragmentHomeDashboardBinding.viewPager.offscreenPageLimit = 3
        fragmentHomeDashboardBinding.viewPager.adapter = adapter
        fragmentHomeDashboardBinding.tabLayout.setupWithViewPager(fragmentHomeDashboardBinding.viewPager)
    }
}