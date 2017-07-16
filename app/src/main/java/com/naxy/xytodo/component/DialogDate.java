package com.naxy.xytodo.component;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.naxy.xytodo.R;

public class DialogDate extends Dialog {
    private Context mContext;
    private int iAnimationResID;
    private int iPositiveID;
    private int iPositiveColorID;//积极颜色
    private int iNegativeID;
    private OnClickListener mPositiveClickListener;
    private OnClickListener mNegativeClickListener;

    private Button mPositiveBtn;
    private Button mNegativeBtn;
    private DatePicker mContent;

    public DatePicker PickerGet(){
        return mContent;
    }

    private DialogDate(Builder builder) {
        super(builder.mContext, builder.mThemeResID);
        mContext = builder.mContext;
        iAnimationResID = builder.mAnimationResID;
        iPositiveID = builder.mPositiveID;
        iPositiveColorID = builder.mPositiveColorID;
        iNegativeID = builder.mNegativeID;
        mPositiveClickListener = builder.mPositiveClickListener;
        mNegativeClickListener = builder.mNegativeClickListener;
    }

    public static Builder from(Context context) {
        return new Builder(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框使用的布局文件
        this.setContentView(R.layout.dialog_date);
        // 获取组件
        mPositiveBtn = (Button) findViewById(R.id.btn_positive);
        mNegativeBtn = (Button) findViewById(R.id.btn_negative);
        mContent = (DatePicker) findViewById(R.id.datePicker);
        // 设置属性
        mPositiveBtn.setText(iPositiveID);
        mPositiveBtn.setTextColor(ContextCompat.getColor(mContext,iPositiveColorID));
        mNegativeBtn.setText(iNegativeID);
        mNegativeBtn.setTextColor(ContextCompat.getColor(mContext,R.color.text_normal_gray));
        // 设置监听
        mPositiveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPositiveClickListener.onClick(DialogDate.this, DialogInterface.BUTTON_POSITIVE);
            }
        });
        mNegativeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mNegativeClickListener.onClick(DialogDate.this, DialogInterface.BUTTON_POSITIVE);
            }
        });
        // 设置动画
        Window window = this.getWindow();
        window.setWindowAnimations(iAnimationResID);
    }

    public static class Builder {
        Context mContext;
        int mThemeResID;
        OnClickListener mPositiveClickListener;
        OnClickListener mNegativeClickListener;
        int mPositiveID;
        int mPositiveColorID;//积极颜色
        int mNegativeID;
        int mAnimationResID;

        private Builder(Context context) {
            mContext = context;
        }

        public Builder setTheme(int theme) {
            mThemeResID = theme;
            return this;
        }

        public Builder setAnimations(int id) {
            mAnimationResID = id;
            return this;
        }

        public Builder setPositiveID(int id) {
            mPositiveID = id;
            return this;
        }

        public Builder setPositiveColorID(int id) {
            mPositiveColorID = id;
            return this;
        }

        public Builder setNegativieID(int id) {
            mNegativeID = id;
            return this;
        }

        public Builder setPositiveListener(OnClickListener listener) {
            mPositiveClickListener = listener;
            return this;
        }

        public Builder setNegativeListener(OnClickListener onClickListener) {
            mNegativeClickListener = onClickListener;
            return this;
        }

        public DialogDate build() {
            return new DialogDate(this);
        }
    }
}
