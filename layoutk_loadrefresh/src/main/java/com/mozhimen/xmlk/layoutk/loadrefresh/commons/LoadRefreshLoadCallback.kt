package com.mozhimen.xmlk.layoutk.loadrefresh.commons

import androidx.annotation.CallSuper
import com.mozhimen.xmlk.layoutk.refresh.LayoutKRefresh
import com.mozhimen.xmlk.layoutk.refresh.commons.RefreshOverView
import com.mozhimen.xmlk.layoutk.refresh.cons.ERefreshStatus
import com.mozhimen.xmlk.recyclerk.load.commons.IRecyclerKLoadListener

/**
 * @ClassName DataKLoadListener
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/4/28 13:39
 * @Version 1.0
 */
open class LoadRefreshLoadCallback(
    private val _refreshOverView: RefreshOverView,
    private val _layoutKRefresh: LayoutKRefresh,
) : IRecyclerKLoadListener {
    @CallSuper
    override fun onLoading() {
        if (_refreshOverView.refreshStatus == ERefreshStatus.VISIBLE ||
            _refreshOverView.refreshStatus == ERefreshStatus.REFRESHING ||
            _refreshOverView.refreshStatus == ERefreshStatus.OVERFLOW ||
            _refreshOverView.refreshStatus == ERefreshStatus.OVERFLOW_RELEASE
        ) {
            //正处于刷新状态
            _layoutKRefresh.finishRefresh()
            return
        }
    }
}