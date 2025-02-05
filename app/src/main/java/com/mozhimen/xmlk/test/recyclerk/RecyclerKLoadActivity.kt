package com.mozhimen.xmlk.test.recyclerk

import android.os.Bundle
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.mozhimen.basick.helpers.WakeBefPauseLifecycleHandler
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.mozhimen.xmlk.recyclerk.item.RecyclerKItemAdapterStuffed
import com.mozhimen.xmlk.layoutk.refresh.commons.IRefreshListener
import com.mozhimen.xmlk.layoutk.refresh.impls.TextOverView
import com.mozhimen.xmlk.layoutk.refresh.cons.ERefreshStatus
import com.mozhimen.xmlk.recyclerk.item.RecyclerKItem
import com.mozhimen.xmlk.recyclerk.load.commons.IRecyclerKLoadListener
import com.mozhimen.xmlk.test.R
import com.mozhimen.xmlk.test.databinding.ActivityRecyclerkLoadBinding
import com.mozhimen.xmlk.test.recyclerk.mos.RecyclerKItemLoadMore

class RecyclerKLoadActivity : BaseActivityVDB<ActivityRecyclerkLoadBinding>() {
    private var _pageIndex: Int = 1
    private lateinit var _textOverView: TextOverView
    private val _adapterKRecyclerStuffed by lazy_ofNone { RecyclerKItemAdapterStuffed() }
    private val _dataSets = ArrayList<RecyclerKItem<out RecyclerView.ViewHolder>>()

    override fun initView(savedInstanceState: Bundle?) {
        initRefresh()
        initRecycler()
    }

    private fun initRefresh() {
        _textOverView = TextOverView(this)
        vdb.layoutkRefresh.setRefreshOverView(_textOverView)
        vdb.layoutkRefresh.setRefreshParams(90f.dp2px().toInt(), 1.6f, null)
        vdb.layoutkRefresh.setRefreshListener(object : IRefreshListener {
            override fun onRefreshing() {
                if (vdb.recyclerkLoad.isLoading()) {
                    //正处于分页
                    //复现场景,比较难以复现---》如果下执行上拉分页。。。快速返回  往下拉，松手。会出现一个bug: 转圈圈的停住不动了。
                    //问题的原因在于 立刻调用 refreshFinished 时，refreshHeader的底部bottom值是超过了 它的height的。
                    //refreshLayout#recover（dis） 方法中判定了，如果传递dis 参数 大于 header height ,dis =200,height =100,只能恢复到 刷新的位置。不能恢复到初始位置。
                    //加了延迟之后，他会  等待 松手的动画做完，才去recover 。此时就能恢复最初状态了。
                    vdb.recyclerkLoad.post {
                        vdb.layoutkRefresh.finishRefresh()
                    }
                    return
                }
                _pageIndex = 1
                //模拟刷新
                WakeBefPauseLifecycleHandler(this@RecyclerKLoadActivity).postDelayed(1000) {
                    //模拟获取到了
                    val dataItems: ArrayList<RecyclerKItem<out RecyclerView.ViewHolder>> = arrayListOf(
                        RecyclerKItemLoadMore(1),
                        RecyclerKItemLoadMore(2),
                        RecyclerKItemLoadMore(3),
                        RecyclerKItemLoadMore(4),
                    )
                    _dataSets.clear()
                    _dataSets.addAll(dataItems)
                    //----------->
                    refreshOrLoad(true, _dataSets)
                }
            }

            override fun onEnableRefresh(): Boolean {
                return true
            }
        })
    }

    /**
     * 刷新or加载
     * @param isRefresh Boolean 是否是刷新
     * @param dataItems List<DataKItem<*, out ViewHolder>>?
     */
    fun refreshOrLoad(isRefresh: Boolean, dataItems: List<RecyclerKItem<out RecyclerView.ViewHolder>>?) {
        val success = dataItems != null && dataItems.isNotEmpty()
        //光真么判断还是不行的，我们还需要别的措施。。。因为可能会出现 下拉单时候，有执行了删上拉分页
        if (isRefresh) {
            vdb.layoutkRefresh.finishRefresh()
            if (success) {
                //emptyView?.visibility = View.GONE空白布局
                _adapterKRecyclerStuffed.removeItemsAll(true)
                _adapterKRecyclerStuffed.addItems(dataItems!!, true)
            } else {
                //此时就需要判断列表上是否已经有数据，如果么有，显示出空页面转态
                if (_adapterKRecyclerStuffed.itemCount <= 0) {
                    //emptyView?.visibility = View.VISIBLE空白布局
                }
            }
        } else {
            if (success) {
                _dataSets.addAll(dataItems!!)
                _adapterKRecyclerStuffed.addItems(dataItems, true)
            }
            vdb.recyclerkLoad.finishLoad()
        }
    }

    private fun initRecycler() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        vdb.recyclerkLoad.layoutManager = layoutManager
        vdb.recyclerkLoad.adapter = _adapterKRecyclerStuffed
        vdb.recyclerkLoad.setFooterView(R.layout.item_recyclerk_footer_load)
        vdb.recyclerkLoad.enableLoad(5, object : IRecyclerKLoadListener {
            override fun onLoading() {
                if (_textOverView.refreshStatus == ERefreshStatus.VISIBLE ||
                    _textOverView.refreshStatus == ERefreshStatus.REFRESHING ||
                    _textOverView.refreshStatus == ERefreshStatus.OVERFLOW ||
                    _textOverView.refreshStatus == ERefreshStatus.OVERFLOW_RELEASE
                ) {
                    //正处于刷新状态
                    vdb.layoutkRefresh.finishRefresh()
                    return
                }
                _pageIndex++
                //模拟加载
                WakeBefPauseLifecycleHandler(this@RecyclerKLoadActivity).postDelayed(1000) {
                    val dataItems: List<RecyclerKItem<out RecyclerView.ViewHolder>> = arrayListOf(
                        RecyclerKItemLoadMore(_dataSets.size + 1)
                    )
                    refreshOrLoad(false, dataItems)
                }
            }
        })
        _dataSets.apply {
            add(RecyclerKItemLoadMore(1))
            add(RecyclerKItemLoadMore(2))
            add(RecyclerKItemLoadMore(3))
            add(RecyclerKItemLoadMore(4))
            add(RecyclerKItemLoadMore(5))
        }
        _adapterKRecyclerStuffed.addItems(_dataSets, true)
    }
}