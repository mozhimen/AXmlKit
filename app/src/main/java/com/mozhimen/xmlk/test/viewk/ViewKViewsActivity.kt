package com.mozhimen.xmlk.test.viewk

import android.os.Bundle
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import com.mozhimen.basick.utilk.androidx.appcompat.runOnBackThread
import com.mozhimen.basick.utilk.androidx.lifecycle.runOnMainThread
import com.mozhimen.xmlk.test.databinding.ActivityViewkViewsBinding
import kotlinx.coroutines.delay

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
        vdb.viewkProgressWave.setOnClickListener {
            if (index++ > 100) {
                index = 0
            }
            UtilKLogWrapper.v(TAG, "initView: setProgress $index")
            vdb.viewkProgressWave.setProgress(index)
        }

        vdb.viewkProgressWave.post {
            runOnBackThread {
                repeat(100) {
                    delay(200)
                    vdb.viewkProgressWave.setProgress(it)
                }
            }
        }

        vdb.viewkProgressWaveImage.setOnClickListener {
            vdb.viewkProgressWaveImage.startLoad()
        }
    }
}