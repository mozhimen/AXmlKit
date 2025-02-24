package com.mozhimen.xmlk.layoutk.spinner

import android.content.Context
import android.content.res.Resources.Theme
import android.util.AttributeSet
import android.widget.ArrayAdapter
import com.mozhimen.kotlin.elemk.kotlin.cons.CSuppress

/**
 * @ClassName AdapterKSpinner
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/2/6 15:56
 * @Version 1.0
 */
class LayoutKSpinner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.spinnerStyle, popupTheme: Theme? = null
) : androidx.appcompat.widget.AppCompatSpinner(context, attrs, defStyleAttr, MODE_DROPDOWN, popupTheme) {

    private var _spinnerItemLayout = R.layout.layoutk_spinner_item
    private var _items = arrayListOf<String>()
    private var _arrayAdapter: ArrayAdapter<String>? = null

    /////////////////////////////////////////////////////////////////

    init {
        dropDownWidth = LayoutParams.MATCH_PARENT
    }

    /////////////////////////////////////////////////////////////////


    fun setLayout(layoutId: Int): LayoutKSpinner {
        _arrayAdapter = ArrayAdapter(context, layoutId.also { _spinnerItemLayout = it }, _items).also { this.adapter = it }
        return this
    }

    fun setEntries(list: List<String>): LayoutKSpinner {
        _items.clear()
        _items.addAll(list)
        if (_arrayAdapter != null) {
            _arrayAdapter!!.apply {
                clear()
                addAll(_items)
                notifyDataSetChanged()
            }
        } else {
            _arrayAdapter = ArrayAdapter(context, _spinnerItemLayout, _items).also { this.adapter = it }
        }
        return this
    }

    fun setSelectItem(obj: String) {
        setSelectItem(_items.indexOf(obj))
    }

    fun setSelectItem(position: Int) {
        if (position in 0 until _items.size) {
            this.setSelection(position)
        }
    }

    @Suppress(CSuppress.UNCHECKED_CAST)
    fun getSelectItem(): String? {
        return if (_items.isNotEmpty()) this.selectedItem as? String? else null
    }
}