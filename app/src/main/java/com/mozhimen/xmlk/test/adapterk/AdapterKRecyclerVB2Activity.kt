package com.mozhimen.xmlk.test.adapterk

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mozhimen.kotlin.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.kotlin.elemk.mos.MKey
import com.mozhimen.kotlin.utilk.android.widget.showToast
import com.mozhimen.xmlk.recyclerk.quick.RecyclerKQuickAdapterVDB
import com.mozhimen.xmlk.vhk.VHKRecyclerVDB
import com.mozhimen.xmlk.test.R
import com.mozhimen.xmlk.test.BR
import com.mozhimen.xmlk.test.databinding.ActivityAdapterkRecyclerVb2Binding
import com.mozhimen.xmlk.test.databinding.ItemAdapterkRecyclerVb2Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * @ClassName AdapterKRecyclerVB2Activity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
class AdapterKRecyclerVB2Activity : BaseActivityVDB<ActivityAdapterkRecyclerVb2Binding>() {
    private val _datas = mutableListOf<MKey>()
    private lateinit var _adapterRecyclerVb2: RecyclerKQuickAdapterVDB<MKey, ItemAdapterkRecyclerVb2Binding>

    override fun initView(savedInstanceState: Bundle?) {
        _adapterRecyclerVb2 = RecyclerKQuickAdapterVDB(
            _datas,
            R.layout.item_adapterk_recycler_vb2,
            BR.item_adapterk_recycler_vb2
        ) { holder: VHKRecyclerVDB<ItemAdapterkRecyclerVb2Binding>, data: MKey, position: Int, _: Int ->
            holder.vdb.itemAdapterkRecyclerVb2Name.setOnClickListener {
                "${position}: data:${data}".showToast()
            }
        }
        vdb.adapterkRecyclerVb2Recycler.apply {
            layoutManager = LinearLayoutManager(this@AdapterKRecyclerVB2Activity)
            adapter = _adapterRecyclerVb2
        }

        lifecycleScope.launch(Dispatchers.IO) {
            _datas.addAll(
                arrayOf(
                    MKey("0", "xxx"),
                    MKey("1", "xxx"),
                    MKey("2", "jjj"),
                    MKey("3", "xxx"),
                    MKey("4", "xxx"),
                    MKey("5", "xxx"),
                    MKey("6", "xxx"),
                    MKey("7", "xxx"),
                    MKey("8", "xxx"),
                )
            )
            withContext(Dispatchers.Main) {
                _adapterRecyclerVb2.addDatas(_datas,true)
            }
        }
    }
}