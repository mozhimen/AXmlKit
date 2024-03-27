package com.mozhimen.xmlk.test.layoutk.loadrefresh

import android.os.Bundle
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.lintk.optins.OApiInit_ByLazy
import com.mozhimen.basick.elemk.android.os.WakeBefPauseLifecycleHandler
import com.mozhimen.xmlk.layoutk.loadrefresh.commons.LoadRefreshLoadCallback
import com.mozhimen.xmlk.layoutk.loadrefresh.commons.LoadRefreshRefreshCallback
import com.mozhimen.xmlk.layoutk.refresh.impls.LottieOverView
import com.mozhimen.xmlk.recyclerk.item.RecyclerKItem
import com.mozhimen.xmlk.test.databinding.ActivityLayoutkLoadrefreshBinding
import com.mozhimen.xmlk.test.recyclerk.mos.RecyclerKItemLoadMore

class LayoutKLoadRefreshActivity : BaseActivityVDB<ActivityLayoutkLoadrefreshBinding>() {
    private val _dataSets = ArrayList<RecyclerKItem<out RecyclerView.ViewHolder>>()
    private var _pageIndex = 0

    @OptIn(OApiInit_ByLazy::class)
    override fun initView(savedInstanceState: Bundle?) {
        _dataSets.apply {
            add(RecyclerKItemLoadMore(1))
            add(RecyclerKItemLoadMore(2))
            add(RecyclerKItemLoadMore(3))
            add(RecyclerKItemLoadMore(4))
            add(RecyclerKItemLoadMore(5))
        }
        val lottieOverView = LottieOverView(this)
        vdb.loadkContainer.apply {
            initLoadParams(
                5,
                _dataSets,
                object : LoadRefreshLoadCallback(lottieOverView, vdb.loadkContainer) {
                    override fun onLoading() {
                        super.onLoading()
                        _pageIndex++
                        WakeBefPauseLifecycleHandler(this@LayoutKLoadRefreshActivity).postDelayed(1000) {
                            val items: List<RecyclerKItem<out RecyclerView.ViewHolder>> = arrayListOf(RecyclerKItemLoadMore(_dataSets.size + 1))
                            _dataSets.addAll(items)
                            vdb.loadkContainer.startLoad(items, null)
                        }
                    }
                })
            initRefreshParams(
                LottieOverView(this@LayoutKLoadRefreshActivity),
                null,
                null,
                null,
                object : LoadRefreshRefreshCallback(vdb.loadkContainer, vdb.loadkContainer.getRecyclerKLoad()) {
                    override fun onRefreshing() {
                        super.onRefreshing()
                        _pageIndex = 1
                        WakeBefPauseLifecycleHandler(this@LayoutKLoadRefreshActivity).postDelayed(1000) {
                            //模拟获取到了
                            val items: ArrayList<RecyclerKItem<out RecyclerView.ViewHolder>> = arrayListOf(
                                RecyclerKItemLoadMore(1),
                                RecyclerKItemLoadMore(2),
                                RecyclerKItemLoadMore(3),
                                RecyclerKItemLoadMore(4),
                            )
                            _dataSets.clear()
                            _dataSets.addAll(items)
                            startRefresh(items, null)
                        }
                    }
                })
        }
    }
}