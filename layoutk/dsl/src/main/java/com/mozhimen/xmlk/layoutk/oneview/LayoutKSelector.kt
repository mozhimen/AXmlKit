package com.mozhimen.xmlk.layoutk.oneview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.fragment.app.Fragment

/**
 * @ClassName LayoutKSelector
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/9/3
 * @Version 1.0
 */

inline fun ViewGroup.layoutKSelector(autoAdd: Boolean = true, init: LayoutKSelector.() -> Unit) =
    LayoutKSelector(context).apply(init).also { addView(it) }

inline fun Context.layoutKSelector(init: LayoutKSelector.() -> Unit): LayoutKSelector =
    LayoutKSelector(this).apply(init)

inline fun Fragment.layoutKSelector(init: LayoutKSelector.() -> Unit) =
    context?.let { LayoutKSelector(it).apply(init) }

///////////////////////////////////////////////////////////////

open class LayoutKSelector @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {
    /**
     * the unique identifier for a [Selector]
     */
    var tag: String = "default tag"

    /**
     * the identifier for the [SelectorGroup] this [Selector] belongs to
     */
    var groupTag: String = "default group tag"

    /**
     * the [SelectorGroup] this [Selector] belongs to
     */
    var group: SelectorGroup? = null

    /**
     * the layout view for this [Selector]
     */
    var contentView: View? = null
        set(value) {
            field = value
            value?.let {
                addView(it, LayoutParams(MATCH_PARENT, MATCH_PARENT))
                setOnClickListener {
                    group?.onSelectorClick(this)
                }
            }
        }

    /**
     * it will be invoked when the selection state of this [Selector] has changed,
     * override it if you want customized effect of selected or unselected
     */
    var onStateChange: ((LayoutKSelector, Boolean) -> Unit)? = null

    init {
        contentView?.let {
            addView(it, LayoutParams(MATCH_PARENT, MATCH_PARENT))
            setOnClickListener {
                group?.onSelectorClick(this)
            }
        }
    }

    override fun setSelected(selected: Boolean) {
        val isPreSelected = isSelected
        super.setSelected(selected)
        if (isPreSelected != selected) {
            onStateChange?.invoke(this, selected)
        }
    }

    class SelectorGroup {

        companion object {
            /**
             * single choice mode, previous [Selector] will be unselected if a new one is selected
             */
            var MODE_SINGLE = { selectorGroup: SelectorGroup, selector: LayoutKSelector ->
                selectorGroup.run {
                    find(selector.groupTag)?.let { setSelected(it, false) }
                    setSelected(selector, true)
                }
            }

            /**
             * multiple choice mode, several [Selector] could be selected in one [SelectorGroup]
             */
            var MODE_MULTIPLE = { selectorGroup: SelectorGroup, selector: LayoutKSelector ->
                selectorGroup.setSelected(selector, !selector.isSelected)
            }
        }

        /**
         * the selected [Selector]s in this [SelectorGroup]
         */
        private var selectors = mutableListOf<LayoutKSelector>()

        /**
         * the choice mode of this [SelectorGroup], there are two default choice mode, which is [singleMode] and [multipleMode]
         */
        var choiceMode: ((SelectorGroup, LayoutKSelector) -> Unit)? = null


        var selectChangeListener: ((List<LayoutKSelector>, Boolean) -> Unit)? = null

        fun onSelectorClick(selector: LayoutKSelector) {
            choiceMode?.invoke(this, selector)
        }

        fun find(groupTag: String) = selectors.find { it.groupTag == groupTag }

        fun setSelected(selector: LayoutKSelector, select: Boolean) {
            if (select) selectors.add(selector) else selectors.remove(selector)
            selector.isSelected = select
            if (select) {
                selectChangeListener?.invoke(selectors, select)
            } else {
                selectChangeListener?.invoke(listOf(selector), select)
            }
        }
    }
}

