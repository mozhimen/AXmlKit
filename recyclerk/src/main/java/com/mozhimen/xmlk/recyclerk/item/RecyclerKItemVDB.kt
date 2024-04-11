package com.mozhimen.xmlk.recyclerk.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.mozhimen.xmlk.vhk.VHKRecyclerVDB

/**
 * @ClassName RecyclerKItemVB
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
typealias IRecyclerKItemVBListener<DATA, VDB> = (holder: VHKRecyclerVDB<VDB>, data: DATA, position: Int, currentSelectPos: Int) -> Unit

class RecyclerKItemVDB<DATA, VDB : ViewDataBinding>(
    val data: DATA,
    private val _brId: Int,
    private val _layoutId: Int,
    private val _selectItemPos: Int,
    private val _onBind: IRecyclerKItemVBListener<DATA, VDB>? = null
) : RecyclerKItem<VHKRecyclerVDB<VDB>>() {

    override fun onBindItem(holder: VHKRecyclerVDB<VDB>, position: Int) {
        super.onBindItem(holder, position)
        holder.vdb.setVariable(_brId, data)
        _onBind?.invoke(holder, data, position, _selectItemPos)
        holder.vdb.executePendingBindings()
    }

    override fun onCreateViewHolder(parent: ViewGroup): VHKRecyclerVDB<VDB> {
        return VHKRecyclerVDB(LayoutInflater.from(parent.context).inflate(getItemLayoutId(), parent, false))
    }

    override fun getItemLayoutId(): Int {
        return _layoutId
    }
}