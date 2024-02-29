package com.mozhimen.uicorek.recyclerk.commons

import com.mozhimen.uicorek.vhk.VHKRecyclerVB

/**
 * @ClassName IAdapterKRecyclerVBListener
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2023/8/31 13:29
 * @Version 1.0
 */
typealias IAdapterKRecyclerVBListener<DATA, VB> = (holder: VHKRecyclerVB<VB>, data: DATA, position: Int, currentSelectPosition: Int) -> Unit
typealias IAdapterKRecyclerStuffedVBListener<DATA, VB> = (holder: VHKRecyclerVB<VB>, data: DATA, position: Int, currentSelectPosition: Int) -> Unit
