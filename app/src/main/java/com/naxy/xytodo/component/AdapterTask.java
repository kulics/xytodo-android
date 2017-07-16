package com.naxy.xytodo.component;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.naxy.xytodo.R;
import com.naxy.xytodo.database.DBManager;
import com.naxy.xytodo.model.ModelTask;
import com.naxy.xytodo.tool.ToolFunction;

import java.util.ArrayList;
import java.util.List;


public class AdapterTask extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ModelTask> mDataset;//数据集
    private ViewHolderClicks mListenerIntent;//按钮监听
    private ViewHolderCbxChange mListenerCbx;//检查监听
    Context mContext;
    private DBManager mDBM;//数据库管理器

    public AdapterTask(Context context, DBManager dbm, ViewHolderClicks listenerIntent
            , ViewHolderCbxChange listenerCbx) {
        this.mContext = context;
        this.mDBM = dbm;
        this.mListenerIntent = listenerIntent;
        this.mListenerCbx = listenerCbx;
    }
    //获取整个列表数据
    public List<ModelTask> DataGetAll() {
        return mDataset;
    }
    //更新整个列表
    public void DataUpdate(String filterValue) {
        //清空数据
        mDataset = new ArrayList<>();
        List<ModelTask> dataset = mDBM.TaskGetTime(filterValue);
        //判断是否全部显示 filter为0时显示，否则过滤
        if (filterValue.equals("today")) {
            //遍历添加进数据集
            for (ModelTask v : dataset) {
                if (ToolFunction.ComputeDateToday(v.getTimeTarget())) {
                    mDataset.add(v);
                }
            }
        } else {
            //遍历添加进数据集
            for (ModelTask v : dataset) {
                mDataset.add(v);
            }
        }
        //刷新整体数据
        this.notifyDataSetChanged();
    }

    //添加数据
    public void DataAdd(ModelTask data) {
        mDataset.add(0, data);
        //使用动画插入
        this.notifyItemInserted(0);
    }
    //根据位置获取数据
    public ModelTask DataGetByPosition(int position) {
        return mDataset.get(position);
    }
    //获取统计数据
    public String CountGet() {
        int iCheckNum = 0;
        for (ModelTask item : mDataset) {
            if (item.getStatus() == 1){
                iCheckNum = iCheckNum + 1;
            }
        }
        return iCheckNum + "/" + mDataset.size();
    }

    public void UpdateStatus(int position){
        this.notifyItemChanged(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 根据布局文件实例化view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_card, parent, false);
        RecyclerView.ViewHolder vh = null;
        vh = new ViewHolderCard(v, mListenerIntent, mListenerCbx);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 给该控件设置数据(数据从集合类中来)
        ViewHolderCard vh = (ViewHolderCard) holder;
        ModelTask temp = mDataset.get(position);

        vh.tvContent.setText(temp.getContent());
        //转化时间
        vh.tvSub.setText(ToolFunction.GetDateFromUTC(mContext, temp.getTimeTarget()));
        //vh.cbxStatus.setButtonTintList(new ColorStateList());

        //根据选择状态作不同显示
        if (temp.getStatus() == 1){
            //文字加横线
            vh.tvContent.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
            vh.tvSub.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
            //设置选择
            vh.cbxStatus.setChecked(true);
            //设置背景色
            vh.cvContent.setBackgroundColor(ContextCompat.getColor(mContext, R.color.PURE_GRAY_500));
        }else{
            vh.tvContent.getPaint().setFlags(Paint. ANTI_ALIAS_FLAG );
            vh.tvSub.getPaint().setFlags(Paint. ANTI_ALIAS_FLAG );
            vh.cbxStatus.setChecked(false);
            vh.cvContent.setBackgroundColor(ContextCompat.getColor(mContext, getColor(temp.getColor())));
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface ViewHolderClicks {
        void onViewClick(int position, View text, View background);
    }

    public interface ViewHolderBtnClicks {
        void onViewClick(int position);
    }

    public interface ViewHolderCbxChange {
        void onViewClick(int position, boolean isChecked);
    }

    private static class ViewHolderCard extends RecyclerView.ViewHolder {
        TextView tvContent;//文本
        TextView tvSub;//文本
        //        AppCompatImageButton btnMore;
        AppCompatCheckBox cbxStatus;
        RelativeLayout rlytRippleContent;//内容布局
        CardView cvContent;//背景布局
        ViewHolderClicks mListenerClick;//ITEM监听
        //ViewHolderBtnClicks mListenerBtn;//按钮监听
        ViewHolderCbxChange mListenerCbx;//检查监听


        ViewHolderCard(View v, ViewHolderClicks listenerIntent, ViewHolderCbxChange listenerCbx) {
            super(v);
            mListenerClick = listenerIntent;
            mListenerCbx = listenerCbx;
            // 加载控件
            tvContent = (TextView) v.findViewById(R.id.tv_content);
            tvSub = (TextView) v.findViewById(R.id.tv_sub);
//            btnMore = (AppCompatImageButton) v.findViewById(R.id.ib_btnMore);
            cbxStatus = (AppCompatCheckBox) v.findViewById(R.id.cbx_status);
            rlytRippleContent = (RelativeLayout) v
                    .findViewById(R.id.item_rippleContent);
            cvContent = (CardView) v
                    .findViewById(R.id.cardview);
            //设置监听
//            btnMore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mListenerBtn != null) {
//                        mListenerBtn.onViewClick(getAdapterPosition());
//                    }
//                }
//            });
            rlytRippleContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListenerClick != null) {
                        mListenerClick.onViewClick(getAdapterPosition(), tvContent, cvContent);
                    }
                }
            });
            cbxStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mListenerCbx != null) {
                        mListenerCbx.onViewClick(getAdapterPosition(), isChecked);
                    }
                }
            });
        }
    }

    //获取颜色方法
    public int getColor(String tag) {
        int id = 0;
        switch (tag) {
            case "green":
                id = R.color.PURE_GREEN_500;
                break;
            case "blue":
                id = R.color.PURE_BLUE_500;
                break;
            case "red":
                id = R.color.PURE_RED_500;
                break;
            case "yellow":
                id = R.color.PURE_YELLOW_500;
                break;
            default:
                id = R.color.PURE_BLUE_500;
                break;
        }
        return id;
    }

    //获取颜色方法
    public int getColor2(String tag) {
        int id = 0;
        switch (tag) {
            case "green":
                id = R.color.PURE_GREEN_700;
                break;
            case "blue":
                id = R.color.PURE_BLUE_700;
                break;
            case "red":
                id = R.color.PURE_RED_700;
                break;
            case "yellow":
                id = R.color.PURE_YELLOW_700;
                break;
            default:
                id = R.color.PURE_BLUE_700;
                break;
        }
        return id;
    }
}

