package com.mozhimen.uicorek.test.recyclerk.mos

import android.widget.ImageView
import com.mozhimen.uicorek.recyclerk.item.RecyclerKItem
import com.mozhimen.uicorek.vhk.VHKRecycler
import com.mozhimen.uicorek.test.R

/**
 * @ClassName DataKItemActivity
 * @Description TODO
 * @Author Kolin Zhao
 * @Date 2021/9/2 14:44
 * @Version 1.0
 */
class RecyclerKItemActivity : RecyclerKItem<VHKRecycler>() {

    override fun onBindItem(holder: VHKRecycler, position: Int) {
        super.onBindItem(holder, position)
        holder.findViewById<ImageView>(R.id.datak_item_activity_img)?.setImageResource(R.drawable.datak_item_activity)
    }

    override fun getItemLayoutId() = R.layout.item_recyclerk_activity

    override fun getItemSpanSize(): Int {
        return 2
    }
}
