package com.nft.maker.ui.dasboard_activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(supportFragmentManager: FragmentManager) : FragmentPagerAdapter(supportFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
)
{
    private val myFragmentList = ArrayList<Fragment>()
    private val myFragmentTitleList = ArrayList<String>()
    override fun getCount(): Int {
        return  myFragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return  myFragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return myFragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title:String)
    {
        myFragmentList.add(fragment)
        myFragmentTitleList.add(title)
    }

}