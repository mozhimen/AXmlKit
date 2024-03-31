package com.mozhimen.xmlk.recyclerk.item

import androidx.databinding.ViewDataBinding
import com.mozhimen.basick.elemk.kotlin.cons.CSuppress
import com.mozhimen.basick.utilk.kotlin.collections.joinT2list
import com.mozhimen.basick.utilk.kotlin.collections.joinT2listIgnoreNull
import com.mozhimen.xmlk.recyclerk.commons.IAdapterKRecyclerVB
import com.mozhimen.xmlk.recyclerk.commons.IAdapterKRecyclerVBListener

/**
 * @ClassName AdapterKRecyclerStuffedVB2
 * @Description 带Header的RecyclerView适配器
 * @Author Kolin Zhao
 * @Date 2021/7/7 14:48
 * @Version 1.0
 */

//typealias IAdapterKRecyclerStuffedVB2Listener<DATA, VB> = (holder: VHKRecyclerVB<VB>, itemData: DATA, position: Int, currentSelectPos: Int) -> Unit

@Suppress(CSuppress.UNCHECKED_CAST)
class AdapterKItemRecyclerStuffedVDB<DATA, VDB : ViewDataBinding>(
    private val _datas: List<DATA>,
    private var _defaultLayoutId: Int,
    private var _brId: Int,
    private var _headerLayoutId: Int? = null,
    private var _footerLayoutId: Int? = null,
    private var _listener: IAdapterKRecyclerVBListener<DATA, VDB>? = null
) : AdapterKItemRecyclerStuffed(), IAdapterKRecyclerVB<DATA, VDB> {
    private var _selectItemPosition = -1

    init {
        addDatas(_datas, true)
    }

    ///////////////////////////////////////////////////////////////////////////////////

    override fun refreshData(data: DATA, position: Int, notify: Boolean) {
        refreshItem(RecyclerKItemVDB(data, _brId, _defaultLayoutId, _selectItemPosition, _listener), position, notify)
    }

    override fun refreshDatas(notify: Boolean) {
        refreshItems(notify)
    }

    override fun refreshDatas(datas: List<DATA>) {
        refreshDatas(datas)
    }

    override fun refreshDatas(datas: List<DATA>, notify: Boolean) {
        refreshItems(datas.joinT2list { RecyclerKItemVDB(it, _brId, _defaultLayoutId, _selectItemPosition, _listener) }, notify)
    }

    override fun addData(data: DATA, notify: Boolean) {
        addItem(RecyclerKItemVDB(data, _brId, _defaultLayoutId, _selectItemPosition, _listener), notify)
    }

    override fun addDataAtPosition(data: DATA, position: Int, notify: Boolean) {
        addItemAtPosition(RecyclerKItemVDB(data, _brId, _defaultLayoutId, _selectItemPosition, _listener), position, notify)
    }

    override fun addDatas(datas: List<DATA>, notify: Boolean) {
        addItems(datas.joinT2list { RecyclerKItemVDB(it, _brId, _defaultLayoutId, _selectItemPosition, _listener) }, notify)
    }

    override fun removeData(data: DATA, notify: Boolean) {
        _items.find { (it as? RecyclerKItemVDB<DATA, VDB>?)?.data == data }?.let { removeItem(it, notify) }
    }

    override fun removeDataAtPosition(position: Int, notify: Boolean): DATA? {
        return (removeDataAtPosition(position, notify) as? RecyclerKItemVDB<DATA, VDB>?)?.data
    }

    override fun removeDatasAll(notify: Boolean) {
        removeDatasAll(notify)
    }

    override fun getData(position: Int): DATA? {
        return (getData(position) as? RecyclerKItemVDB<DATA, VDB>?)?.data
    }

    override fun getDatas(): List<DATA?> {
        return _items.joinT2listIgnoreNull { (it as? RecyclerKItemVDB<DATA, VDB>?)?.data }
    }

    override fun onDataSelected(position: Int) {
        if (position < 0 || position >= _items.size) return
        _selectItemPosition = position
//        val item = getData(_selectItemPosition) as RecyclerKItemVB<DATA, VB>
//        listener.invoke(item.vh, item.data, _selectItemPosition, _selectItemPosition)
        refreshItems(true)
    }

    override fun getCurrentSelectPosition(): Int {
        return _selectItemPosition
    }

//    ///////////////////////////////////////////////////////////////////////////////////
//
//    override fun addHeaderView(view: View) {
//        addHeaderView(view)
//    }
//
//    override fun removeHeaderView(view: View) {
//        removeHeaderView(view)
//    }
//
//    override fun addFooterView(view: View) {
//        addFooterView(view)
//    }
//
//    override fun removeFooterView(view: View) {
//        removeFooterView(view)
//    }
//
//    override fun getHeaderViewSize(): Int {
//        return getHeaderViewSize()
//    }
//
//    override fun getFooterViewSize(): Int {
//        return getFooterViewSize()
//    }
//
//    override fun getNormalItemSize(): Int {
//        return getNormalItemSize()
//    }
}