package com.template.project.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.template.project.R;
import com.template.project.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 主页HomeFragment  Adapter
 * Created by YoKeyword on 16/2/1.
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> mItems = new ArrayList<>();
    private LayoutInflater mInflater;
    HashMap<String, Integer> url_maps = new HashMap<String, Integer>();

    private final int IS_HEADER = 0;
    private final int IS_BODY = 1;

    private OnItemClickListener mClickListener;
    private Context context;

    public MainAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setDatas(List<String> items,  HashMap<String, Integer> url_maps) {
        mItems.clear();
        mItems.addAll(items);
        this.url_maps = url_maps;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == IS_HEADER){
            View headerView = mInflater.inflate(R.layout.header_main, parent, false);
            MyHeaderViewHolder myHeaderViewHolder = new MyHeaderViewHolder(headerView);

            return  myHeaderViewHolder;
        }else{
            View view = mInflater.inflate(R.layout.row_main_grid, parent, false);
            final MyViewHolder holder = new MyViewHolder(view);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();

                    if (mClickListener != null) {
                        mClickListener.onItemClick(position, v);
                    }
                }
            });
            return holder;
        }




    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == IS_HEADER){

            for (String name : url_maps.keySet()) {
                TextSliderView textSliderView = new TextSliderView(context);
                // initialize a SliderLayout
                textSliderView
                        .description(name)
                        .image(url_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {

                            }
                        });

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", name);

                ((MyHeaderViewHolder) holder).sliderLayout.addSlider(textSliderView);
            }
            ((MyHeaderViewHolder) holder).sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
            ((MyHeaderViewHolder) holder).sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//            ((MyHeaderViewHolder) holder).sliderLayout.setCustomAnimation(new DescriptionAnimation());

            ((MyHeaderViewHolder) holder).sliderLayout.setDuration(10000);
            ((MyHeaderViewHolder) holder).sliderLayout.setCustomIndicator(((MyHeaderViewHolder) holder).pagerIndicator);
            ((MyHeaderViewHolder) holder).sliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }else{
            ((MyViewHolder) holder).title.setText(mItems.get(position) + position);
        }



    }

    @Override
    public int getItemCount() {
//        return mItems.size();
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return IS_HEADER;
        }
        return IS_BODY;
    }

    public boolean isHeader(int position){
        if (position == 0){
            return true;
        }

        return false;
    }

//
//    public t_event_db getItem(int position) {
//        return mItems.get(position);
//    }

    class MyViewHolder extends RecyclerView.ViewHolder {
//        TextView module;
        TextView title;
//        TextView date;
//        TextView status;
//        TextView attended;

        public MyViewHolder(View itemView) {
            super(itemView);
//            module = (TextView) itemView.findViewById(R.id.module);
            title = (TextView) itemView.findViewById(R.id.title);
//            date = (TextView) itemView.findViewById(R.id.date);
//            status = (TextView) itemView.findViewById(R.id.status);
//            attended= (TextView) itemView.findViewById(R.id.attended);
//
//

        }
    }

    class MyHeaderViewHolder extends RecyclerView.ViewHolder {

        SliderLayout sliderLayout;
        PagerIndicator pagerIndicator;

        public MyHeaderViewHolder(View itemView) {
            super(itemView);

            sliderLayout = (SliderLayout) itemView.findViewById(R.id.slider);
            pagerIndicator = (PagerIndicator) itemView.findViewById(R.id.custom_indicator);

            View descriptionLayout = itemView.findViewById(com.daimajia.slider.library.R.id.description_layout);
            if(descriptionLayout!=null){
                itemView.findViewById(com.daimajia.slider.library.R.id.description_layout).setVisibility(View.GONE);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


}
