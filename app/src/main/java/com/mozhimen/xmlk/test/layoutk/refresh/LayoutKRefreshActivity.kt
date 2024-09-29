package com.mozhimen.xmlk.test.layoutk.refresh

import android.view.View
import com.mozhimen.bindk.bases.activity.databinding.BaseActivityVDB
import com.mozhimen.kotlin.utilk.android.content.startContext
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkRefreshBinding

/**
 * @ClassName LayoutKRefreshActivity
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/11/7 1:36
 * @Version 1.0
 */
class LayoutKRefreshActivity : BaseActivityVDB<ActivityLayoutkRefreshBinding>() {
    fun goLayoutKRefreshLottie(view: View) {
        startContext<LayoutKRefreshLottieActivity>()
    }

    fun goLayoutKRefreshText(view: View) {
        startContext<LayoutKRefreshTextActivity>()
    }
}