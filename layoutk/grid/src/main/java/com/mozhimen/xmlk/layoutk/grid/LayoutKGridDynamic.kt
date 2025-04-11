package com.mozhimen.xmlk.layoutk.grid

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridLayout
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import com.mozhimen.kotlin.elemk.android.os.cons.CVersCode
import com.mozhimen.kotlin.lintk.optins.OApiCall_Recycle
import com.mozhimen.kotlin.utilk.android.os.UtilKBuildVersion
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.xmlk.adapterk.list.AdapterKList
import com.mozhimen.xmlk.basic.commons.ILayoutK
import kotlin.math.roundToInt


/**
 * @ClassName LayoutKGridDynamicGrid
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/4/11
 * @Version 1.0
 */
class LayoutKGridDynamic @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    GridLayout(context, attrs, defStyleAttr), ILayoutK {

    @OptIn(OApiCall_Recycle::class)
    private var _adapter: DynamicGridLayoutAdapter<*>? = null
    private var _space = 0f
    private var _onItemClickListener: OnItemClickListener? = null

    ///////////////////////////////////////////////////////////////////////////////

    init {
        initAttrs(attrs)
    }

    ///////////////////////////////////////////////////////////////////////////////

    override fun initAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LayoutKGridDynamic)
            _space = typedArray.getDimension(R.styleable.LayoutKGridDynamic_layoutKGridDynamic_spacing, _space)
            typedArray.recycle()
        }
    }

    ///////////////////////////////////////////////////////////////////////////////

    @OptIn(OApiCall_Recycle::class)
    fun setAdapter(adapter: DynamicGridLayoutAdapter<*>) {
        _adapter = adapter
        loadView()
    }

    ///////////////////////////////////////////////////////////////////////////////

    @OptIn(OApiCall_Recycle::class)
    private fun loadView() {
        if (_adapter == null) {
            UtilKLogWrapper.e(TAG, "Adapter can not be null!")
            return
        }
        if (UtilKBuildVersion.isAfterV_21_5_L()) {
            loadViewAfter21()
        } else {
            loadViewBefore21()
        }
    }

    @RequiresApi(CVersCode.V_21_5_L)
    @OptIn(OApiCall_Recycle::class)
    private fun loadViewAfter21() {
        val realRowCount = kotlin.math.ceil(_adapter!!.count.toFloat() / columnCount.toFloat()).toInt()
        val lastRowFirstIndex = (realRowCount - 1) * columnCount
        val rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f) // 自动行，权重1
        val columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f) // 自动列，权重1
        for (index in 0 until _adapter!!.count) {
            //创建ViewHolder
            val view: View = _adapter!!.getView(index, null, this)
            val lp = GridLayout.LayoutParams(rowSpec, columnSpec)
            lp.width = 0
            lp.height = GridLayout.LayoutParams.WRAP_CONTENT
            val rightMargin = if ((index + 1) % columnCount == 0) 0 else _space
            val bottomMargin = if (index in lastRowFirstIndex.._adapter!!.count) 0 else _space
            lp.setMargins(0, 0, rightMargin.toInt(), bottomMargin.toInt())
            addView(view, lp)
        }
    }

    @OptIn(OApiCall_Recycle::class)
    private fun loadViewBefore21() {
        val realRowCount = kotlin.math.ceil(_adapter!!.count.toFloat() / columnCount.toFloat()).toInt()
        val lastRowFirstIndex = (realRowCount - 1) * columnCount
        for (index in 0 until _adapter!!.count) {
            //创建ViewHolder
            val view: View = _adapter!!.getView(index, null, this)
            val lp = GridLayout.LayoutParams()
            lp.width = 0
            lp.height = GridLayout.LayoutParams.WRAP_CONTENT
            val rightMargin = if ((index + 1) % columnCount == 0) 0 else _space
            val bottomMargin = if (index in lastRowFirstIndex.._adapter!!.count) 0 else _space
            lp.setMargins(0, 0, rightMargin.toInt(), bottomMargin.toInt())
            addView(view, lp)
        }
    }

    @OApiCall_Recycle
    abstract class DynamicGridLayoutAdapter<T>(datas: MutableList<T>, @LayoutRes layoutId: Int) : AdapterKList<T>(datas, layoutId)
}
