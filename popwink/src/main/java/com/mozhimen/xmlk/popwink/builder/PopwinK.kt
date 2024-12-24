package com.mozhimen.xmlk.popwink.builder

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.annotation.DimenRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.Px
import com.mozhimen.kotlin.elemk.commons.IExt_Listener
import com.mozhimen.kotlin.lintk.annors.Dp


/**
 * @ClassName BasePopwinK
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/12/24
 * @Version 1.0
 */
class PopwinK : PopupWindow {
    constructor(builder: Builder) : super(builder.context) {
        builder.view!!.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        contentView = builder.view
        width = if (builder.width == 0) ViewGroup.LayoutParams.WRAP_CONTENT else builder.width
        height = if (builder.height == 0) ViewGroup.LayoutParams.WRAP_CONTENT else builder.height
        if (builder.isOutsideTouchable) {
            setBackgroundDrawable(ColorDrawable(0x00000000)) //设置透明背景
            isOutsideTouchable = builder.isOutsideTouchable //设置outside可点击
        }
        isFocusable = builder.isFocusable
        isTouchable = builder.isTouchable

        if (builder.animationStyle != 0) {
            animationStyle = builder.animationStyle
        }
        if (builder.onDismissListener!=null){
            setOnDismissListener(builder.onDismissListener)
        }
    }

    /////////////////////////////////////////////////////////////////////////////

    class Builder(internal val context: Context) {
        internal var height: Int = 0
        internal var width: Int = 0
        internal var isOutsideTouchable: Boolean = true
        internal var isFocusable: Boolean = true
        internal var isTouchable: Boolean = true
        internal var view: View? = null
        internal var animationStyle: Int = 0
        internal var onDismissListener: OnDismissListener? = null

        fun view(@LayoutRes intLayoutRes: Int): Builder {
            return view(intLayoutRes, null)
        }

        fun view(@LayoutRes intLayoutRes: Int, generator: IExt_Listener<View>? = null): Builder {
            return view(LayoutInflater.from(context).inflate(intLayoutRes, null), generator)
        }

        fun view(view: View): Builder {
            return view(view, null)
        }

        fun view(view: View, generator: IExt_Listener<View>? = null): Builder {
            this.view = view.apply { generator?.invoke(this) }
            return this
        }

        fun width_dp(@Dp value: Int): Builder {
            this.width = value
            return this
        }

        fun height_dp(@Dp value: Int): Builder {
            this.height = value
            return this
        }

        fun width_px(@Px value: Int): Builder {
            this.width = value
            return this
        }

        fun height_px(@Px value: Int): Builder {
            this.height = value
            return this
        }

        fun width_dimen(@DimenRes intDimenRes: Int): Builder {
            width = context.resources.getDimensionPixelOffset(intDimenRes)
            return this
        }

        fun height_dimen(@DimenRes intDimenRes: Int): Builder {
            this.height = context.resources.getDimensionPixelOffset(intDimenRes)
            return this
        }

        fun isOutsideTouchable(value: Boolean): Builder {
            isOutsideTouchable = value
            return this
        }

        fun isFocusable(value: Boolean): Builder {
            isFocusable = value
            return this
        }

        fun isTouchable(value: Boolean): Builder {
            isTouchable = value
            return this
        }

        fun animationStyle(value: Int): Builder {
            animationStyle = value
            return this
        }

        fun setOnClickListener(@IdRes intIdRes: Int, listener: View.OnClickListener?): Builder {
            view?.findViewById<View>(intIdRes)?.setOnClickListener(listener)
            return this
        }

        fun setOnDismissListener(listener: OnDismissListener): Builder {
            onDismissListener = listener
            return this
        }

        fun build(): PopwinK? {
            view ?: return null
            return PopwinK(this)
        }
    }

    override fun getWidth(): Int {
        return contentView.measuredWidth
    }
}

