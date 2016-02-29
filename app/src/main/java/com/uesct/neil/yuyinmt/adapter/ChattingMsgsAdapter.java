package com.uesct.neil.yuyinmt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uesct.neil.yuyinmt.R;
import com.uesct.neil.yuyinmt.bean.ChattingMsgItem;

import java.util.List;

/**
 * Created by Neil on 2016/2/25.
 */
public class ChattingMsgsAdapter extends BaseAdapter {
    private List<ChattingMsgItem> mDatas;
    private Context mContext;
    private int layoutId;

    public ChattingMsgsAdapter(Context context, int layoutId, List<ChattingMsgItem> datas) {
        this.mContext = context;
        this.mDatas = datas;
        initDatas();
        this.layoutId = layoutId;
    }

    /**
     * 初始化Adapter
     */
    private void initDatas() {
        //可在此添加初始化数据
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            //对每个Item的View进行操作
            convertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
            //viewHolder成员与item的view对应
            viewHolder = new ViewHolder();
//            viewHolder.ivMTpic = (ImageView) convertView.findViewById(R.id.id_ivMTpic);
            viewHolder.tvMe = (TextView) convertView.findViewById(R.id.id_tvChattingMsgMe);
            viewHolder.tvMT = (TextView) convertView.findViewById(R.id.id_tvChattingMsgMT);
            viewHolder.llMT = (LinearLayout) convertView.findViewById(R.id.id_llMT);
            viewHolder.llME = (LinearLayout) convertView.findViewById(R.id.id_llME);
            //View与ViewHolder绑定
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.llMT.setVisibility(View.GONE);
        viewHolder.llME.setVisibility(View.GONE);

        ChattingMsgItem msgItem = mDatas.get(position);
        //对每个Item的View设置内容及Visibility
        if (msgItem.getType() == ChattingMsgItem.ChattingMsgType.Me) {
            viewHolder.tvMe.setText(msgItem.getContent());
            viewHolder.llME.setVisibility(View.VISIBLE);
            viewHolder.llMT.setVisibility(View.GONE);
        } else {
            viewHolder.tvMT.setText(msgItem.getContent());
//            if (msgItem.getPicMTId() > 0)
//                viewHolder.ivMTpic.setImageResource(R.mipmap.ic_launcher);
            viewHolder.llMT.setVisibility(View.VISIBLE);
//            viewHolder.ivMTpic.setVisibility(View.GONE);
            viewHolder.llME.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvMe;
        TextView tvMT;
        //        ImageView ivMTpic;
        LinearLayout llMT;
        LinearLayout llME;
    }
}
