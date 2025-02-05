package com.mozhimen.xmlk.test.textk

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.mozhimen.uik.databinding.bases.viewdatabinding.activity.BaseActivityVDB
import com.mozhimen.kotlin.utilk.android.content.startContext
import com.mozhimen.kotlin.utilk.kotlin.strHtml2chars
import com.mozhimen.kotlin.utilk.kotlin.text.replace_lineBreak2strHtmlBr
import com.mozhimen.xmlk.popwink.bubble.PopwinKBubbleBuilder
import com.mozhimen.xmlk.textk.progress.TextKProgress
import com.mozhimen.xmlk.test.databinding.ActivityTextkBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TextKActivity : BaseActivityVDB<ActivityTextkBinding>() {
    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        vdb.textkBubbleBtn.setOnClickListener {
            genPopwinKBubbleText(it, "弹出了一个气泡提示")
        }
        lifecycleScope.launch {
            vdb.textkProgress.setProgress(0f)
            vdb.textkProgress.setCurrentText("0%")
            vdb.textkProgress.setProgressState(TextKProgress.PROGRESS_STATE_LOADING)
            delay(1000)
            vdb.textkProgress.setProgress(50f)
            vdb.textkProgress.setCurrentText("10%")
            delay(1000)
            vdb.textkProgress.setProgress(100f)
            vdb.textkProgress.setCurrentText("100%")
            vdb.textkProgress.setProgressState(TextKProgress.PROGRESS_STATE_FINISH)
        }

        lifecycleScope.launch {
            vdb.textkProgress1.setProgressState(TextKProgress.PROGRESS_STATE_LOADING)
            vdb.textkProgress1.setProgressText("0%", 0)
            delay(1000)
            vdb.textkProgress1.setProgressText("20%", 20)
            delay(1000)
            vdb.textkProgress1.setProgressText("40%", 40)
            delay(1000)
            vdb.textkProgress1.setProgressText("60%", 60)
            delay(1000)
            vdb.textkProgress1.setProgressText("80%", 80)
            delay(1000)
            vdb.textkProgress1.setProgressText("100%", 100)
            vdb.textkProgress1.setProgressState(TextKProgress.PROGRESS_STATE_FINISH)
        }
        lifecycleScope.launch {
            vdb.textkProgress2.setProgressState(TextKProgress.PROGRESS_STATE_LOADING)
            vdb.textkProgress2.setContentAndProgress("0%", 0, false)
            delay(2000)
            vdb.textkProgress2.setContentAndProgress("20%", 20, false)
            delay(2000)
            vdb.textkProgress2.setContentAndProgress("40%", 40, false)
            delay(2000)
            vdb.textkProgress2.setContentAndProgress("50%", 50, false)
            delay(2000)
            vdb.textkProgress2.setContentAndProgress("60%", 60, false)
            delay(2000)
            vdb.textkProgress2.setContentAndProgress("80%", 80, false)
            delay(2000)
            vdb.textkProgress2.setContentAndProgress("100%", 100, false)
            vdb.textkProgress2.setProgressState(TextKProgress.PROGRESS_STATE_FINISH)
        }
        vdb.textkMarquee.setText("著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处")
        vdb.textkExpandable.setExpandableText(
            "尽情地飞奔!\n躲开迎面而来的火车!\n\n帮助Jake、Tricky和Fresh逃出坏警察和他的大狗的追赶。\n\n★ 与你的伙伴们在火车间穿行!\n★ 鲜艳生动的高清画质！\n★ 冲浪滑板!\n★ 颜色动力飞行器!\n★ 闪电般快速的飞行技巧!\n★ 接受挑战并帮助恁的伙伴!\n\n快来加入最为刺激的追逐！\n\n一款带有高清优化图像的通用应用程序\n\n由Sybo和Kiloo共同开发\",\"modDescription\":\"无限金钱、钥匙、滑板和助推器； 用真实货币免费购买（出现错误后，关闭支付窗口）； 你可以无限次跳跃。\",\"lastUpdateTime\":\"2024-04-19 00:00:00\",\"lastUpdateContent\":\"\n - 这个地球日，Subway Surfers回到水下！在海底奔跑，在海底与水母、珊瑚和其他海洋风格的美丽元素赛跑。\n - 2个新角色：完成熔岩地面挑战以解锁Electra，或者接受赛季挑战并发现古怪的Finn。\n - 加入特殊的地球跑以解锁Malik。\n - Cathy和Koral获得了新的套装，参加季度狩猎和特殊关卡互助计时赛即可解锁。\n \n".replace_lineBreak2strHtmlBr()
                .strHtml2chars()
            /*"@Override\n" +
                    "protected void onCreate(Bundle savedInstanceState) {\n" +
                    "super.onCreate(savedInstanceState);\n" +
                    "setContentView(R.layout.activity_main);\n" +
                    "collapsibleTextView = (CollapsibleTextView)\n" +
                    "findViewById(R.id.CollapsibleTextView);\n" +
                    "//设置文字会出现效果\n" +
                    "collapsibleTextView.setText(getResources().getString(R.string.content));\n" +
                    "}\n" +
                    "\n" +
                    "作者：miaozbetter\n" +
                    "链接：https://www.jianshu.com/p/61aec9d934e5\n" +
                    "来源：简书\n" +
                    "著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。"*/, 4
        )
    }

    private val _popwinKTextKBubbleBuilder: PopwinKBubbleBuilder? = null
    private fun genPopwinKBubbleText(view: View, tip: String, delayMillis: Long = 4000) {
        _popwinKTextKBubbleBuilder?.dismiss()
        val builder = PopwinKBubbleBuilder.Builder(this)
        builder.apply {
            setTip(tip)
            setDismissDelay(delayMillis)
            create(view)
        }
    }

    fun goTextKEdit(view: View) {
        startContext<TextKEditActivity>()
    }
}