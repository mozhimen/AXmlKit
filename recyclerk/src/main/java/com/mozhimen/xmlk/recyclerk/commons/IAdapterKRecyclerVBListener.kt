package com.mozhimen.xmlk.recyclerk.commons

import com.mozhimen.xmlk.vhk.VHKRecyclerVDB

/**
 * @ClassName IAdapterKRecyclerVBListener
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2023/8/31 13:29
 * @Version 1.0
 */
typealias IAdapterKRecyclerVBListener<DATA, VB> = (holder: VHKRecyclerVDB<VB>, data: DATA, position: Int, currentSelectPosition: Int) -> Unit
typealias IAdapterKRecyclerStuffedVBListener<DATA, VB> = (holder: VHKRecyclerVDB<VB>, data: DATA, position: Int, currentSelectPosition: Int) -> Unit
