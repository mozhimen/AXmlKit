package com.mozhimen.xmlk.recyclerk.commons

import com.mozhimen.xmlk.vhk.VHKRecyclerVDB

/**
 * @ClassName IAdapterKRecyclerVBListener
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Version 1.0
 */
typealias IRecyclerKAdapterVDBListener<DATA, VDB> = (holder: VHKRecyclerVDB<VDB>, data: DATA, position: Int, currentSelectPosition: Int) -> Unit
typealias IRecyclerKAdapterStuffedVDBListener<DATA, VDB> = (holder: VHKRecyclerVDB<VDB>, data: DATA, position: Int, currentSelectPosition: Int) -> Unit
