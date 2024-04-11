package com.mozhimen.xmlk.vhk

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes

/**
 * @ClassName VHK
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
class VHK private constructor(context: Context, parent: ViewGroup, layoutId: Int) {

    private val _views: SparseArray<View?> = SparseArray()//存储ListView 的 item中的View

    /**
     * 获取当前条目//存放convertView
     */
    var itemView: View = LayoutInflater.from(context).inflate(layoutId, parent, false).apply {
        tag = this@VHK
    }
        private set

    /**
     * 获取条目位置//游标
     */
    var itemPosition = 0
        private set

    ////////////////////////////////////////////////////////////////////////

    companion object {
        @JvmStatic
        fun bindView(context: Context, convertView: View?, parent: ViewGroup, layoutId: Int, position: Int): VHK {//绑定ViewHolder与item
            val holder: VHK
            if (convertView == null) {
                holder = VHK(context, parent, layoutId)
            } else {
                holder = convertView.tag as VHK
                holder.itemView = convertView
            }
            holder.itemPosition = position
            return holder
        }
    }

    ////////////////////////////////////////////////////////////////////////

    fun <T : View?> getView(@IdRes intResId: Int): T? {
        var view = _views[intResId] as? T?
        if (view == null) {
            view = itemView.findViewById<View>(intResId) as T
            _views.put(intResId, view)
        }
        return view
    }

    ////////////////////////////////////////////////////////////////////////

    /**
     * 设置文字
     */
    fun setText(@IdRes intResId: Int, text: CharSequence?): VHK {
        val view = getView<View>(intResId)!!
        if (view is TextView) {
            view.text = text
        }
        return this
    }

    /**
     * 设置图片
     */
    fun setImageResource(@IdRes intResId: Int, @DrawableRes intResDrawable: Int): VHK {
        val imageView = getView<View>(intResId)!!
        if (imageView is ImageView)
            imageView.setImageResource(intResDrawable)
        else
            imageView.setBackgroundResource(intResDrawable)
        return this
    }

    /**
     * 设置点击监听
     */
    fun setOnClickListener(@IdRes intResId: Int, listener: View.OnClickListener): VHK {
        getView<View>(intResId)!!.setOnClickListener(listener)
        return this
    }

    /**
     * 设置可见
     */
    fun setVisibility(@IdRes intResId: Int, visible: Int): VHK {
        getView<View>(intResId)!!.visibility = visible
        return this
    }

    /**
     * 设置标签
     */
    fun setTag(@IdRes intResId: Int, obj: Any): VHK {
        getView<View>(intResId)!!.tag = obj
        return this
    } //其他方法可自行扩展
}