package com.mozhimen.xmlk.adapterk.list

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.mozhimen.kotlin.lintk.optins.OApiCall_Recycle
import com.mozhimen.kotlin.utilk.androidx.lifecycle.handleLifecycleEventOnDestroy
import com.mozhimen.kotlin.utilk.androidx.lifecycle.handleLifecycleEventOnStart
import com.mozhimen.xmlk.vhk.VHK


/**
 * @ClassName BaseAdapterK
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/11/22 15:02
 * @Version 1.0
 */
@OApiCall_Recycle
abstract class AdapterKList<T>(
    protected val _datas: MutableList<T>,
    @LayoutRes private val _layoutId: Int
) : BaseAdapter(), LifecycleOwner {

    private var _lifecycleRegistry: LifecycleRegistry? = null
    protected val lifecycleRegistry: LifecycleRegistry
        get() = _lifecycleRegistry ?: LifecycleRegistry(this).also {
            _lifecycleRegistry = it
        }

    //////////////////////////////////////////////////////////////////////////

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    //////////////////////////////////////////////////////////////////////////

    init {
        lifecycleRegistry.handleLifecycleEventOnStart()
    }

    //////////////////////////////////////////////////////////////////////////

    override fun getCount(): Int =
        _datas.size

    override fun getItem(position: Int): T =
        _datas[position]

    override fun getItemId(position: Int): Long =
        position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder = VHK.bindView(parent.context, convertView, parent, _layoutId, position)
        onBindView(viewHolder, getItem(position), position)
        return viewHolder.itemView
    }

    //////////////////////////////////////////////////////////////////////////

    abstract fun onBindView(holder: VHK, data: T, position: Int)

//    @CallSuper
//    open fun onDetachedFromWindow() {
////        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
//    }

    @CallSuper
    open fun onViewRecycled() {
        lifecycleRegistry.handleLifecycleEventOnDestroy()
    }

    //////////////////////////////////////////////////////////////////////////

    //添加一个元素
    fun add(data: T) {
        _datas.add(data)
        notifyDataSetChanged()
    }

    //往特定位置，添加一个元素
    fun add(position: Int, data: T) {
        _datas.add(position, data)
        notifyDataSetChanged()
    }

    fun remove(data: T) {
        _datas.remove(data)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        _datas.removeAt(position)
        notifyDataSetChanged()
    }

    fun clear() {
        _datas.clear()
        notifyDataSetChanged()
    }
}

