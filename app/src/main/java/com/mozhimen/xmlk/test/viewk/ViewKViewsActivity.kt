package com.mozhimen.xmlk.test.viewk

import android.os.Bundle
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVDB
import com.mozhimen.basick.utilk.androidx.appcompat.runOnBackThread
import com.mozhimen.basick.utilk.kotlin.intResDrawable2bitmapAny
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
                    withContext(Dispatchers.Main){

                        vdb.viewkProgressWave2.apply {
                            setProgress(it)
                            setText("$it%")
                            if (it==10){
                                val bitmap = R.drawable.layoutk_tab_bottom_layout_fire.intResDrawable2bitmapAny(this@ViewKViewsActivity)
                                if(bitmap!=null){
                                    setIconBitmap(bitmap)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}