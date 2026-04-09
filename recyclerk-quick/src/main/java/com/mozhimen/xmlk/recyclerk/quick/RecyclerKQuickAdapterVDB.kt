package com.mozhimen.xmlk.recyclerk.quick

import android.annotation.SuppressLint
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.kotlin.utilk.commons.IUtilK
import com.mozhimen.xmlk.recyclerk.commons.IRecyclerKAdapterVDB
import com.mozhimen.xmlk.recyclerk.commons.IRecyclerKAdapterVDBListener
import com.mozhimen.xmlk.vhk.VHKLifecycle2VDB

/**
 * @ClassName RecyclerKQuickAdapterVDB
 * @Description  通用RecyclerView适配器
 * 注意:
 * 在使用Fragment切换,挂起与恢复时, 要使recyclerView.adapter置null
 * 不然持有全局本类, 会引起内存的泄漏
 * @Author Kolin Zhao
 * @Date 2021/6/4 20:07
 * @Version 1.0
 */
open class RecyclerKQuickAdapterVDB<DATA, VDB : ViewDataBinding>(
    private var _datas: MutableList<DATA>,
    private val _defaultLayout: Int,
    private val _brId: Int,
    private val _listener: IRecyclerKAdapterVDBListener<DATA, VDB>? = null /* = (VHK<ViewDataBinding>, T, Int) -> Unit */
) : RecyclerView.Adapter<VHKLifecycle2VDB<VDB>>(), IRecyclerKAdapterVDB<DATA, VDB>, IUtilK {

    private var _selectItemPosition = -1

    ////////////////////////////////////////////////////////////////////////////

    override fun refreshData(data: DATA, position: Int, notify: Boolean) {
        if (position < 0 || position >= _datas.size) return
        _datas[position] = data
        if (notify) notifyItemChanged(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun refreshDatas(notify: Boolean) {
        if (notify) notifyDataSetChanged()
    }

    override fun refreshDatas(datas: List<DATA>) {
        refreshDatas(datas, true)
    }

    override fun refreshDatas(datas: List<DATA>, notify: Boolean) {
//        _datas.clear()
//        _datas.addAll(datas)
        refreshDatas(notify)
    }

    override fun addData(data: DATA, notify: Boolean) {
        addDataAtPosition(data, -1, notify)
    }

    override fun addDataAtPosition(data: DATA, position: Int, notify: Boolean) {
        if (position >= 0) _datas.add(position, data)
        else _datas.add(data)

        val notifyPos = if (position >= 0) position else _datas.size - 1
        if (notify) notifyItemInserted(notifyPos)
    }

    override fun addDatas(datas: List<DATA>, notify: Boolean) {
        val start = _datas.size.also { UtilKLogWrapper.d(TAG, "addDatas: start $it") }
        _datas.addAll(datas).also { UtilKLogWrapper.d(TAG, "addDatas: size ${_datas.size}") }
        if (notify) notifyItemRangeInserted(start, _datas.size)
    }

    override fun removeData(data: DATA, notify: Boolean) {
        val position = _datas.indexOf(data)
        if (position != -1) removeDataAtPosition(position, notify)
    }

    override fun removeDataAtPosition(position: Int, notify: Boolean): DATA? {
        if (position < 0 || position >= _datas.size) return null
        val remove = _datas.removeAt(position)
        if (notify) notifyItemRemoved(position)
        return remove
    }

    override fun removeDatasAll(notify: Boolean) {
        if (notify) notifyItemRangeRemoved(0, _datas.size)
        _datas.clear()
    }

    override fun getData(position: Int): DATA? {
        if (position < 0 || position >= _datas.size) return null
        return _datas[position]
    }

    override fun getDatas(): List<DATA?> =
        _datas

    override fun onDataSelected(position: Int) {
        if (position < 0 || position >= _datas.size) return
        _selectItemPosition = position
        refreshDatas(true)
    }

    override fun getCurrentSelectPosition(): Int =
        _selectItemPosition

    ////////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHKLifecycle2VDB<VDB> {
        val binding = DataBindingUtil.inflate<VDB>(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false
        )
        return VHKLifecycle2VDB(binding)
    }

    override fun getItemCount() = if (_datas.isEmpty()) 0 else _datas.size

    override fun onBindViewHolder(holder: VHKLifecycle2VDB<VDB>, position: Int) {
        holder.vdb.setVariable(_brId, _datas[position])
        _listener?.invoke(holder, _datas[position], position, _selectItemPosition)
        holder.vdb.executePendingBindings()
    }

    override fun getItemViewType(position: Int) = _defaultLayout


//    fun onItemRangeChanged(newItemDatas: List<DATA>, positionStart: Int, itemCount: Int) {
//        _datas = newItemDatas
//        notifyItemChanged(positionStart, itemCount)
//    }
//
//    fun onItemRangeInserted(newItemDatas: List<DATA>, positionStart: Int, itemCount: Int) {
//        _datas = newItemDatas
//        notifyItemRangeInserted(positionStart, itemCount)
//    }
//
//    fun onItemRangeRemoved(newItemDatas: List<DATA>, positionStart: Int, itemCount: Int) {
//        _datas = newItemDatas
//        notifyItemRangeRemoved(positionStart, itemCount)
//    }
}