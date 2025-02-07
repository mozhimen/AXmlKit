package com.mozhimen.xmlk.test.recyclerk.mos

import android.widget.ImageView
import com.mozhimen.xmlk.recyclerk.item.RecyclerKItem
import com.mozhimen.xmlk.vhk.VHKLifecycle2
import com.mozhimen.xmlk.test.R

/**
 * @ClassName DataKItemImage
 * @Description TODO
 * @Author Kolin Zhao
 * @Version 1.0
 */
class RecyclerKItemImage(private var _spanCount: Int) : RecyclerKItem<VHKLifecycle2>() {

    override fun onBindItem(holder: VHKLifecycle2, position: Int) {
        super.onBindItem(holder, position)
        holder.findViewById<ImageView>(R.id.datak_item_image_img)?.setImageResource(R.drawable.datak_item_image)
    }

    override fun getItemLayoutId() =
        R.layout.item_recyclerk_image

    override fun getItemSpanSize() =
        _spanCount
}
