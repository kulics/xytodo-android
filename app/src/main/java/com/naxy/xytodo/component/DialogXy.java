package com.naxy.xytodo.component;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.naxy.xytodo.R;

public class DialogXy extends Dialog
{
    private Context mContext;
    private int iAnimationResID;
    private int iTitleResID;
    private int iContentResID;
    private String strContentRes = "";
    private int iPositiveID;
    private int iPositiveColorID;//积极颜色
    private int iNegativeID;
    private OnClickListener mPositiveClickListener;
    private OnClickListener mNegativeClickListener;

    private TextView mTitle;
    private Button mPositiveBtn;
    private Button mNegativeBtn;
    private TextView mContent;

    private DialogXy(Builder builder)
    {
        super(builder.mContext, builder.mThemeResID);
        mContext = builder.mContext;
        iAnimationResID = builder.mAnimationResID;
        iTitleResID = builder.mTitleResID;
        iContentResID = builder.mContentResID;
        strContentRes = builder.mContentRes;
        iPositiveID = builder.mPositiveID;
        iPositiveColorID = builder.mPositiveColorID;
        iNegativeID = builder.mNegativeID;
        mPositiveClickListener = builder.mPositiveClickListener;
        mNegativeClickListener = builder.mNegativeClickListener;
    }

    public static Builder from(Context context)
    {
        return new Builder(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // 设置对话框使用的布局文件
        this.setContentView(R.layout.dialog_xy);
        // 获取组件
        mTitle = (TextView) findViewById(R.id.tv_title);
        mPositiveBtn = (Button) findViewById(R.id.btn_positive);
        mNegativeBtn = (Button) findViewById(R.id.btn_negative);
        mContent = (TextView) findViewById(R.id.dialog_content);
        // 设置属性
        mTitle.setText(iTitleResID);
        if (iContentResID == 0)
        {
            mContent.setText(strContentRes);
        }
        else
        {
            mContent.setText(iContentResID);
        }
        mPositiveBtn.setText(iPositiveID);
        mPositiveBtn.setTextColor(ContextCompat.getColor(mContext, iPositiveColorID));
        mNegativeBtn.setText(iNegativeID);
        mNegativeBtn.setTextColor(ContextCompat.getColor(mContext, R.color.text_normal_gray));
        // 设置监听
        mPositiveBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mPositiveClickListener.onClick(DialogXy.this, DialogInterface.BUTTON_POSITIVE);
            }
        });
        mNegativeBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mNegativeClickListener.onClick(DialogXy.this, DialogInterface.BUTTON_POSITIVE);
            }
        });
        // 设置动画
        Window window = this.getWindow();
        window.setWindowAnimations(iAnimationResID);
    }

    public static class Builder
    {
        Context mContext;
        int mThemeResID;
        OnClickListener mPositiveClickListener;
        OnClickListener mNegativeClickListener;
        int mPositiveID;
        int mPositiveColorID;//积极颜色
        int mNegativeID;
        int mContentResID;
        String mContentRes;
        int mTitleResID;
        int mAnimationResID;

        private Builder(Context context)
        {
            mContext = context;
        }

        public Builder setTheme(int theme)
        {
            mThemeResID = theme;
            return this;
        }

        public Builder setTitle(int id)
        {
            mTitleResID = id;
            return this;
        }

        public Builder setAnimations(int id)
        {
            mAnimationResID = id;
            return this;
        }

        public Builder setContent(int id)
        {
            mContentResID = id;
            return this;
        }

        public Builder setContent(String str)
        {
            mContentRes = str;
            return this;
        }

        public Builder setPositiveID(int id)
        {
            mPositiveID = id;
            return this;
        }

        public Builder setPositiveColorID(int id)
        {
            mPositiveColorID = id;
            return this;
        }

        public Builder setNegativeID(int id)
        {
            mNegativeID = id;
            return this;
        }

        public Builder setPositiveListener(OnClickListener listener)
        {
            mPositiveClickListener = listener;
            return this;
        }

        public Builder setNegativeListener(OnClickListener onClickListener)
        {
            mNegativeClickListener = onClickListener;
            return this;
        }

        public DialogXy build()
        {
            return new DialogXy(this);
        }
    }
}
