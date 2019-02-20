package com.bw.movie.my.adaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.my.bean.SystemMessageBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SystemMessageAdapter extends RecyclerView.Adapter<SystemMessageAdapter.ViewHodel> {

    private List<SystemMessageBean.ResultBean>mList;
    private Context mContext;

    public SystemMessageAdapter(Context mContext) {
        this.mContext = mContext;
        mList=new ArrayList<>();
    }

public void setmList(List<SystemMessageBean.ResultBean>list){
        if(list!=null){
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
}
    public void addmList(List<SystemMessageBean.ResultBean>list){
        if(list!=null){

            mList.addAll(list);
            notifyDataSetChanged();
        }
    }
    @NonNull
    @Override
    public ViewHodel onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //View view = View.inflate(mContext, R.layout.item_system_message,null);
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.item_system_message,viewGroup,false);

        return new ViewHodel(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHodel viewHodel, final int i) {
        final SystemMessageBean.ResultBean resultBean = mList.get(i);
        viewHodel.content.setText(resultBean.getContent());
        viewHodel.name.setText(resultBean.getTitle());
        String s = stampToDate(resultBean.getPushTime());
        viewHodel.tiem.setText(s);
        if(resultBean.getStatus()==0){
            viewHodel.num.setVisibility(View.VISIBLE);
        }else{
            viewHodel.num.setVisibility(View.INVISIBLE);
        }
        viewHodel.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resultBean.getStatus()==0){
                    back.callback(mList.get(i),i);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }
public void setChange(int p){
        mList.get(p).setStatus(1);
        notifyDataSetChanged();
}
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    static class ViewHodel extends RecyclerView.ViewHolder{
        @BindView(R.id.messageName)
        TextView name;
        @BindView(R.id.messageContent)
        TextView content;
        @BindView(R.id.timeText)
        TextView tiem;
        @BindView(R.id.messageNum)
        TextView num;
        public ViewHodel(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    CallBack back;
    public interface CallBack{
      void  callback(SystemMessageBean.ResultBean resultBean,int n);
    }
    public void setBack(CallBack c){
        back=c;
    }
}
