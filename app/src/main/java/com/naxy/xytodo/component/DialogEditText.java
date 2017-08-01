package com.naxy.xytodo.component;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.naxy.xytodo.R;

public class DialogEditText extends Dialog
{
    private Context mContext;
    private int iAnimationResID;
    private int iTitleResID;
    private int iEditHintResID;
    private String strEditHintRes = "";
    private int iEditErrResID;
    private int iPositiveID;
    private int iPositiveColorID;//积极颜色
    private int iNegativeID;
    private OnClickListener mPositiveClickListener;
    private OnClickListener mNegativeClickListener;

    private TextView mTitle;
    private EditText edtText;
    private Button mPositiveBtn;
    private Button mNegativeBtn;

    //显示键盘
    public void showKeyboard()
    {
        if (edtText != null)
        {
            //设置可获得焦点
            edtText.setFocusable(true);
            edtText.setFocusableInTouchMode(true);
            //请求获得焦点
            edtText.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) edtText
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(edtText, 0);
        }
    }

    private DialogEditText(Builder builder)
    {
        super(builder.mContext, builder.mThemeResID);
        mContext = builder.mContext;
        iAnimationResID = builder.mAnimationResID;
        iTitleResID = builder.mTitleResID;
        iEditHintResID = builder.mEditHintResID;
        strEditHintRes = builder.mEditHintRes;
        iEditErrResID = builder.mEditErrResID;
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
        this.setContentView(R.layout.dialog_edit_text);
        // 获取组件
        mTitle = (TextView) findViewById(R.id.tv_title);
        mPositiveBtn = (Button) findViewById(R.id.btn_positive);
        mNegativeBtn = (Button) findViewById(R.id.btn_negative);
        edtText = (EditText) findViewById(R.id.edit_masterPassword);
        // 设置属性
        mTitle.setText(iTitleResID);
        if (iEditHintResID == 0)
        {
            edtText.setHint(strEditHintRes);
        }
        else
        {
            edtText.setHint(iEditHintResID);
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
                mPositiveClickListener.onClick(DialogEditText.this, DialogInterface.BUTTON_POSITIVE);
            }
        });
        mNegativeBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mNegativeClickListener.onClick(DialogEditText.this, DialogInterface.BUTTON_POSITIVE);
            }
        });
        // 设置动画
        Window window = this.getWindow();
        window.setWindowAnimations(iAnimationResID);
    }

    public String getText()
    {
        String strtemp =
                edtText.getText()
                        .toString()
                        .trim();
        return strtemp;
    }

    public boolean checkCode()
    {
        boolean bResult = true;
        String strtemp =
                edtText.getText()
                        .toString()
                        .trim();
        if (strtemp.length() == 0) // 判断是否为空
        {
            edtText
                    .setError(mContext.getString(iEditErrResID));
            bResult = false;
        }
        return bResult;
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
        int mTitleResID;
        int mEditHintResID;
        String mEditHintRes;
        int mEditErrResID;
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

        public Builder setEditHint(int id)
        {
            mEditHintResID = id;
            return this;
        }

        public Builder setEditHint(String str)
        {
            mEditHintRes = str;
            return this;
        }

        public Builder setEditErr(int id)
        {
            mEditErrResID = id;
            return this;
        }

        public Builder setAnimations(int id)
        {
            mAnimationResID = id;
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

        public DialogEditText build()
        {
            return new DialogEditText(this);
        }
    }
}
