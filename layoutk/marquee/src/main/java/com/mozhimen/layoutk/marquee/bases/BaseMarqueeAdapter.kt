package com.mozhimen.layoutk.marquee.bases

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import com.mozhimen.basick.bases.BaseWakeBefDestroyLifecycleObserver
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiCall_BindViewLifecycle
import com.mozhimen.kotlin.lintk.optins.OApiInit_ByLazy
import com.mozhimen.layoutk.marquee.LayoutKMarqueeText

/**
 * @ClassName BaseMarqueeAdapter
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/12/17
 * @Version 1.0
 */
@OApiInit_ByLazy
@OApiCall_BindLifecycle
@OApiCall_BindViewLifecycle
abstract class BaseMarqueeAdapter<T>(@LayoutRes private val _intLayoutId: Int, private var _datas: List<T>? = null) : BaseWakeBefDestroyLifecycleObserver() {
    private var _onDataChangedListener: OnDataChangedListener? = null

    //////////////////////////////////////////////////////////////////

    override fun onDestroy(owner: LifecycleOwner) {
        removeOnDataChangedListener()
        super.onDestroy(owner)
    }

    //////////////////////////////////////////////////////////////////

    abstract fun onBindView(view: View, position: Int, data: T?)

    //////////////////////////////////////////////////////////////////

    fun onBindView(view: View, position: Int) {//更新数据
        onBindView(view, position, _datas?.getOrNull(position))
    }

    fun setDatas(datas: List<T>, notify: Boolean) {
        _datas = datas
        if (notify)
            notifyDataChanged()
    }

    fun setOnDataChangedListener(onDataChangedListener: OnDataChangedListener) {
        _onDataChangedListener = onDataChangedListener
    }

    fun removeOnDataChangedListener() {
        _onDataChangedListener = null
    }

    /**
     * 自定义跑马灯的Item布局
     */
    fun onCreateView(parent: LayoutKMarqueeText): View =
        LayoutInflater.from(parent.context).inflate(_intLayoutId, null)

    fun getItemCount(): Int =
        _datas?.size ?: 0

    fun notifyDataChanged() {
        _onDataChangedListener?.onDataChanged()
    }

    //////////////////////////////////////////////////////////////////

    interface OnDataChangedListener {
        fun onDataChanged()
    }
}
