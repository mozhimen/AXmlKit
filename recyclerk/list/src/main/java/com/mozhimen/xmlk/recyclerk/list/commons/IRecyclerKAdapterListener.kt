package com.mozhimen.xmlk.recyclerk.list.commons

import android.view.View
import androidx.recyclerview.widget.RecyclerView.Adapter

/**
 * @ClassName
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/10/11
 * @Version 1.0
 */
fun interface IOnItemClickListener<DATA : Any, A : Adapter<*>> {
    fun onClick(adapter: A, view: View, position: Int)
}

fun interface IOnItemLongClickListener<DATA : Any, A : Adapter<*>> {
    fun onLongClick(adapter: A, view: View, position: Int): Boolean
}

fun interface IOnItemChildClickListener<DATA : Any, A : Adapter<*>> {
    fun onChildClick(adapter: A, view: View, position: Int)
}

fun interface IOnItemChildLongClickListener<DATA : Any, A : Adapter<*>> {
    fun onChildLongClick(adapter: A, view: View, position: Int): Boolean
}