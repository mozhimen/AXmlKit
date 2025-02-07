package com.mozhimen.xmlk.test.recyclerk.mos

import android.widget.ImageView
import com.mozhimen.xmlk.recyclerk.item.RecyclerKItem
import com.mozhimen.xmlk.vhk.VHKLifecycle2
import com.mozhimen.xmlk.test.R

/**
 * @ClassName DataKItemActivity
 * @Description TODO
 * @Author Kolin Zhao
 * @Version 1.0
 */
class RecyclerKItemActivity : RecyclerKItem<VHKLifecycle2>() {

    override fun onBindItem(holder: VHKLifecycle2, position: Int) {
        super.onBindItem(holder, position)
        holder.findViewById<ImageView>(R.id.datak_item_activity_img)?.setImageResource(R.drawable.datak_item_activity)
    }

    override fun getItemLayoutId() = R.layout.item_recyclerk_activity

    override fun getItemSpanSize(): Int {
        return 2
    }
}
