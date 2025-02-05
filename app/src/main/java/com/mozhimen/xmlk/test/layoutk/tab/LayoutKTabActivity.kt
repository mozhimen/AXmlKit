package com.mozhimen.xmlk.test.layoutk.tab

import android.view.View
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.kotlin.utilk.android.content.startContext
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkTabBinding

/**
 * @ClassName LayoutKTabActivity
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/7 1:29
 * @Version 1.0
 */
class LayoutKTabActivity : BaseActivityVDB<ActivityLayoutkTabBinding>() {

    fun goLayoutKTabBottom(view: View) {
        startContext<LayoutKTabBottomActivity>()
    }

    fun goLayoutKTabBottomFragment(view: View) {
        startContext<LayoutKTabBottomFragmentActivity>()
    }

    fun goLayoutKTabBottomLayout(view: View) {
        startContext<LayoutKTabBottomLayoutActivity>()
    }

    fun goLayoutKTabTop(view: View) {
        startContext<LayoutKTabTopActivity>()
    }

    fun goLayoutKTabTopLayout(view: View) {
        startContext<LayoutKTabTopLayoutActivity>()
    }
}