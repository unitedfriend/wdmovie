package com.bw.movie.home.adapter;

import com.bw.movie.home.bean.FindAllCinemaComment;
       import android.content.Context;
        import android.net.Uri;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.bw.movie.R;

        import com.bw.movie.home.bean.FilmCommentBean;

        import com.facebook.drawee.view.SimpleDraweeView;
        import com.jcodecraeer.xrecyclerview.XRecyclerView;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;

        import butterknife.BindView;
        import butterknife.ButterKnife;

public class FindAllCinemaCommentAdapter extends RecyclerView.Adapter<FindAllCinemaCommentAdapter.ViewHolder> {

    private List<FindAllCinemaComment.ResultBean> list;
    private Context context;

    public FindAllCinemaCommentAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<FindAllCinemaComment.ResultBean> data) {
        list.clear();
        if (data!=null){
            list.addAll(data);
        }
        notifyDataSetChanged();
    }
    public void addList(List<FindAllCinemaComment.ResultBean> data) {
        if (data!=null){
            list.addAll(data);
        }
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.film_adapter_revirw_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Uri uri =Uri.parse(list.get(position).getCommentHeadPic());
        holder.imageHeader.setImageURI(uri);
        holder.name.setText(list.get(position).getCommentUserName());
        holder.contentText.setText(list.get(position).getCinemaComment());
        holder.dateText.setText(stampToDate(list.get(position).getCommentTime()+""));
        holder.pariseNum.setText(list.get(position).getGreatNum()+"");
        if (list.get(position).getIsGreat()==0){
            holder.praise.setBackgroundResource(R.mipmap.com_icon_praise_default);
        }else if(list.get(position).getIsGreat()==1){
            holder.praise.setBackgroundResource(R.mipmap.com_icon_praise_selected);
        }
        //点赞
        holder.praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLucky!=null) {
                    mLucky.onLucky(list.get(position).getCommentId(),position);
                }
            }
        });
        //查看评论回复
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.film_comment_recyclerview.setVisibility(View.VISIBLE);
                if (click!=null){
                    click.onClick(list.get(position).getCommentId(),holder.film_comment_recyclerview);
                }
            }
        });
    }

    public void setLike(int p){
        list.get(p).setIsGreat(1);
        int greatNum = list.get(p).getGreatNum();
        list.get(p).setGreatNum(greatNum+1);
    }
    /**
     *设置点赞
     *@author Administrator
     *@time 2019/1/1 0001 18:14
     */
    public void addWhetherGreat(int position) {
        list.get(position).setIsGreat(1);
        list.get(position).setGreatNum(list.get(position).getGreatNum()+1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.comment)
        ImageView comment;
        @BindView(R.id.comment_num)
        TextView commentNum;
        @BindView(R.id.film_comment_recyclerview)
        XRecyclerView film_comment_recyclerview;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    /**点赞*/
    private Lucky mLucky;
    public void setLucky(Lucky mLucky) {
        this.mLucky = mLucky;
    }
    public interface Lucky {
        void onLucky(int commentId, int position);
    }
    /**点击查看评论回复*/
    private Click click;
    public void setClick(Click click) {
        this.click = click;
    }
    public interface Click {
        void onClick(int commentId, XRecyclerView film_comment_recyclerview);
    }
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
