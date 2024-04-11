package com.mozhimen.xmlk.adapterk.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @ClassName AdapterKFragmentPager
 * @Description TODO
 * @Author Kolin Zhao
 * @Version 1.0
 */
open class AdapterKFragmentPager : FragmentPagerAdapter {
    private val _fragments: List<Fragment>

    constructor(fragmentManager: FragmentManager, fragments: List<Fragment>) : super(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        _fragments = fragments
    }

    /////////////////////////////////////////////////////////////////////

    override fun getCount() =
        _fragments.size

    override fun getItem(position: Int): Fragment =
        _fragments[position]
}