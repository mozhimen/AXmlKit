package com.mozhimen.xmlk.test.recyclerk.mos

import android.widget.ImageView
import com.mozhimen.xmlk.recyclerk.item.RecyclerKItem
import com.mozhimen.xmlk.vhk.VHKRecycler
import com.mozhimen.xmlk.test.R

/**
 * @ClassName DataKItemVideo
 * @Description TODO
 * @Author Kolin Zhao
 * @Version 1.0
 */
class RecyclerKItemVideo(private var _spanCount: Int) :
    RecyclerKItem<VHKRecycler>() {
    override fun onBindItem(holder: VHKRecycler, position: Int) {
        super.onBindItem(holder, position)
        holder.findViewById<ImageView>(R.id.datak_item_video_img)?.setImageResource(R.drawable.datak_item_video)
    }

    override fun getItemLayoutId() =
        R.layout.item_recyclerk_video

    override fun getItemSpanSize() =
        _spanCount
}
