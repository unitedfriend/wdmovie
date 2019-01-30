package com.bw.movie.home.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.movie.R;

import com.bw.movie.home.bean.ReplyBean;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilmCommentAdapter extends RecyclerView.Adapter<FilmCommentAdapter.ViewHolder> {


    private List<ReplyBean.ResultBean> list;
    private Context context;

    public FilmCommentAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<ReplyBean.ResultBean> data) {
        list.clear();
        if (data != null) {
            list.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addList(List<ReplyBean.ResultBean> data) {
        if (data != null) {
            list.addAll(data);
        }
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.film_adapter_comment_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Uri uri = Uri.parse(list.get(position).getReplyHeadPic());
        holder.imageHeader.setImageURI(uri);
        holder.totalNum.setText("共"+list.size()+"条评论");
        holder.name.setText(list.get(position).getReplyUserName());
        holder.dateText.setText(stampToDate(list.get(position).getCommentTime()+""));
        holder.contentText.setText(list.get(position).getReplyContent());
    }
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.total_num)
        TextView totalNum;
        @BindView(R.id.image_header)
        SimpleDraweeView imageHeader;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.content_text)
        TextView contentText;
        @BindView(R.id.date_text)
        TextView dateText;
        @BindView(R.id.praise)
        ImageView praise;
        @BindView(R.id.parise_num)
        TextView pariseNum;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 点赞
     */
    private Lucky mLucky;

    public void setLucky(Lucky mLucky) {
        this.mLucky = mLucky;
    }

    public interface Lucky {
        void onLucky(int commentId, int position);
    }


    /**
     * 点击查看评论回复
     */
    private Click click;

    public void setClick(Click click) {
        this.click = click;
    }

    public interface Click {
        void onClick(int commentId, XRecyclerView film_comment_recyclerview);
    }


}
