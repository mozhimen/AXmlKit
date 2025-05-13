package com.mozhimen.xmlk.textk.html

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.text.Spannable
import android.text.Spanned
import android.text.style.QuoteSpan
import android.util.AttributeSet
import androidx.annotation.RawRes
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.commons.IUtilK
import com.mozhimen.kotlin.utilk.java.io.inputStream2str_use_scanner
import java.io.InputStream
import java.util.Scanner

/**
 * @ClassName TextKHtml
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/5/13
 * @Version 1.0
 */
class TextKHtml @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr), IUtilK {
    var blockQuoteBackgroundColor: Int = Color.WHITE
    var blockQuoteStripColor: Int = Color.BLACK
    var blockQuoteStripWidth: Float = 10f
    var blockQuoteGap: Float = 20f
    private var clickableTableSpan: ClickableTableSpan? = null
    private var drawTableLinkSpan: DrawTableLinkSpan? = null
    private var onClickATagListener: OnClickATagListener? = null
    private var indent = 24.0f // Default to 24px.
    private var removeTrailingWhiteSpace = true

    ///////////////////////////////////////////////////////////////////////////

    /**
     * @see HtmlTextView.setHtml
     */
    fun setHtml(@RawRes resId: Int) {
        setHtml(resId, null)
    }

    /**
     * @see HtmlTextView.setHtml
     */
    fun setHtml(html: String) {
        setHtml(html, null)
    }

    /**
     * Loads HTML from a raw resource, i.e., a HTML file in res/raw/.
     * This allows translatable resource (e.g., res/raw-de/ for german).
     * The containing HTML is parsed to Android's Spannable format and then displayed.
     *
     * @param resId       for example: R.raw.help
     * @param imageGetter for fetching images. Possible ImageGetter provided by this library:
     * HtmlLocalImageGetter and HtmlRemoteImageGetter
     */
    fun setHtml(@RawRes resId: Int, imageGetter: Html.ImageGetter?) {
        val inputStreamText: InputStream = getContext().getResources().openRawResource(resId)

        setHtml(inputStreamText.inputStream2str_use_scanner(), imageGetter)
    }

    /**
     * Parses String containing HTML to Android's Spannable format and displays it in this TextView.
     * Using the implementation of Html.ImageGetter provided.
     *
     * @param html        String containing HTML, for example: "**Hello world!**"
     * @param imageGetter for fetching images. Possible ImageGetter provided by this library:
     * HtmlLocalImageGetter and HtmlRemoteImageGetter
     */
    fun setHtml(html: String, imageGetter: Html.ImageGetter?) {
        val styledText = HtmlFormatter.formatHtml(
            html, imageGetter, clickableTableSpan, drawTableLinkSpan,
            { onClickATagListener!! }, indent, removeTrailingWhiteSpace
        )
        UtilKLogWrapper.d(TAG, "setHtml: \$styledText $styledText")
        replaceQuoteSpans(styledText)
        setText(styledText)

        // make links work
        setMovementMethod(LocalLinkMovementMethod.getInstance())
    }

    /**
     * The Html.fromHtml method has the behavior of adding extra whitespace at the bottom
     * of the parsed HTML displayed in for example a TextView. In order to remove this
     * whitespace call this method before setting the text with setHtml on this TextView.
     *
     * @param removeTrailingWhiteSpace true if the whitespace rendered at the bottom of a TextView
     * after setting HTML should be removed.
     */
    fun setRemoveTrailingWhiteSpace(removeTrailingWhiteSpace: Boolean) {
        this.removeTrailingWhiteSpace = removeTrailingWhiteSpace
    }

    fun setClickableTableSpan(clickableTableSpan: ClickableTableSpan?) {
        this.clickableTableSpan = clickableTableSpan
    }

    fun setDrawTableLinkSpan(drawTableLinkSpan: DrawTableLinkSpan?) {
        this.drawTableLinkSpan = drawTableLinkSpan
    }

    fun setOnClickATagListener(onClickATagListener: OnClickATagListener?) {
        this.onClickATagListener = onClickATagListener
    }

    /**
     * Add ability to increase list item spacing. Useful for configuring spacing based on device
     * screen size. This applies to ordered and unordered lists.
     *
     * @param px pixels to indent.
     */
    fun setListIndentPx(px: Float) {
        this.indent = px
    }

    ///////////////////////////////////////////////////////////////////////////

    private fun replaceQuoteSpans(spanned: Spanned) {
        val spannable = spanned as Spannable
        val quoteSpans = spannable.getSpans(0, spannable.length - 1, QuoteSpan::class.java)
        for (quoteSpan in quoteSpans) {
            val start = spannable.getSpanStart(quoteSpan)
            val end = spannable.getSpanEnd(quoteSpan)
            val flags = spannable.getSpanFlags(quoteSpan)
            spannable.removeSpan(quoteSpan)
            spannable.setSpan(
                DesignQuoteSpan(blockQuoteBackgroundColor, blockQuoteStripColor, blockQuoteStripWidth, blockQuoteGap),
                start,
                end,
                flags
            )
        }
    }
}