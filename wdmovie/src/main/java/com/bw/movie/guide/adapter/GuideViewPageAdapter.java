package com.bw.movie.guide.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.bw.movie.R;
import com.bw.movie.guide.bean.GuideBean;
import java.util.ArrayList;
import java.util.List;

public class GuideViewPageAdapter extends PagerAdapter {

    private Context mContext;
    private List<GuideBean> mList;

    public GuideViewPageAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void setmList(List<GuideBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }



    /**
     *  @author Tang
     *  @time 2019/1/24  10:54
     *  @describe 设置一个view,根据页数不同展示不同的内容,当展示最后一页时触发点击事件
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        //   View inflate = LayoutInflater.from(mContext).inflate(R.layout.guide_first_fragment, container, false);
        View inflate = View.inflate(mContext, R.layout.guide_first_fragment, null);
        ImageView image = inflate.findViewById(R.id.image);
        TextView textView = inflate.findViewById(R.id.text1);
        TextView textView2 = inflate.findViewById(R.id.text2);

        RadioGroup radioGroup = inflate.findViewById(R.id.radioGroup);
        for (int i=0;i<mList.size();i++){
            RadioButton radioButton = new RadioButton(mContext);
          //添加选中背景
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.guide_selector);
            radioButton.setBackground(drawable);
            radioButton.setWidth(40);
            radioButton.setHeight(40);
            Bitmap a=null;
            radioButton.setButtonDrawable(new BitmapDrawable(a));

            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i!=0){
                int i1 = (int) (mContext.getResources().getDimension(R.dimen.dp_10));
                layoutParams.setMargins(i1,0,0,0);
            }else {
                layoutParams.setMargins(0,0,0,0);
            }


            radioButton.setClickable(false);
            if(i==position){
               radioButton.setChecked(true);
            }

            radioGroup.addView(radioButton,layoutParams);
        }

        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mList.size()-1==position){
                    callBack.callBack();
                }
            }
        });
         if(mList.size()==position){
            inflate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float y = 0;
                    float endy;
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                           y = event.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            
                            break;
                        case MotionEvent.ACTION_UP:
                            float rawY = event.getRawY();
                            if((rawY-y)>200){
                                callBack.callBack();
                            }
                            break;
                    }

                    return false;
                }
            });
        }
        image.setImageResource(mList.get(position).getImage());
        textView.setText(mList.get(position).getTitle());
        textView2.setText(mList.get(position).getTitle2());

        container.addView(inflate);
        return inflate;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
    GuideCallBack callBack ;
    public interface GuideCallBack{
        void callBack();
    }
public void setCallBack(GuideCallBack back){
        callBack=back;
}
}
