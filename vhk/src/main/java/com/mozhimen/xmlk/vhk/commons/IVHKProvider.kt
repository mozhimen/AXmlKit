package com.mozhimen.xmlk.vhk.commons

import android.content.Context
import android.view.ViewGroup

/**
 * @ClassName IItemProvider
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/4/29
 * @Version 1.0
 */
interface IVHKProvider<DATA, VH> {
    fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH
    fun onBindViewHolder(holder: VH, item: DATA?, position: Int)
    fun onBindViewHolder(holder: VH, item: DATA?, position: Int, payloads: List<Any>) {
        onBindViewHolder(holder, item, position)
    }

    fun onViewAttachedToWindow(holder: VH, item: DATA?, position: Int?)
    fun onViewDetachedFromWindow(holder: VH, item: DATA?, position: Int?)
    fun onViewRecycled(holder: VH, item: DATA?, position: Int?)
}