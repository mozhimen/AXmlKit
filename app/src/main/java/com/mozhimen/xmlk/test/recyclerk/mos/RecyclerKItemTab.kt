package com.mozhimen.xmlk.test.recyclerk.mos

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mozhimen.xmlk.recyclerk.item.RecyclerKItem
import com.mozhimen.xmlk.vhk.VHKLifecycle2VDB
import com.mozhimen.xmlk.test.R
import com.mozhimen.xmlk.test.databinding.ItemRecyclerkTabBinding

/**
 * @ClassName DataKItemTab
 * @Description TODO
 * @Author Kolin Zhao
 * @Version 1.0
 */
class RecyclerKItemTab : RecyclerKItem<VHKLifecycle2VDB<ItemRecyclerkTabBinding>>() {

    override fun onBindItem(holder: VHKLifecycle2VDB<ItemRecyclerkTabBinding>, position: Int) {
        super.onBindItem(holder, position)
        //holder.binding.setVariable(BR.XXX, XXX)
        holder.vdb.datakItemTabImg.setImageResource(R.drawable.datak_item_tab)
    }

    override fun onCreateViewHolder(parent: ViewGroup): VHKLifecycle2VDB<ItemRecyclerkTabBinding> {
        return VHKLifecycle2VDB(LayoutInflater.from(parent.context).inflate(getItemLayoutId(), parent, false))
    }

    override fun getItemLayoutId() =
        R.layout.item_recyclerk_tab

    override fun getItemSpanSize(): Int {
        return 2
    }
}