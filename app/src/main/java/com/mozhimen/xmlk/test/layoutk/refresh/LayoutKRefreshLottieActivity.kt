package com.mozhimen.xmlk.test.layoutk.refresh

import android.os.Bundle
import androidx.core.os.postDelayed
import com.mozhimen.kotlin.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.kotlin.elemk.android.os.WakeBefPauseLifecycleHandler
import com.mozhimen.xmlk.layoutk.refresh.commons.IRefreshListener
import com.mozhimen.xmlk.layoutk.refresh.impls.LottieOverView
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkRefreshLottieBinding

class LayoutKRefreshLottieActivity : BaseActivityVDB<ActivityLayoutkRefreshLottieBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        val lottieOverView = LottieOverView(this)
        vdb.layoutkRefreshContainerLottie.setRefreshOverView(lottieOverView)
        vdb.layoutkRefreshContainerLottie.setRefreshParams(90f.dp2px().toInt(), null, null)
        vdb.layoutkRefreshContainerLottie.setRefreshListener(object : IRefreshListener {
            override fun onRefreshing() {
                WakeBefPauseLifecycleHandler(this@LayoutKRefreshLottieActivity).postDelayed(1000) {
                    vdb.layoutkRefreshContainerLottie.finishRefresh()
                }
            }

            override fun onEnableRefresh(): Boolean {
                return true
            }
        })
    }
}