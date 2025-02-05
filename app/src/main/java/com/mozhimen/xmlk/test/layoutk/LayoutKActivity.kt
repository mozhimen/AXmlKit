package com.mozhimen.xmlk.test.layoutk

import android.view.View
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.kotlin.utilk.android.content.startContext
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkBinding
import com.mozhimen.xmlk.test.layoutk.banner.LayoutKBannerActivity
import com.mozhimen.xmlk.test.layoutk.loadrefresh.LayoutKLoadRefreshActivity
import com.mozhimen.xmlk.test.layoutk.navbar.LayoutKNavBarActivity
import com.mozhimen.xmlk.test.layoutk.refresh.LayoutKRefreshActivity
import com.mozhimen.xmlk.test.layoutk.side.LayoutKSideActivity
import com.mozhimen.xmlk.test.layoutk.slider.LayoutKSliderActivity
import com.mozhimen.xmlk.test.layoutk.tab.LayoutKTabActivity

class LayoutKActivity : BaseActivityVDB<ActivityLayoutkBinding>() {
    fun goLayoutKBanner(view: View) {
        startContext<LayoutKBannerActivity>()
    }

    fun goLayoutKBlur(view: View) {

    }

    fun goLayoutKBtn(view: View) {
        startContext<LayoutKBtnActivity>()
    }

    fun goLayoutKEdit(view: View) {
        startContext<LayoutKEditActivity>()
    }

    fun goLayoutKLoadRefresh(view: View) {
        startContext<LayoutKLoadRefreshActivity>()
    }

    fun goLayoutKNavBar(view: View) {
        startContext<LayoutKNavBarActivity>()
    }

    fun goLayoutKRefresh(view: View) {
        startContext<LayoutKRefreshActivity>()
    }

    fun goLayoutKRoll(view: View) {
        startContext<LayoutKRollActivity>()
    }

    fun goLayoutKScroll(view: View) {
        startContext<LayoutKScrollActivity>()
    }

    fun goLayoutSearch(view: View) {

    }

    fun goLayoutKSide(view: View) {
        startContext<LayoutKSideActivity>()
    }

    fun goLayoutKSlider(view: View) {
        startContext<LayoutKSliderActivity>()
    }

    fun goLayoutKTab(view: View) {
        startContext<LayoutKTabActivity>()
    }

    fun goLayoutKTouch(view: View) {

    }

    fun goLayoutKUnTouch(view: View) {

    }

    fun goLayoutAmount(view: View) {

    }

    fun goLayoutKChipGroup(view: View) {
        startContext<LayoutKChipGroupActivity>()
    }

    fun goLayoutKEmpty(view: View) {
        startContext<LayoutKEmptyActivity>()
    }

    fun goLayoutKLabelGroup(view: View) {

    }

    fun goLayoutKLoading(view: View) {
        startContext<LayoutKLoadingActivity>()
    }

    fun goLayoutKSpinner(view: View) {
        startContext<LayoutKSpinnerActivity>()
    }

    fun goLayoutKSquare(view: View) {
        startContext<LayoutKSquareActivity>()
    }
}