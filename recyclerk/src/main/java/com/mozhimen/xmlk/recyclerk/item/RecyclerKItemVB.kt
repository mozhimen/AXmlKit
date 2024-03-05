package com.mozhimen.xmlk.recyclerk.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.mozhimen.xmlk.vhk.VHKRecyclerVDB

/**
 * @ClassName RecyclerKItemVB
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/4/4 14:32
 * @Version 1.0
 */
typealias IRecyclerKItemVBListener<DATA, VB> = (holder: VHKRecyclerVDB<VB>, data: DATA, position: Int, currentSelectPos: Int) -> Unit

class RecyclerKItemVB<DATA, VB : ViewDataBinding>(
    val data: DATA,
    private val _brId: Int,
    private val _layoutId: Int,
    private val _selectItemPos: Int,
    private val _onBind: IRecyclerKItemVBListener<DATA, VB>? = null
) : RecyclerKItem<VHKRecyclerVDB<VB>>() {

    override fun onBindItem(holder: VHKRecyclerVDB<VB>, position: Int) {
        super.onBindItem(holder, position)
        holder.vdb.setVariable(_brId, data)
        _onBind?.invoke(holder, data, position, _selectItemPos)
        holder.vdb.executePendingBindings()
    }

    override fun onCreateViewHolder(parent: ViewGroup): VHKRecyclerVDB<VB> {
        return VHKRecyclerVDB(LayoutInflater.from(parent.context).inflate(getItemLayoutId(), parent, false))
    }

    override fun getItemLayoutId(): Int {
        return _layoutId
    }
}