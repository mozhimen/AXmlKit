package com.mozhimen.xmlk.bark.title;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BarKTitle extends RelativeLayout {

    public interface IBarKTitleClickListener {
        void onBackClick(View v);
    }

    TextView mTextView;
    IBarKTitleClickListener mListener;

    public BarKTitle(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {

        LayoutInflater.from(context).inflate(R.layout.bark_title, this, true);

        mTextView = ((TextView) findViewById(R.id.tv_title));
        mTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onBackClick(v);
                }
            }
        });
    }

    public void setTitle(int titleResId) {
        mTextView.setText(titleResId);
    }

    public void setListener(IBarKTitleClickListener listener) {
        mListener = listener;
    }
}