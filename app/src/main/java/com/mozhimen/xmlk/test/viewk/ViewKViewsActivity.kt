package com.mozhimen.xmlk.test.viewk

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import com.mozhimen.basick.utilk.androidx.lifecycle.runOnMainThread
import com.mozhimen.xmlk.test.databinding.ActivityViewkViewsBinding
import kotlinx.coroutines.delay

class ViewKViewsActivity : BaseActivityVDB<ActivityViewkViewsBinding>() {

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
            runOnMainThread {
                repeat(100) { i ->
                    UtilKLogWrapper.v(TAG, "initView: repeat i $i")
                    vdb.viewkProgressWave.setProgress(i)
                    delay(1000)
                }
            }
        }
    }
}