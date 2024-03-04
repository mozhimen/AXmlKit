package com.mozhimen.uicorek.test.layoutk.refresh

import android.view.View
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.android.content.startContext
import com.mozhimen.uicorek.test.databinding.ActivityLayoutkRefreshBinding

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