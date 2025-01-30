package com.mozhimen.xmlk.layoutk.refresh.commons

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.xmlk.layoutk.refresh.cons.ERefreshStatus

/**
 * @ClassName RefreshOverView
 * @Description 下拉数显的Overlay视图,可以重载这个类来定义自己的Overlay
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/4/17 23:32
 * @Version 1.0
 */
abstract class RefreshOverView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    var refreshStatus = ERefreshStatus.INIT

    /**
     * 触发下拉刷新需要的最小高度
     */
    var minPullRefreshHeight = 90f.dp2px().toInt()

    /**
     * 最小阻尼
     */
    var minDamp = 1.6f

    /**
     * 最大阻尼
     */
    var maxDamp = 2.2f

    init {
        init()
    }

    /**
     * 初始化
     */
    abstract fun init()

    /**
     * 开始滑动
     * @param scrollY Int
     * @param pullRefreshHeight Int
     */
    abstract fun onScroll(scrollY: Int, pullRefreshHeight: Int)

    /**
     * 显示Overlay
     */
    abstract fun onVisible()

    /**
     * 超过Overlay,释放就会加载
     */
    abstract fun onOverflow()

    /**
     * 开始刷新
     */
    abstract fun onStartRefresh()

    /**
     * 加载完成
     */
    abstract fun onFinish()

    /**
     * 设置下拉刷新头部状态
     * @param status ERefreshStatus
     */
    open fun setStatus(status: ERefreshStatus) {
        this.refreshStatus = status
    }

    /**
     * 获取状态
     *
     * @return kStatus
     */
    open fun getStatus(): ERefreshStatus = refreshStatus
}