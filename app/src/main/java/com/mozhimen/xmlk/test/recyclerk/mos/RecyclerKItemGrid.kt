package com.mozhimen.xmlk.test.recyclerk.mos

import android.view.View
import android.widget.ImageView
import com.mozhimen.xmlk.recyclerk.item.RecyclerKItem
import com.mozhimen.xmlk.vhk.VHKLifecycle2
import com.mozhimen.xmlk.test.R

/**
 * @ClassName DataKItemGrid
 * @Description TODO
 * @Author Kolin Zhao
 * @Version 1.0
 */
class RecyclerKItemGrid : RecyclerKItem<RecyclerKItemGrid.MyVHKLifecycle2>() {
    override fun onBindItem(holder: MyVHKLifecycle2, position: Int) {
        super.onBindItem(holder, position)
        holder.imageView.setImageResource(R.drawable.datak_item_grid)
    }

    override fun getItemLayoutId() = R.layout.item_recyclerk_grid

    class MyVHKLifecycle2(view: View) : VHKLifecycle2(view) {
        val imageView: ImageView = view.findViewById(R.id.datak_item_grid_img)
    }

    override fun getItemSpanSize(): Int {
        return 2
    }
}
