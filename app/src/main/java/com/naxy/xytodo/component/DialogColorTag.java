package com.naxy.xytodo.component;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.naxy.xytodo.R;

public class DialogColorTag extends Dialog implements View.OnClickListener {
    Context mContext;
    private int iAnimationResID;
    private int iTitleResID;
    private int iColorTag;
    private int iPositiveID;
    private int iPositiveColorID;//积极颜色
    private int iNegativeID;
    private OnClickListener mPositiveClickListener;
    private OnClickListener mNegativeClickListener;

    private TextView mTitle;
    private Button mPositiveBtn;
    private Button mNegativeBtn;

    private CheckBox[] cbx = new CheckBox[4];


    private DialogColorTag(Builder builder) {
        super(builder.mContext, builder.mThemeResID);
        mContext = builder.mContext;
        iAnimationResID = builder.mAnimationResID;
        iTitleResID = builder.mTitleResID;
        iColorTag = builder.mColorTag;
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
        this.setContentView(R.layout.dialog_color_tag);
        // 获取组件
        mTitle = (TextView) findViewById(R.id.tv_title);
        mPositiveBtn = (Button) findViewById(R.id.btn_positive);
        mNegativeBtn = (Button) findViewById(R.id.btn_negative);
        cbx[0] = (CheckBox) findViewById(R.id.cbx_0);
        cbx[1] = (CheckBox) findViewById(R.id.cbx_1);
        cbx[2] = (CheckBox) findViewById(R.id.cbx_2);
        cbx[3] = (CheckBox) findViewById(R.id.cbx_3);
        // 设置属性
        mTitle.setText(iTitleResID);
        setCheckBox(iColorTag);
        mPositiveBtn.setText(iPositiveID);
        mPositiveBtn.setTextColor(ContextCompat.getColor(mContext,iPositiveColorID));
        mNegativeBtn.setText(iNegativeID);
        mNegativeBtn.setTextColor(ContextCompat.getColor(mContext,R.color.text_normal_gray));
        // 设置监听
        mPositiveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPositiveClickListener.onClick(DialogColorTag.this, DialogInterface.BUTTON_POSITIVE);
            }
        });
        mNegativeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mNegativeClickListener.onClick(DialogColorTag.this, DialogInterface.BUTTON_POSITIVE);
            }
        });
        cbx[0].setOnClickListener(this);
        cbx[1].setOnClickListener(this);
        cbx[2].setOnClickListener(this);
        cbx[3].setOnClickListener(this);
        // 设置动画
        Window window = this.getWindow();
        window.setWindowAnimations(iAnimationResID);
    }

    public String getColorTag() {
        for (int i = 0; i <= 3; i++) {
            if (cbx[i].isChecked()) {
                iColorTag = i;
            }
        }
        return tag2Color(iColorTag);
    }

    private void setCheckBox(int num) {
        for (int i = 0; i <= 3; i++) {
            if (i == num) {
                cbx[i].setChecked(true);
            } else {
                cbx[i].setChecked(false);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cbx_0:
                setCheckBox(0);
                break;
            case R.id.cbx_1:
                setCheckBox(1);
                break;
            case R.id.cbx_2:
                setCheckBox(2);
                break;
            case R.id.cbx_3:
                setCheckBox(3);
                break;
        }
    }

    private static int color2Tag(String color) {
        int i = 0;
        switch (color) {
            case "blue":
                i = 0;
                break;
            case "green":
                i = 1;
                break;
            case "yellow":
                i = 2;
                break;
            case "red":
                i = 3;
                break;

        }
        return i;
    }

    private String tag2Color(int tag) {
        String str = "green";
        switch (tag) {
            case 0:
                str = "blue";
                break;
            case 1:
                str = "green";
                break;
            case 2:
                str = "yellow";
                break;
            case 3:
                str = "red";
                break;
        }
        return str;
    }

    public static class Builder {
        Context mContext;
        int mThemeResID;
        OnClickListener mPositiveClickListener;
        OnClickListener mNegativeClickListener;
        int mPositiveID;
        int mPositiveColorID;//积极颜色
        int mNegativeID;
        int mTitleResID;
        int mAnimationResID;
        int mColorTag;

        private Builder(Context context) {
            mContext = context;
        }

        public Builder setTheme(int theme) {
            mThemeResID = theme;
            return this;
        }

        public Builder setTitle(int id) {
            mTitleResID = id;
            return this;
        }

        public Builder setColorTag(String tag) {
            mColorTag = color2Tag(tag);
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

        public DialogColorTag build() {
            return new DialogColorTag(this);
        }
    }
}
