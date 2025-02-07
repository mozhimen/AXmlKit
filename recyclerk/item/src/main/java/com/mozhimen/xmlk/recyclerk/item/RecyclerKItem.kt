package com.mozhimen.xmlk.recyclerk.item

import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.kotlin.utilk.commons.IUtilK
import com.mozhimen.xmlk.recyclerk.item.commons.IRecyclerKAdapter

/**
 * @ClassName DataItemK
 * @Description TODO
 * @Author Kolin Zhao
 * @Version 1.0
 */
open class RecyclerKItem<VH : RecyclerView.ViewHolder> : IUtilK {
    protected var _adapterKRecycler: IRecyclerKAdapter? = null
    lateinit var vh: VH
    var pos: Int = -1

    /**
     * 设置adapter
     * @param adapter DataKAdapter
     */
    fun bindAdapter(adapter: IRecyclerKAdapter) {
        _adapterKRecycler = adapter
    }

    /**
     * 刷新列表
     */
    fun refreshItem(notify: Boolean) {
        _adapterKRecycler?.refreshItem(this, pos, notify)
    }

    /**
     * 从列表上移除
     */
    fun removeItem(notify: Boolean) {
        _adapterKRecycler?.removeItem(this, notify)
    }

    /**
     * 绑定数据
     * @param holder VH
     * @param position Int
     */
    @CallSuper
    open fun onBindItem(holder: VH, position: Int) {
        vh = holder
        pos = position
    }

    open fun onBindItem(holder: VH, position: Int, payloads: MutableList<Any>) {

    }

    /**
     * 返回该item的资源id
     * @return Int
     */
    open fun getItemLayoutId(): Int = -1

    /**
     * 返回该item的视图view
     * @param parent ViewGroup
     * @return View?
     */
    open fun getItemView(parent: ViewGroup): View? = null

    /**
     * 获取item所占列数
     * @return Int
     */
    open fun getItemSpanSize() = 1

    /**
     * 该item被滑进屏幕
     * @param holder VH
     */
    open fun onViewAttachedToWindow(holder: VH) {}

    /**
     * 当item被划出屏幕
     * @param holder VH
     */
    open fun onViewDetachedFromWindow(holder: VH) {}

    /**
     * 创建ViewHolder
     * @param parent ViewGroup
     * @return VH?
     */
    open fun onCreateViewHolder(parent: ViewGroup): VH? {
        return null
    }
}