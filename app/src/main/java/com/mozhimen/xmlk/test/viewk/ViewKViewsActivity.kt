package com.mozhimen.xmlk.test.viewk

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import com.mozhimen.animk.builder.impls.AnimatorTranslationXYType
import com.mozhimen.basick.utils.runOnBackThread
import com.mozhimen.bindk.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.android.view.UtilKStatusBar
import com.mozhimen.kotlin.utilk.android.view.getLocation
import com.mozhimen.kotlin.utilk.android.widget.showToast
import com.mozhimen.kotlin.utilk.kotlin.intResDrawable2bitmapAny
import com.mozhimen.xmlk.test.databinding.ActivityViewkViewsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import com.mozhimen.xmlk.test.R

class ViewKViewsActivity : BaseActivityVDB<ActivityViewkViewsBinding>() {
    private var index = 0
    override fun initView(savedInstanceState: Bundle?) {
        /*val squareQRScan = findViewById<SquareQRScan>(R.id.viewk_squareQrScan)
       squareQRScan.setSquareQrScanCallback(object : SquareQRScan.SquareQrScanCallback {
           override fun onAnimEnd() {
               UtilKLogWrapper.i(TAG, "onAnimEnd : OK")
           }
       })
       GlobalScope.launch(Dispatchers.Main) {
           delay(4000)
           squareQRScan.requireSuccess()
       }*/
        vdb.viewkProgressWave.post {
            runOnBackThread {
                repeat(50) {
                    delay(200)
                    vdb.viewkProgressWave.setProgress(it)
                    vdb.viewkProgressWave1.setProgress(it)
                    withContext(Dispatchers.Main) {

                        vdb.viewkProgressWave2.apply {
                            setProgress(it)
                            setText("$it%")
                            if (it == 10) {
                                val bitmap = R.drawable.layoutk_tab_bottom_layout_fire.intResDrawable2bitmapAny(this@ViewKViewsActivity)
                                if (bitmap != null) {
                                    setIconBitmap(bitmap)
                                }
                            }
                        }
                    }
                }
            }
        }

        UtilKLogWrapper.d(TAG, "initView: ${UtilKStatusBar.getHeight()} ${UtilKStatusBar.getHeight(this)} ${UtilKStatusBar.getHeight(true)}")
        vdb.viewkImg.setOnClickListener {
            var xy = vdb.viewkImg.getLocation()
            UtilKLogWrapper.d(TAG, "initView: x ${vdb.viewkImg.x} y ${vdb.viewkImg.y} xy ${xy.first} ${xy.second}")
//            vdb.viewkImg.animate().x(0f).y(0f).setListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationEnd(animation: Animator) {
//                    "动画结束啦".showToast()
//                }
//            })

            AnimatorTranslationXYType().setViewRef(vdb.viewkImg).fromX(vdb.viewkImg.x).fromY(vdb.viewkImg.y).toX(0f).toY(0f).addAnimatorListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    xy = vdb.viewkImg.getLocation()
                    UtilKLogWrapper.d(TAG, "initView: x ${vdb.viewkImg.x} y ${vdb.viewkImg.y} xy ${xy.first} ${xy.second}")
                    "动画结束啦".showToast()
                }
            }).build().start()
        }
    }
}