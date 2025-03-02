package com.mozhimen.xmlk.dialogk.bases.commons

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresPermission
import androidx.annotation.StyleRes
import com.mozhimen.kotlin.lintk.optins.permission.OPermission_SYSTEM_ALERT_WINDOW
import com.mozhimen.kotlin.elemk.android.cons.CPermission
import com.mozhimen.kotlin.utilk.commons.IUtilK

/**
 * @ClassName IBaseDialogK
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/6/2 15:38
 * @Version 1.0
 */
interface IBaseDialogK : IUtilK {
    fun getDialogClickListener(): IDialogKClickListener?

//    /**
//     * 弹框模式
//     * @return Int
//     */
//    @ADialogMode
//    fun getDialogMode(): Int

    /**
     * 初始化window宽度
     * 默认屏幕宽度左右间距25dp
     * (getCurrentScreenWidth() * 0.8f).roundToInt()
     * @return
     */
    fun getDialogWindowWidth(): Int

    /**
     * 初始化window高度
     * 默认wrap_content
     * @return
     */
    fun getDialogWindowHeight(): Int

    /**
     * 初始化window的gravity
     * @return 默认返回 Gravity.CENTER [Gravity]
     */
    fun getDialogWindowGravity(): Int

    /**
     * 窗口动画
     */
    @StyleRes
    fun getDialogWindowAnimations(): Int?

    //////////////////////////////////////////////////////////////////////////////

    fun setDialogClickListener(listener: IDialogKClickListener): IBaseDialogK

//    /**
//     * 设置dialog的模式, 设置后会回调到[.onInitMode]
//     * @param mode
//     */
//    fun setDialogMode(@ADialogMode mode: Int): IBaseDialogK<*>

//    /**
//     * 设置dialog的模式
//     * 设置后会回调到[.onInitMode]
//     * @param mode
//     * @param callModeChange false 禁止回调[.onInitMode]
//     */
//    fun setDialogMode(@ADialogMode mode: Int, callModeChange: Boolean): IBaseDialogK<*>

    /**
     * 设置可取消
     * @param flag Boolean
     * @return BaseDialogK<*>
     */
    fun setDialogCancelable(flag: Boolean): IBaseDialogK

    /**
     * 延迟显示
     * @param delayMillis Long
     */
    fun showByDelay(delayMillis: Long)

    /**
     * 不依附Activity来show，比如在service里面
     * 此举将会把dialog的window level提升为system
     * 需要权限
     * <h3>uses-permission Android:name="android.permission.SYSTEM_ALERT_WINDOW"
    </h3> */
    @RequiresPermission(CPermission.SYSTEM_ALERT_WINDOW)
    @OPermission_SYSTEM_ALERT_WINDOW
    fun showInSystemWindow()

    //////////////////////////////////////////////////////////////////////////////
    //os detect
    //////////////////////////////////////////////////////////////////////////////

    /**
     * 创建View
     * @param inflater LayoutInflater
     * @return View?
     */
    fun onCreateView(inflater: LayoutInflater): View?

    /**
     * view创建
     * @param view View
     */
    fun onViewCreated(view: View)

    /**
     * 销毁View
     */
    fun onDestroyView()

//    /**
//     * 初始化Mode
//     * @param mode Int
//     */
//    fun onInitMode(@ADialogMode mode: Int) {}
}