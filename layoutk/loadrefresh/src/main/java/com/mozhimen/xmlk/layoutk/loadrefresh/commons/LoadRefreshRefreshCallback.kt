package com.mozhimen.xmlk.layoutk.loadrefresh.commons

import androidx.annotation.CallSuper
import com.mozhimen.xmlk.recyclerk.load.RecyclerKLoad
import com.mozhimen.xmlk.layoutk.refresh.LayoutKRefresh
import com.mozhimen.xmlk.layoutk.refresh.commons.IRefreshListener

/**
 * @ClassName LoadKRefreshListener
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/4/28 14:44
 * @Version 1.0
 */
open class LoadRefreshRefreshCallback(
    private val _layoutKRefresh: LayoutKRefresh,
    private val _recyclerKLoad: RecyclerKLoad,
) : IRefreshListener {
    @CallSuper
    override fun onRefreshing() {
        if (_recyclerKLoad.isLoading()) {
            _layoutKRefresh.post {
                _layoutKRefresh.finishRefresh()
            }
            return
        }
    }

    override fun onEnableRefresh(): Boolean = true
}