package com.mozhimen.uicorek.test.adapterk

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.elemk.mos.MKey
import com.mozhimen.basick.utilk.android.widget.showToast
import com.mozhimen.uicorek.recyclerk.item.AdapterKItemRecyclerVB
import com.mozhimen.uicorek.test.R
import com.mozhimen.uicorek.test.BR
import com.mozhimen.uicorek.test.databinding.ActivityAdapterkRecyclerVbBinding
import com.mozhimen.uicorek.test.databinding.ItemAdapterkRecyclerVbBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @ClassName AdapterKRecyclerVBActivity
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2023/8/30 23:29
 * @Version 1.0
 */
class AdapterKRecyclerVBActivity : BaseActivityVDB<ActivityAdapterkRecyclerVbBinding>() {

    private lateinit var _adapterRecyclerVb: AdapterKItemRecyclerVB<MKey, ItemAdapterkRecyclerVbBinding>

    override fun initView(savedInstanceState: Bundle?) {
        _adapterRecyclerVb = AdapterKItemRecyclerVB<MKey, ItemAdapterkRecyclerVbBinding>(
            mutableListOf(),
            R.layout.item_adapterk_recycler_vb,
            BR.item_adapterk_recycler_vb
        ) { holder, itemData, position, currentSelectPos ->
            holder.vdb.itemAdapterkRecyclerVb2Name.setOnClickListener {
                "${position}: data:${itemData}".showToast()
            }
        }
        vdb.adapterkRecyclerVbRv.apply {
            layoutManager = LinearLayoutManager(this@AdapterKRecyclerVBActivity)
            adapter = _adapterRecyclerVb
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val datas =
                listOf(
                    MKey("0", "xxx"),
                    MKey("1", "xxx"),
                    MKey("2", "jjj"),
                    MKey("3", "xxx"),
                    MKey("4", "xxx"),
                    MKey("5", "xxx"),
                    MKey("6", "xxx"),
                    MKey("7", "xxx"),
                    MKey("8", "xxx")
                )
            withContext(Dispatchers.Main) {
                _adapterRecyclerVb.addDatas(datas, true)
            }
        }
    }
}