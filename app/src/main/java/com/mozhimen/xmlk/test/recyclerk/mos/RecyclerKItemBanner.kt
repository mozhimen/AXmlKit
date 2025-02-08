package com.mozhimen.xmlk.test.recyclerk.mos

import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.mozhimen.cachek.sharedpreferences.CacheKSP
import com.mozhimen.imagek.glide.loadImage_ofGlide
import com.mozhimen.xmlk.layoutk.banner.IBannerItemChangeListener
import com.mozhimen.xmlk.layoutk.banner.bases.BaseBannerItem
import com.mozhimen.xmlk.layoutk.banner.commons.IBannerBindListener
import com.mozhimen.xmlk.layoutk.banner.temps.PointIndicator
import com.mozhimen.xmlk.layoutk.banner.helpers.BannerViewHolder
import com.mozhimen.xmlk.recyclerk.item.RecyclerKItem
import com.mozhimen.xmlk.vhk.VHKLifecycle2VDB
import com.mozhimen.xmlk.test.R
import com.mozhimen.xmlk.test.databinding.ItemRecyclerkBannerBinding

/**
 * @ClassName DataKItemBanner
 * @Description TODO
 * @Author Kolin Zhao
 * @Version 1.0
 */
class RecyclerKItemBanner : RecyclerKItem<VHKLifecycle2VDB<ItemRecyclerkBannerBinding>>() {
    private val RECYCLERK_ITEM_BANNER_SP_NAME = "recyclerk_item_banner_sp_name"
    private var _index = 0
    private var _urls = arrayOf(
        "https://images.pexels.com/photos/2709388/pexels-photo-2709388.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/8721987/pexels-photo-8721987.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/6679876/pexels-photo-6679876.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/7078587/pexels-photo-7078587.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/6948010/pexels-photo-6948010.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
        "https://images.pexels.com/photos/7078486/pexels-photo-7078486.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
    )

    inner class BannerItem : BaseBannerItem()

    override fun onBindItem(holder: VHKLifecycle2VDB<ItemRecyclerkBannerBinding>, position: Int) {
        super.onBindItem(holder, position)
        val context = holder.itemView.context ?: return
        val moList: MutableList<BaseBannerItem> = ArrayList()
        for (i in 0..5) {
            val item = BannerItem()
            item.url = _urls[i % _urls.size]
            moList.add(item)
        }
        holder.vdb.itemRecyclerkBanner.apply {
            setBannerIndicator(PointIndicator(context))
            setAutoPlay(true)
            setIntervalTime(5000)
            setScrollDuration(3000)
            setBannerData(R.layout.item_layoutk_banner, moList)
            setBannerBindListener(object : IBannerBindListener {
                override fun onBannerBind(viewHolder: BannerViewHolder, item: BaseBannerItem, position: Int) {
                    val model = item as BannerItem
                    val imageView: ImageView = viewHolder.findViewById(R.id.item_layoutk_banner_img)
                    model.url.let { imageView.loadImage_ofGlide(it) }
                }
            })
            setPagerChangeListener(object : IBannerItemChangeListener {
                override fun onPageSelected(position: Int) {
                    _index = position
                    UtilKLogWrapper.d(TAG, "onPageSelected $position")
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): VHKLifecycle2VDB<ItemRecyclerkBannerBinding> {
        return VHKLifecycle2VDB(LayoutInflater.from(parent.context).inflate(getItemLayoutId(), parent, false))
    }

    override fun getItemLayoutId() = R.layout.item_recyclerk_banner

    override fun onViewDetachedFromWindow(holder: VHKLifecycle2VDB<ItemRecyclerkBannerBinding>) {
        CacheKSP.instance.with(RECYCLERK_ITEM_BANNER_SP_NAME).putInt("bannerIndex", _index)
    }

    override fun onViewAttachedToWindow(holder: VHKLifecycle2VDB<ItemRecyclerkBannerBinding>) {
        val index = CacheKSP.instance.with(RECYCLERK_ITEM_BANNER_SP_NAME).getInt("bannerIndex")
        UtilKLogWrapper.d(TAG, "onViewAttachedToWindow currentIndex $index")
        holder.vdb.itemRecyclerkBanner.setCurrentPosition(index, false)
    }

    override fun getItemSpanSize(): Int {
        return 2
    }
}
