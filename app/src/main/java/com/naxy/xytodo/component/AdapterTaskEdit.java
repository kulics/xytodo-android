package com.naxy.xytodo.component;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.naxy.xytodo.R;
import com.naxy.xytodo.database.DBManager;
import com.naxy.xytodo.model.ModelTask;
import com.naxy.xytodo.model.ModelTaskSub;
import com.naxy.xytodo.tool.ToolFunction;

import java.util.Date;
import java.util.List;


public class AdapterTaskEdit extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ModelTask mDataTask;//任务数据
    private List<ModelTaskSub> mDataTaskSub;//子任务数据
    private int iCheckNum;
    private ViewHolderClicks mListenerIntent;
    private ViewHolderBtnClicks mListenerBtn;//按钮监听
    private Context mContext;
    private DBManager mDBM;

    public AdapterTaskEdit(Context context, DBManager dbm, ViewHolderClicks listenerIntent, ViewHolderBtnClicks listenerBtn)
    {
        this.mContext = context;
        this.mDBM = dbm;
        this.mListenerIntent = listenerIntent;
        this.mListenerBtn = listenerBtn;
    }

    public void UpdateDataset(ModelTask task)
    {
        mDataTask = task;
        mDataTaskSub = task.getSub();
        iCheckNum = 0;
        for (ModelTaskSub item : mDataTaskSub)
        {
            if (item.getStatus() == 1)
            {
                iCheckNum = iCheckNum + 1;
            }
        }
        this.notifyDataSetChanged();
    }

    public void UpdateStatus(int position)
    {
        this.notifyItemChanged(position);
    }

    public void TimeUpdate()
    {
        this.notifyItemChanged(1);
    }

    public void NoteUpdate(String note)
    {
        mDataTask.setNote(note);
        this.notifyItemChanged(2);
    }

    public void DataSubAdd()
    {
        this.notifyItemInserted(this.getItemCount());
        mDataTaskSub.add(new ModelTaskSub());
        this.notifyItemChanged(0);
        //this.notifyDataSetChanged();
    }

    public void DataSubDelete(int index)
    {
        //this.notifyItemRemoved(index);
        if (mDataTaskSub.get(index - 3).getStatus() == 1)
        {
            iCheckNum = iCheckNum - 1;
        }
        mDataTaskSub.remove(index - 3);
        this.notifyDataSetChanged();
    }

    public ModelTask DataTaskGet()
    {
        return mDataTask;
    }

    public List<ModelTaskSub> DataSubGet()
    {
        return mDataTaskSub;
    }

    @Override
    public int getItemCount()
    {
        return 3 + mDataTaskSub.size();
    }

    // 每个convert view都会调用此方法，获得当前所需要的view样式
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // 根据布局文件实例化view
        View v = null;
        RecyclerView.ViewHolder vh = null;
        switch (viewType)
        {
            case 0:
                // 根据布局文件实例化view
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_item_task_main, parent, false);
                vh = new ViewHolderTaskMain(v, mListenerIntent, new EditTextListener()
                {
                    @Override
                    public void onTextEdit(int position, String str)
                    {
                        mDataTask.setContent(str);
                    }
                }, new ViewHolderCbxChange()
                {
                    @Override
                    public void onViewClick(int position, boolean isChecked)
                    {
                        //必须先对当前状态做判断，因为每次装填都会触发事件，这里需要过滤
                        int b = isChecked ? 1 : 0;
                        if (mDataTask.getStatus() != b)
                        {
                            mDataTask.setStatus(b);
                            long unixTime = new Date().getTime();//获取当前时区下日期时间对应的时间戳
                            mDataTask.setTimeDone((int) (unixTime / 1000));
                            //刷新对应条目
                            UpdateStatus(0);
                        }
                    }
                });
                break;
            case 1:
                // 根据布局文件实例化view
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_item_task_time, parent, false);
                vh = new ViewHolderTaskTime(v, mListenerIntent, mListenerBtn);
                break;
            case 2:
                // 根据布局文件实例化view
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_item_task_note, parent, false);
                vh = new ViewHolderTaskNote(v, mListenerIntent);
                break;
            default:
                // 根据布局文件实例化view
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_item_task_sub, parent, false);
                vh = new ViewHolderTaskSub(v, mListenerIntent, mListenerBtn, new EditTextListener()
                {
                    @Override
                    public void onTextEdit(int position, String str)
                    {
                        ModelTaskSub temp = mDataTaskSub.get(position - 3);
                        temp.setContent(str.toString().trim());
                    }
                }, new ViewHolderCbxChange()
                {
                    @Override
                    public void onViewClick(int position, boolean isChecked)
                    {
                        //必须先对当前状态做判断，因为每次装填都会触发事件，这里需要过滤
                        int b = isChecked ? 1 : 0;
                        ModelTaskSub temp = mDataTaskSub.get(position - 3);
                        if (temp.getStatus() != b)
                        {
                            temp.setStatus(b);
                            if (b == 1)
                            {
                                iCheckNum = iCheckNum + 1;
                            }
                            else
                            {
                                iCheckNum = iCheckNum - 1;
                            }
                            //刷新对应条目
                            AdapterTaskEdit.this.notifyItemChanged(0);
                            UpdateStatus(position);
                        }
                    }
                });
                break;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        // 给该控件设置数据(数据从集合类中来)
        switch (position)
        {
            case 0:
                ViewHolderTaskMain holder0 = (ViewHolderTaskMain) holder;
                holder0.tvContent.setText(mDataTask.getContent());
                holder0.tvContent.setHint(R.string.content);
                holder0.cbxStatus.setChecked(mDataTask.getStatus() == 1);
                String count = iCheckNum + "/" + mDataTaskSub.size();
                //holder0.tvSub.setText(time + "  ▪  " + count);
                holder0.tvSub.setText(count);
                if (mDataTask.getStatus() == 1)
                {
                    //文字加横线
                    holder0.tvContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    //文字改颜色
                    holder0.tvContent.setTextColor(ContextCompat.getColor(mContext, R.color.PURE_GRAY_500));
                }
                else
                {
                    holder0.tvContent.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
                    holder0.tvContent.setTextColor(ContextCompat.getColor(mContext, R.color.PURE_BLACK_500));
                }
                break;
            case 1:
                ViewHolderTaskTime holder1 = (ViewHolderTaskTime) holder;
                //转化时间
                String time = ToolFunction.GetDateFromUTC(mContext, mDataTask.getTimeTarget());
                holder1.tvContent.setText(time);
                break;
            case 2:
                ViewHolderTaskNote holder2 = (ViewHolderTaskNote) holder;
                holder2.tvContent.setText(mDataTask.getNote());
                holder2.tvContent.setHint(R.string.note);
                break;
            default:
                ViewHolderTaskSub holder3 = (ViewHolderTaskSub) holder;
                holder3.tvContent.setHint(R.string.content);
                ModelTaskSub sub = mDataTaskSub.get(position - 3);
                holder3.tvContent.setText(sub.getContent());
                if (sub.getStatus() == 1)
                {
                    //文字加横线
                    holder3.tvContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    //文字改颜色
                    holder3.tvContent.setTextColor(ContextCompat.getColor(mContext, R.color.PURE_GRAY_500));
                    holder3.cbxStatus.setChecked(true);
                }
                else
                {
                    holder3.tvContent.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
                    holder3.tvContent.setTextColor(ContextCompat.getColor(mContext, R.color.PURE_BLACK_500));
                    holder3.cbxStatus.setChecked(false);
                }
                break;
        }
    }

    public interface ViewHolderClicks
    {
        void onViewClick(int position);
    }

    public interface ViewHolderBtnClicks
    {
        void onViewClick(int position, View v);
    }

    interface EditTextListener
    {
        void onTextEdit(int position, String str);
    }

    public interface ViewHolderCbxChange
    {
        void onViewClick(int position, boolean isChecked);
    }

    private static class ViewHolderTaskMain extends RecyclerView.ViewHolder
    {
        EditText tvContent;
        AppCompatCheckBox cbxStatus;
        TextView tvSub;//文本
        RelativeLayout rlytRippleContent;//内容布局
        ViewHolderClicks mListenerContact;//ITEM监听
        EditTextListener mListenerTextContent;
        ViewHolderCbxChange mListenerCbx;//检查监听

        ViewHolderTaskMain(View v, ViewHolderClicks listenerIntent, EditTextListener listenerTextContent, ViewHolderCbxChange listenerCbx)
        {
            super(v);
            mListenerContact = listenerIntent;
            mListenerTextContent = listenerTextContent;
            mListenerCbx = listenerCbx;
            // 加载控件
            tvContent = (EditText) v.findViewById(R.id.tv_content);
            tvSub = (TextView) v.findViewById(R.id.tv_sub);
            cbxStatus = (AppCompatCheckBox) v.findViewById(R.id.cbx_status);
            rlytRippleContent = (RelativeLayout) v
                    .findViewById(R.id.item_rippleContent);
            rlytRippleContent.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mListenerContact != null)
                    {
                        mListenerContact.onViewClick(getAdapterPosition());
                    }
                }
            });
            tvContent.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    if (mListenerTextContent != null)
                    {
                        mListenerTextContent.onTextEdit(getAdapterPosition(), s.toString().trim());
                    }
                }

                @Override
                public void afterTextChanged(Editable s)
                {

                }
            });
            cbxStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (mListenerCbx != null)
                    {
                        mListenerCbx.onViewClick(getAdapterPosition(), isChecked);
                    }
                }
            });
        }
    }

    private static class ViewHolderTaskNote extends RecyclerView.ViewHolder
    {
        TextView tvContent;
        RelativeLayout rlytRippleContent;//内容布局
        ViewHolderClicks mListenerContact;//ITEM监听

        ViewHolderTaskNote(View v, ViewHolderClicks listenerIntent)
        {
            super(v);
            mListenerContact = listenerIntent;
            // 加载控件
            tvContent = (TextView) v.findViewById(R.id.tv_content);
            rlytRippleContent = (RelativeLayout) v
                    .findViewById(R.id.item_rippleContent);
            rlytRippleContent.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mListenerContact != null)
                    {
                        mListenerContact.onViewClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    private static class ViewHolderTaskTime extends RecyclerView.ViewHolder
    {
        TextView tvContent;
        AppCompatImageButton btnMore;
        RelativeLayout rlytRippleContent;//内容布局
        ViewHolderClicks mListenerContact;//ITEM监听
        ViewHolderBtnClicks mListenerBtn;//按钮监听

        ViewHolderTaskTime(View v, ViewHolderClicks listenerIntent, ViewHolderBtnClicks linstenerBtn)
        {
            super(v);
            mListenerContact = listenerIntent;
            mListenerBtn = linstenerBtn;
            // 加载控件
            tvContent = (TextView) v.findViewById(R.id.tv_content);
            btnMore = (AppCompatImageButton) v.findViewById(R.id.ib_btnMore);
            rlytRippleContent = (RelativeLayout) v
                    .findViewById(R.id.item_rippleContent);
            rlytRippleContent.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mListenerContact != null)
                    {
                        mListenerContact.onViewClick(getAdapterPosition());
                    }
                }
            });
            btnMore.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (mListenerBtn != null)
                    {
                        mListenerBtn.onViewClick(getAdapterPosition(), btnMore);
                    }
                }
            });
        }
    }


    private static class ViewHolderTaskSub extends RecyclerView.ViewHolder
    {
        EditText tvContent;
        AppCompatImageButton btnDelete;
        AppCompatCheckBox cbxStatus;
        RelativeLayout rlytRippleContent;//内容布局
        ViewHolderClicks mListenerContact;//ITEM监听
        ViewHolderBtnClicks mListenerBtn;//按钮监听
        EditTextListener mListenerTextContent;
        ViewHolderCbxChange mListenerCbx;//检查监听

        ViewHolderTaskSub(View v, ViewHolderClicks listenerIntent, ViewHolderBtnClicks linstenerBtn, EditTextListener listenerTextContent,
                          ViewHolderCbxChange listenerCbx)
        {
            super(v);
            mListenerContact = listenerIntent;
            mListenerBtn = linstenerBtn;
            mListenerTextContent = listenerTextContent;
            mListenerCbx = listenerCbx;
            // 加载控件
            tvContent = (EditText) v.findViewById(R.id.tv_content);
            btnDelete = (AppCompatImageButton) v.findViewById(R.id.ib_btnMore);
            cbxStatus = (AppCompatCheckBox) v.findViewById(R.id.cbx_status);
            rlytRippleContent = (RelativeLayout) v
                    .findViewById(R.id.item_rippleContent);
            rlytRippleContent.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mListenerContact != null)
                    {
                        mListenerContact.onViewClick(getAdapterPosition());
                    }
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (mListenerBtn != null)
                    {
                        mListenerBtn.onViewClick(getAdapterPosition(), btnDelete);
                    }
                }
            });
            tvContent.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    if (mListenerTextContent != null)
                    {
                        mListenerTextContent.onTextEdit(getAdapterPosition(), s.toString().trim());
                    }
                }

                @Override
                public void afterTextChanged(Editable s)
                {

                }
            });
            cbxStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (mListenerCbx != null)
                    {
                        mListenerCbx.onViewClick(getAdapterPosition(), isChecked);
                    }
                }
            });
        }
    }

}

