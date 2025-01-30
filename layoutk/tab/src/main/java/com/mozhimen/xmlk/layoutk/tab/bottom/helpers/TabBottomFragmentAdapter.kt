package com.mozhimen.xmlk.layoutk.tab.bottom.helpers

import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.mozhimen.kotlin.utilk.commons.IUtilK

import com.mozhimen.xmlk.layoutk.tab.bottom.mos.MTabBottom
import java.lang.Exception

/**
 * @ClassName TabBottomFragmentAdapter
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/4/15 21:16
 * @Version 1.0
 */
class TabBottomFragmentAdapter(
    private var _fragmentManager: FragmentManager,
    private var _moList: List<MTabBottom>
) : IUtilK {

    private var _currentFragment: Fragment? = null

    /**
     * 获取当前fragment
     * @return Fragment?
     */
    fun getCurrentFragment(): Fragment? = _currentFragment

    /**
     * 获取数量
     * @return Int
     */
    fun getCount(): Int = _moList.size

    /**
     * 实例化以及显示指定位置的fragment
     * @param container View
     * @param position Int
     */
    fun instantiateItem(container: View, position: Int) {
        val currentTransaction: FragmentTransaction = _fragmentManager.beginTransaction()
        if (_currentFragment != null) {
            currentTransaction.hide(_currentFragment!!)
        }
        val name = container.id.toString() + ":" + position
        var fragment: Fragment? = _fragmentManager.findFragmentByTag(name)
        if (fragment != null) {
            currentTransaction.show(fragment)
        } else {
            fragment = getItem(position)
            if (fragment != null && !fragment.isAdded) {
                currentTransaction.add(container.id, fragment, name)
            }
        }
        _currentFragment = fragment
        currentTransaction.commitAllowingStateLoss()
    }

    /**
     * 获取item
     * @param position Int
     * @return Fragment?
     */
    fun getItem(position: Int): Fragment? {
        try {
            return _moList[position].fragment!!.newInstance()
        } catch (e: Exception) {
            e.printStackTrace()
            UtilKLogWrapper.e(TAG, "getItem Exception ${e.message}")
        }
        return null
    }
}