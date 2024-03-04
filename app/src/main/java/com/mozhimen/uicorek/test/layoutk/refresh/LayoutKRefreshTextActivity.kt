package com.mozhimen.uicorek.test.layoutk.refresh

import android.os.Bundle
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.elemk.android.os.WakeBefPauseLifecycleHandler
import com.mozhimen.basick.utilk.android.os.applyPostDelayed
import com.mozhimen.uicorek.layoutk.refresh.commons.IRefreshListener
import com.mozhimen.uicorek.layoutk.refresh.temps.TextOverView
import com.mozhimen.uicorek.test.databinding.ActivityLayoutkRefreshTextBinding

class LayoutKRefreshTextActivity : BaseActivityVDB<ActivityLayoutkRefreshTextBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        val textOverView = TextOverView(this)
        vdb.layoutkRefreshTextContainer.setRefreshOverView(textOverView)
        vdb.layoutkRefreshTextContainer.setRefreshParams(90f.dp2px().toInt(), 1.6f, null)
        vdb.layoutkRefreshTextContainer.setRefreshListener(object : IRefreshListener {
            override fun onRefreshing() {
                WakeBefPauseLifecycleHandler(this@LayoutKRefreshTextActivity).applyPostDelayed(1000) { vdb.layoutkRefreshTextContainer.finishRefresh() }
            }

            override fun onEnableRefresh(): Boolean {
                return true
            }
        })
    }
}