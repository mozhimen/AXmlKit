package com.mozhimen.xmlk.recyclerk.item.commons

import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.kotlin.utilk.commons.IUtilK
import com.mozhimen.xmlk.recyclerk.item.RecyclerKItem

/**
 * @ClassName IAdapterKRecycler
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
interface IRecyclerKAdapter : IUtilK {
    fun refreshItem(item: RecyclerKItem<out RecyclerView.ViewHolder>, position: Int, notify: Boolean, payloads: Any?)
    /**
     * 刷新Item
     * @param item RecyclerKItem<DATA, ViewHolder>
     */
    fun refreshItem(item: RecyclerKItem<out RecyclerView.ViewHolder>, position: Int, notify: Boolean)

    /**
     * 刷新Item
     */
    fun refreshItems(notify: Boolean)

    /**
     * 刷新Item
     * @param items List<RecyclerKItem<DATA, ViewHolder>>
     */
    fun refreshItems(items: List<RecyclerKItem<out RecyclerView.ViewHolder>>, notify: Boolean)

    /**
     * 在末尾添加item
     * @param item DataKItem<DATA, ViewHolder>
     * @param notify Boolean
     */
    fun addItem(item: RecyclerKItem<out RecyclerView.ViewHolder>, notify: Boolean)

    /**
     * 在指定位上添加item
     * @param item RecyclerKItem<DATA, ViewHolder>
     * @param position Int
     * @param notify Boolean
     */
    fun addItemAtPosition(item: RecyclerKItem<out RecyclerView.ViewHolder>, position: Int, notify: Boolean)

    /**
     * 添加items集合
     * @param items List<RecyclerKItem<DATA, ViewHolder>>
     * @param notify Boolean
     */
    fun addItems(items: List<RecyclerKItem<out RecyclerView.ViewHolder>>, notify: Boolean)

    /**
     * 移除item
     * @param item RecyclerKItem<DATA, ViewHolder>
     */
    fun removeItem(item: RecyclerKItem<out RecyclerView.ViewHolder>, notify: Boolean)

    /**
     * 从指定位上移除item
     * @param position Int
     * @return RecyclerKItem<DATA, VH>?
     */
    fun removeItemAtPosition(position: Int, notify: Boolean): RecyclerKItem<in RecyclerView.ViewHolder>?

    /**
     * 删除所有的item
     */
    fun removeItemsAll(notify: Boolean)

    /**
     * 获取Item
     * @param position Int
     * @return RecyclerKItem<DATA, VH>?
     */
    fun getItem(position: Int): RecyclerKItem<RecyclerView.ViewHolder>?

    /**
     * 获取Items
     * @return RecyclerKItem<DATA, VH>?
     */
    fun getItems(): List<RecyclerKItem<in RecyclerView.ViewHolder>>

    /**
     * 获取RecyclerView
     * @return RecyclerView?
     */
    fun getAttachedRecyclerView(): RecyclerView?
}