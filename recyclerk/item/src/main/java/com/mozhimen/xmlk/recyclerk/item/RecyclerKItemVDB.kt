package com.mozhimen.xmlk.recyclerk.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.mozhimen.xmlk.vhk.VHKRecycler2VDB

typealias IRecyclerKItemVBListener<DATA, VDB> = (holder: VHKRecycler2VDB<VDB>, data: DATA, position: Int, currentSelectPos: Int) -> Unit

open class RecyclerKItemVDB<DATA, VDB : ViewDataBinding>(
    val data: DATA,
    private val _brId: Int,
    private val _layoutId: Int,
    private val _selectItemPos: Int,
    private val _onBind: IRecyclerKItemVBListener<DATA, VDB>? = null
) : RecyclerKItem<VHKRecycler2VDB<VDB>>() {

    override fun onBindItem(holder: VHKRecycler2VDB<VDB>, position: Int) {
        super.onBindItem(holder, position)
        holder.vdb.setVariable(_brId, data)
        _onBind?.invoke(holder, data, position, _selectItemPos)
        holder.vdb.executePendingBindings()
    }

    override fun onCreateViewHolder(parent: ViewGroup): VHKRecycler2VDB<VDB> {
        return VHKRecycler2VDB(LayoutInflater.from(parent.context).inflate(getItemLayoutId(), parent, false))
    }

    override fun getItemLayoutId(): Int {
        return _layoutId
    }
}