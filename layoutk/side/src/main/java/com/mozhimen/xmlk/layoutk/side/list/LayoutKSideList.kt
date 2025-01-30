package com.mozhimen.xmlk.layoutk.side.list

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.util.SparseIntArray
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mozhimen.kotlin.elemk.commons.IAB_Listener
import com.mozhimen.kotlin.lintk.optins.permission.OPermission_INTERNET
import com.mozhimen.kotlin.utilk.android.widget.applyTypeface
import com.mozhimen.kotlin.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.mozhimen.imagek.glide.loadImageComplex_ofGlide
import com.mozhimen.xmlk.bases.BaseLayoutKLinear
import com.mozhimen.xmlk.layoutk.side.R
import com.mozhimen.xmlk.vhk.VHKRecycler
import com.mozhimen.xmlk.layoutk.side.list.helpers.SideAttrsParser
import com.mozhimen.xmlk.layoutk.side.list.temps.SideSubItemDecoration
import com.mozhimen.xmlk.layoutk.side.list.mos.*

typealias ILayoutKSideListListener = IAB_Listener<VHKRecycler, MSideSubContent?>//(viewHolder: VHKRecycler, content: MSideSubContent?) -> Unit

/**
 * @ClassName LayoutKSideList
 * @Description TODO
 * @Author Kolin Zhao / Mozhimen
 * @Version 1.0
 */
@OPermission_INTERNET
class LayoutKSideList @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseLayoutKLinear(context, attrs, defStyleAttr) {

    private val _menuView by lazy_ofNone { RecyclerView(context) }
    private val _contentView by lazy_ofNone { RecyclerView(context) }
    private val _attr: MSideAttrs by lazy_ofNone { SideAttrsParser.parseAttrs(context, attrs) }
    private val _subSpanSizeOffset = SparseIntArray()

    private var _menuLayoutId = R.layout.layoutk_side_menu_item
    private var _contentLayoutId = R.layout.layoutk_side_content_item
    private var _spanCount = 3
    private var _layoutKSideSubItemLayoutManager: RecyclerView.LayoutManager? = null
    private var _layoutKSideListListener: ILayoutKSideListListener? = null

    init {
        initView()
    }

    override fun initView() {
        orientation = HORIZONTAL

        _menuView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        _menuView.overScrollMode = View.OVER_SCROLL_NEVER
        _menuView.itemAnimator = null

        _contentView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        _contentView.overScrollMode = View.OVER_SCROLL_NEVER
        _contentView.itemAnimator = null

        addView(_menuView)
        addView(_contentView)
    }

    /**
     * 获取MenuView
     * @return RecyclerView
     */
    fun getMenuView(): RecyclerView = _menuView

    /**
     * 获取ContentView
     * @return RecyclerView
     */
    fun getContentView(): RecyclerView = _contentView

    fun bindData(
        mo: MSide,
        menuLayoutId: Int? = null,
        contentLayoutId: Int? = null,
        spanCount: Int? = null,
        layoutManager: RecyclerView.LayoutManager? = null,
        listener: ILayoutKSideListListener? = null
    ) {
        menuLayoutId?.let { _menuLayoutId = it }
        contentLayoutId?.let { _contentLayoutId = it }
        spanCount?.let {
            _spanCount = it
        }
        layoutManager?.let {
            _layoutKSideSubItemLayoutManager = it
        }
        listener?.let { _layoutKSideListListener = it }
        bindDataMenuView(mo)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bindDataContentView(
        mo: MSideMenu
    ) {
        if (_contentView.layoutManager == null) {
            _contentView.layoutManager = _layoutKSideSubItemLayoutManager ?: GridLayoutManager(context, _spanCount)
            if (_contentView.layoutManager is GridLayoutManager) {
                (_contentView.layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        val spanSize: Int
                        val subName: String? = getSubNameByPosition(mo, position)
                        val nextSubName: String? = getSubNameByPosition(mo, position + 1)

                        if (TextUtils.equals(subName, nextSubName)) {
                            spanSize = 1
                        } else {
                            //当前位置和 下一个位置 不再同一个分组
                            //1 .要拿到当前组 position （所在组）在 _subSpanSizeOffset 的索引下标
                            //2 .拿到 当前组前面一组 存储的 spanSizeOffset 偏移量
                            //3 .给当前组最后一个item 分配 spanSize count

                            val indexOfKey = _subSpanSizeOffset.indexOfKey(position)
                            val size = _subSpanSizeOffset.size()
                            val lastGroupOffset = if (size <= 0) 0
                            else if (indexOfKey >= 0) {
                                //说明当前组的偏移量记录，已经存在了 _subSpanSizeOffset ，这个情况发生在上下滑动，
                                if (indexOfKey == 0) 0 else _subSpanSizeOffset.valueAt(indexOfKey - 1)
                            } else {
                                //说明当前组的偏移量记录，还没有存在于 _subSpanSizeOffset ，这个情况发生在 第一次布局的时候
                                //得到前面所有组的偏移量之和
                                _subSpanSizeOffset.valueAt(size - 1)
                            }
                            //3-(6+5%3)第几列=0,1,2
                            spanSize = _spanCount - (position + lastGroupOffset) % _spanCount
                            if (indexOfKey < 0) {
                                //得到当前组 和前面所有组的spanSize 偏移量之和
                                val groupOffset = lastGroupOffset + spanSize - 1
                                _subSpanSizeOffset.put(position, groupOffset)
                            }
                        }
                        return spanSize
                    }
                }
            }
            _contentView.adapter = SideContentAdapter(mo)
            _contentView.addItemDecoration(
                SideSubItemDecoration(_spanCount, _attr.subHeight, _attr.subMarginStart, _attr.subTextSize, _attr.subTextColor) { position ->
                    getSubNameByPosition(mo, position)
                })
        }
        val contentAdapter = _contentView.adapter as SideContentAdapter
        contentAdapter.update(mo)
        contentAdapter.notifyDataSetChanged()
        _contentView.scrollToPosition(0)
    }

    private fun bindDataMenuView(
        mo: MSide,
    ) {
        _menuView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        _menuView.adapter = SideMenuAdapter(mo, _menuLayoutId)
    }

    inner class SideMenuAdapter(
        private val _mo: MSide,
        private val _layoutId: Int,
    ) : RecyclerView.Adapter<VHKRecycler>() {
        private var _currentSelectIndex = 0//本次选中的item的位置
        private var _lastSelectIndex = 0//上一次选中的item的位置

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHKRecycler {
            val itemView = LayoutInflater.from(context).inflate(_layoutId, parent, false)
            val layoutParams = RecyclerView.LayoutParams(_attr.menuWidth, _attr.menuHeight)

            itemView.layoutParams = layoutParams
            itemView.setBackgroundColor(_attr.menuItemBgColor)
            itemView.findViewById<TextView>(R.id.layoutk_side_menu_item_title)?.setTextColor(_attr.menuItemTextColor)
            itemView.findViewById<ImageView>(R.id.layoutk_side_menu_item_indicator)?.setImageDrawable(_attr.menuItemIndicator)
            return VHKRecycler(itemView)
        }

        override fun getItemCount(): Int = _mo.menus.size

        override fun onBindViewHolder(holder: VHKRecycler, @SuppressLint("RecyclerView") position: Int) {
            holder.itemView.setOnClickListener {
                _currentSelectIndex = position
                notifyItemChanged(position)
                notifyItemChanged(_lastSelectIndex)
            }

            //applyItemAttr
            if (_currentSelectIndex == position) {
                bindDataContentView(_mo.menus[_currentSelectIndex])
                _lastSelectIndex = _currentSelectIndex
            }
            applyMenuItemAttr(position, holder)
            bindMenuView(holder, position)
        }

        private fun bindMenuView(holder: VHKRecycler, position: Int) {
            if (position in _mo.menus.indices)
                holder.findViewByIdOrNull<TextView>(R.id.layoutk_side_menu_item_title)?.text = _mo.menus[position].menuName
        }

        private fun applyMenuItemAttr(position: Int, holder: VHKRecycler) {
            val selected = position == _currentSelectIndex
            val titleView: TextView? = holder.itemView.findViewById(R.id.layoutk_side_menu_item_title)
            val indicatorView: ImageView? = holder.itemView.findViewById(R.id.layoutk_side_menu_item_indicator)

            indicatorView?.visibility = if (selected) View.VISIBLE else View.GONE
            titleView?.apply {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, (if (selected) _attr.menuItemTextSizeSelect else _attr.menuItemTextSize).toFloat())
                if (selected) applyTypeface(Typeface.BOLD) else applyTypeface()
            }
            holder.itemView.setBackgroundColor(if (selected) _attr.menuItemBgColorSelect else _attr.menuItemBgColor)
            titleView?.isSelected = selected
        }
    }

    inner class SideContentAdapter(
        private var _mo: MSideMenu
    ) : RecyclerView.Adapter<VHKRecycler>() {

        fun update(mo: MSideMenu) {
            this._mo = mo
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHKRecycler {
            val itemView = LayoutInflater.from(context).inflate(_contentLayoutId, parent, false)
            itemView.findViewById<TextView>(R.id.layoutk_side_content_item_txt)?.apply {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, _attr.contentTextSize.toFloat())
                setTextColor(_attr.contentTextColor)
            }
            return VHKRecycler(itemView)
        }

        override fun getItemCount(): Int {
            var count = 0
            for (sub in _mo.menuSubs) count += sub.subContents.size
            return count
        }

        override fun onBindViewHolder(holder: VHKRecycler, position: Int) {
            bindContentView(holder, position)
            holder.itemView.setOnClickListener {
                _layoutKSideListListener?.invoke(holder, getContentByPosition(_mo, position))
            }
        }

        private fun bindContentView(holder: VHKRecycler, position: Int) {
            val contentMo = getContentByPosition(_mo, position)
            holder.findViewByIdOrNull<TextView>(R.id.layoutk_side_content_item_txt)?.text = contentMo?.contentName ?: "暂无数据"
            holder.findViewByIdOrNull<ImageView>(R.id.layoutk_side_content_item_img)
                ?.loadImageComplex_ofGlide(contentMo?.contentImageUrl ?: "", placeholder = com.mozhimen.xmlk.layoutk.refresh.R.drawable.layoutk_refresh_loading, error = com.mozhimen.xmlk.layoutk.empty.R.drawable.layoutk_empty)
        }

        override fun onViewAttachedToWindow(holder: VHKRecycler) {
            super.onViewAttachedToWindow(holder)
            val remainScope = width - paddingLeft - paddingRight - _attr.menuWidth
            val layoutManager = _contentView.layoutManager
            var spanCount = 0
            if (layoutManager is GridLayoutManager) {
                spanCount = layoutManager.spanCount
            } else if (layoutManager is StaggeredGridLayoutManager) {
                spanCount = layoutManager.spanCount
            }

            if (spanCount > 0) {
                val itemWidth = remainScope / spanCount
                //创建content itemView, 设置它的layoutParams 的原因,是防止图片未加载之前, 列表滑动时,上下闪动的效果
                val layoutParams = holder.itemView.layoutParams
                layoutParams.width = itemWidth
                layoutParams.height = (itemWidth * _attr.contentImgRatio).toInt()
                holder.itemView.layoutParams = layoutParams
            }
        }
    }

    private fun getSubNameByPosition(mo: MSideMenu, position: Int): String? {
        val sideContentMo = getContentByPosition(mo, position)
        return sideContentMo?.let { contentMo ->
            mo.menuSubs.find { it.subId == contentMo.fatherId }?.subName
        }
    }

    private fun getContentByPosition(mo: MSideMenu, position: Int): MSideSubContent? {
        val lengthList = arrayListOf<Int>()//3,4,6
        for (sub in mo.menuSubs) lengthList.add(sub.subContents.size)
        if (lengthList.isNotEmpty()) {
            var totalSize = lengthList[0]
            lengthList.forEachIndexed { index, item ->
                if (index != 0) totalSize += item
                if (position in 0 until totalSize) {
                    var preSize = 0
                    if (index > 0)
                        for (i in 0 until index) preSize += lengthList[i]
                    return mo.menuSubs[index].subContents[position - preSize]
                }
            }
        }
        return null
    }
}


