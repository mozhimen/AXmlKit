package com.mozhimen.xmlk.recyclerk.list

import android.content.Context
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.androidx.lifecycle.handleLifecycleEventOnDestroy
import com.mozhimen.kotlin.utilk.androidx.lifecycle.handleLifecycleEventOnStart
import com.mozhimen.kotlin.utilk.commons.IUtilK
import com.mozhimen.xmlk.recyclerk.list.commons.IOnItemChildClickListener
import com.mozhimen.xmlk.recyclerk.list.commons.IOnItemChildLongClickListener
import com.mozhimen.xmlk.recyclerk.list.commons.IOnItemClickListener
import com.mozhimen.xmlk.recyclerk.list.commons.IOnItemLongClickListener
import com.mozhimen.xmlk.vhk.VHKLifecycle

/**
 * @ClassName BaseListAdapterVB
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/7 2:12
 * @Version 1.0
 */
abstract class RecyclerKListAdapter<DATA : Any,VH : VHKLifecycle> constructor(diffUtil: DiffUtil.ItemCallback<DATA>) : ListAdapter<DATA, VH>(diffUtil) , LifecycleOwner,IUtilK {

    private var _recyclerView: RecyclerView? = null

    val recyclerView get() = _recyclerView

    val context get() = _recyclerView?.context

    ///////////////////////////////////////////////////////

    private var _lifecycleRegistry: LifecycleRegistry? = null
    val lifecycleRegistry: LifecycleRegistry
        get() = _lifecycleRegistry ?: LifecycleRegistry(this).also {
            _lifecycleRegistry = it
        }

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    //////////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
//        val view = inflateView(parent, viewType)
        return onCreateViewHolder(parent.context, parent, viewType).apply {
            bindViewClickListener(this, viewType)
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, getItem(position), position)
    }

//    override fun onBindViewHolder(holder: VH, position: Int) {
//        if (position >= currentList.size) return
//        val data = getItem(position)
//        applyTry {
//            holder.onBind(data, position)
//        }
//    }

    @CallSuper
    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            onBindViewHolder(holder, getItem(position), position, payloads)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        _recyclerView = recyclerView
        lifecycleRegistry.handleLifecycleEventOnStart()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        _recyclerView = null
        lifecycleRegistry.handleLifecycleEventOnDestroy()
    }

    override fun onViewAttachedToWindow(holder: VH) {
        holder.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        holder.onViewDetachedFromWindow()
    }

    override fun onViewRecycled(holder: VH) {
        holder.onViewRecycled()
    }

    //////////////////////////////////////////////////////////////////////////////

    protected abstract fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH

//    protected abstract fun viewHolder(@LayoutRes layout: Int, view: View): VH
//    protected abstract fun layout(position: Int): Int


    @CallSuper
    protected open fun onBindViewHolder(holder: VH, item: DATA?, position: Int) {
        UtilKLogWrapper.d(TAG, "onBindViewHolderInner: holder $holder item $holder position $holder")
        holder.onBind()
    }

    protected open fun onBindViewHolder(holder: VH, item: DATA?, position: Int, payloads: List<Any>) {
        UtilKLogWrapper.d(TAG, "onBindViewHolderInner: holder $holder item $holder position $holder payloads $payloads")
    }

    //////////////////////////////////////////////////////////////////////////////

    fun getPosition(viewHolder: VH): Int? {
        val position = viewHolder.bindingAdapterPosition
        if (position == RecyclerView.NO_POSITION) {
            return null
        }
        return position
    }

//    override fun getItemViewType(position: Int): Int {
//        return layout(position)
//    }

//    private fun inflateView(viewGroup: ViewGroup, @LayoutRes viewType: Int): View {
//        val layoutInflater = LayoutInflater.from(viewGroup.context)
//        return layoutInflater.inflate(viewType, viewGroup, false)
//    }

    //////////////////////////////////////////////////////////////////////////////

    private var _onItemClickListener: IOnItemClickListener<DATA, RecyclerKListAdapter<*, *>>? = null
    private var _onItemLongClickListener: IOnItemLongClickListener<DATA, RecyclerKListAdapter<*, *>>? = null
    private var _onItemChildClickListeners: SparseArray<IOnItemChildClickListener<DATA, RecyclerKListAdapter<*, *>>>? = null
    private var _onItemChildLongClickListeners: SparseArray<IOnItemChildLongClickListener<DATA, RecyclerKListAdapter<*, *>>>? = null

    //////////////////////////////////////////////////////////////////////////////

    /**
     * override this method if you want to override click event logic
     * 如果你想重新实现 item 点击事件逻辑，请重写此方法
     */
    protected open fun onItemClick(v: View, position: Int) {
        _onItemClickListener?.onClick(this, v, position)
    }

    /**
     * override this method if you want to override longClick event logic
     * 如果你想重新实现 item 长按事件逻辑，请重写此方法
     */
    protected open fun onItemLongClick(v: View, position: Int): Boolean {
        return _onItemLongClickListener?.onLongClick(this, v, position) ?: false
    }

    protected open fun onItemChildClick(v: View, position: Int) {
        _onItemChildClickListeners?.get(v.id)?.onChildClick(this, v, position)
    }

    protected open fun onItemChildLongClick(v: View, position: Int): Boolean {
        return _onItemChildLongClickListeners?.get(v.id)?.onChildLongClick(this, v, position) ?: false
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     * 绑定 item 点击事件
     */
    @CallSuper
    protected open fun bindViewClickListener(holder: VH, viewType: Int) {
        _onItemClickListener?.let {
            holder.itemView.setOnClickListener { view ->
                getPosition(holder)?.let {
                    onItemClick(view, it)
                }
            }
        }
        _onItemLongClickListener?.let {
            holder.itemView.setOnLongClickListener { view ->
                getPosition(holder)?.let {
                    onItemLongClick(view, it)
                } ?: false
            }
        }
        _onItemChildClickListeners?.let {
            for (i in 0 until it.size()) {
                val id = it.keyAt(i)
                holder.itemView.findViewById<View>(id)?.let { v1 ->
                    v1.setOnClickListener { v2 ->
                        getPosition(holder)?.let { pos ->
                            onItemChildClick(v2, pos)
                        }
                    }
                }
            }
        }
        _onItemChildLongClickListeners?.let {
            for (i in 0 until it.size()) {
                val id = it.keyAt(i)
                holder.itemView.findViewById<View>(id)?.let { v1 ->
                    v1.setOnLongClickListener { v2 ->
                        getPosition(holder)?.let { pos ->
                            onItemChildLongClick(v2, pos)
                        } ?: false
                    }
                }
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////

    fun setOnItemClickListener(listener: IOnItemClickListener<DATA, RecyclerKListAdapter<*, *>>?) = apply {
        _onItemClickListener = listener
    }

    fun getOnItemClickListener(): IOnItemClickListener<DATA, RecyclerKListAdapter<*, *>>? =
        _onItemClickListener

    fun setOnItemLongClickListener(listener: IOnItemLongClickListener<DATA, RecyclerKListAdapter<*, *>>?) = apply {
        _onItemLongClickListener = listener
    }

    fun getOnItemLongClickListener(): IOnItemLongClickListener<DATA, RecyclerKListAdapter<*, *>>? =
        _onItemLongClickListener

    fun addOnItemChildClickListener(@IdRes id: Int, listener: IOnItemChildClickListener<DATA, RecyclerKListAdapter<*, *>>) = apply {
        _onItemChildClickListeners =
            (_onItemChildClickListeners ?: SparseArray<IOnItemChildClickListener<DATA, RecyclerKListAdapter<*, *>>>(2)).apply {
                put(id, listener)
            }
    }

    fun removeOnItemChildClickListener(@IdRes id: Int)= apply {
        _onItemChildClickListeners?.remove(id)
    }

    fun addOnItemChildLongClickListener(@IdRes id: Int, listener: IOnItemChildLongClickListener<DATA, RecyclerKListAdapter<*, *>>) = apply {
        _onItemChildLongClickListeners = (_onItemChildLongClickListeners ?: SparseArray<IOnItemChildLongClickListener<DATA, RecyclerKListAdapter<*, *>>>(2)).apply {
            put(id, listener)
        }
    }

    fun removeOnItemChildLongClickListener(@IdRes id: Int) = apply {
        _onItemChildLongClickListeners?.remove(id)
    }
}
