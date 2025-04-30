package com.mozhimen.xmlk.layoutk.linear

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.mozhimen.kotlin.lintk.optins.OApiCall_Recycle
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.xmlk.adapterk.list.AdapterKList
import com.mozhimen.xmlk.basic.commons.ILayoutK

/**
 * @ClassName LayoutKLinear
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/4/27
 * @Version 1.0
 */
class LayoutKLinearDynamic @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr), ILayoutK {

    @OptIn(OApiCall_Recycle::class)
    private var _adapter: DynamicLinearLayoutAdapter<*>? = null
    private var _space = 0f

    ///////////////////////////////////////////////////////////////////////////////

    init {
        initAttrs(attrs)
    }

    ///////////////////////////////////////////////////////////////////////////////

    override fun initAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LayoutKLinearDynamic)
            _space = typedArray.getDimension(R.styleable.LayoutKLinearDynamic_layoutKLinearDynamic_spacing, _space)
            typedArray.recycle()
        }
    }

    ///////////////////////////////////////////////////////////////////////////////

    @OptIn(OApiCall_Recycle::class)
    fun setAdapter(adapter: DynamicLinearLayoutAdapter<*>) {
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
        for (index in 0 until _adapter!!.count) {
            //创建ViewHolder
            val view: View = _adapter!!.getView(index, null, this)
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            val bottomMargin = if (index >= (_adapter!!.count-1)) 0 else _space
            lp.setMargins(0, 0, 0, bottomMargin.toInt())
            addView(view, lp)
        }
    }

    ///////////////////////////////////////////////////////////////////////////////

    @OApiCall_Recycle
    abstract class DynamicLinearLayoutAdapter<T>(datas: MutableList<T>, @LayoutRes layoutId: Int) : AdapterKList<T>(datas, layoutId)
}