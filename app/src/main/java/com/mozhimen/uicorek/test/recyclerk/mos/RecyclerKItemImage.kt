package com.mozhimen.uicorek.test.recyclerk.mos

import android.widget.ImageView
import com.mozhimen.uicorek.recyclerk.item.RecyclerKItem
import com.mozhimen.uicorek.vhk.VHKRecycler
import com.mozhimen.uicorek.test.R

/**
 * @ClassName DataKItemImage
 * @Description TODO
 * @Author Kolin Zhao
 * @Date 2021/9/2 15:33
 * @Version 1.0
 */
class RecyclerKItemImage(private var _spanCount: Int) : RecyclerKItem<VHKRecycler>() {

    override fun onBindItem(holder: VHKRecycler, position: Int) {
        super.onBindItem(holder, position)
        holder.findViewById<ImageView>(R.id.datak_item_image_img)?.setImageResource(R.drawable.datak_item_image)
    }

    override fun getItemLayoutId() =
        R.layout.item_recyclerk_image

    override fun getItemSpanSize() =
        _spanCount
}
