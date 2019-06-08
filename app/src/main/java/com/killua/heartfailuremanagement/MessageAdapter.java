package com.killua.heartfailuremanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends BaseAdapter {
    //定义两个类别标志
    private Context mContext;
    private ArrayList<message> mData = null;


    public MessageAdapter(Context mContext, ArrayList<message> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //多布局的核心，通过这个判断类别
    @Override
    public int getItemViewType(int position) {
        return mData.get(position).type;
    }

    //类别数目
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public void add(message data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(data);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder holder = null;
        if(convertView == null){
            switch (type){
                case 0:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.message_left, parent, false);
                    break;
                case 1:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.message_right, parent, false);
                    break;
            }
            holder = new ViewHolder();
            holder.portrait = (CircleImageView) convertView.findViewById(R.id.portrait);
            holder.head = (TextView) convertView.findViewById(R.id.head);
            holder.text=(TextView)convertView.findViewById(R.id.text);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        message obj = mData.get(position);
        //设置下控件的值
        holder.portrait.setImageResource(obj.portrait);
        holder.head.setText(obj.name+" --"+obj.time);
        holder.text.setText(obj.text);
        return convertView;
    }

    private static class ViewHolder{
        CircleImageView portrait;
        TextView head;
        TextView text;
    }

}
