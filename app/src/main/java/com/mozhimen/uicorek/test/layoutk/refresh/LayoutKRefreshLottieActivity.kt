package com.mozhimen.uicorek.test.layoutk.refresh

import android.os.Bundle
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.elemk.android.os.WakeBefPauseLifecycleHandler
import com.mozhimen.basick.utilk.android.os.applyPostDelayed
import com.mozhimen.uicorek.layoutk.refresh.commons.IRefreshListener
import com.mozhimen.uicorek.layoutk.refresh.impls.LottieOverView
import com.mozhimen.uicorek.test.databinding.ActivityLayoutkRefreshLottieBinding

class LayoutKRefreshLottieActivity : BaseActivityVDB<ActivityLayoutkRefreshLottieBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        val lottieOverView = LottieOverView(this)
        vdb.layoutkRefreshContainerLottie.setRefreshOverView(lottieOverView)
        vdb.layoutkRefreshContainerLottie.setRefreshParams(90f.dp2px().toInt(), null, null)
        vdb.layoutkRefreshContainerLottie.setRefreshListener(object : IRefreshListener {
            override fun onRefreshing() {
                WakeBefPauseLifecycleHandler(this@LayoutKRefreshLottieActivity).applyPostDelayed(1000) {
                    vdb.layoutkRefreshContainerLottie.finishRefresh()
                }
            }

            override fun onEnableRefresh(): Boolean {
                return true
            }
        })
    }
}