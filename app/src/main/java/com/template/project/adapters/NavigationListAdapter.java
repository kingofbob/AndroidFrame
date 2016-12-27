package com.template.project.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.template.project.R;
import com.template.project.listobjects.NavigationRowObject;
import com.template.project.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class NavigationListAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private LayoutInflater inflater;
    private Context context;
    private List<NavigationRowObject> datas = new ArrayList<>();
    private NavigationRowObject headerDatas;
    private int selectedId = 0;

    public NavigationListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setData(List<NavigationRowObject> datas, NavigationRowObject headerDatas) {
        this.headerDatas = headerDatas;
        this.datas = datas;
    }

    public void setSelectedId(int selectedId){
        this.selectedId = selectedId;
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return datas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(NavigationListAdapter.class.getSimpleName(), "TYPE: " + datas.get(position).getTYPE() + " : " +position);
        switch (datas.get(position).getTYPE()){
            case NavigationRowObject.TYPE_DIVIDER:
                BodyDividerViewHolder bodyDividerViewHolder;

                if (convertView == null) {

                    convertView = inflater.inflate(R.layout.row_divider, parent, false);
                    bodyDividerViewHolder = new BodyDividerViewHolder(convertView);
                    convertView.setTag(bodyDividerViewHolder);
                } else {
                    bodyDividerViewHolder = (BodyDividerViewHolder) convertView.getTag();
                }

                break;

            case NavigationRowObject.TYPE_TITLE:
                BodyViewHolder bodyViewHolder;

                if (convertView == null) {

                    convertView = inflater.inflate(R.layout.row_navigation_title, parent, false);
                    bodyViewHolder = new BodyViewHolder(convertView);
                    convertView.setTag(bodyViewHolder);
                } else {
                    bodyViewHolder = (BodyViewHolder) convertView.getTag();
                }


                bodyViewHolder.titleText.setText(datas.get(position).getTitle());

                break;

            case NavigationRowObject.TYPE_WITH_ICON:
                BodyIconViewHolder bodyIconViewHolder;

                if (convertView == null) {

                    convertView = inflater.inflate(R.layout.row_navigation_row_icon, parent, false);
                    bodyIconViewHolder = new BodyIconViewHolder(convertView);
                    convertView.setTag(bodyIconViewHolder);
                } else {
                    bodyIconViewHolder = (BodyIconViewHolder) convertView.getTag();
                }


                bodyIconViewHolder.titleText.setText(datas.get(position).getTitle());
                bodyIconViewHolder.imageView.setImageResource(datas.get(position).getDrawble());

                if (selectedId == datas.get(position).getId()){
                    bodyIconViewHolder.backgroundLayout.setBackgroundColor(context.getResources().getColor(R.color.primary));
                }else{
                    bodyIconViewHolder.backgroundLayout.setBackgroundColor(context.getResources().getColor(R.color.primary_orange));
                }

                break;
        }


        return convertView;
    }


//    @Override
//    public int getItemViewType(int position) {
//        return datas.get(position).getTYPE();
//
//    }


    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getTYPE();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.row_navigation_header_icon, parent, false);
            holder = new HeaderViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        holder.imageView.setImageResource(headerDatas.getDrawble());
        if (headerDatas.getImgURL() != null && headerDatas.getImgURL().length() > 0){
//            Glide
//                .with(context)
//                .load(headerDatas.getImgURL())
//                .placeholder(R.drawable.img_profile_placeholder)
//                .into(holder.imageView);
            new ImageLoader.DownloadImageTask(holder.imageView)
                    .execute(headerDatas.getImgURL());
        }else{
            holder.imageView.setImageResource(R.drawable.img_profile_placeholder);
        }

        holder.titleText.setText(headerDatas.getTitle());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return 0;
    }


    class BodyIconViewHolder {

        @Bind(R.id.image)
        ImageView imageView;
        @Bind(R.id.title)
        TextView titleText;
        @Bind(R.id.background)LinearLayout backgroundLayout;



        public BodyIconViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);

        }
    }

    class BodyViewHolder {

        @Bind(R.id.title)
        TextView titleText;
        @Bind(R.id.background)LinearLayout backgroundLayout;



        public BodyViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);

        }
    }



    class BodyDividerViewHolder {



        public BodyDividerViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);

        }
    }

    class HeaderViewHolder {

        @Bind(R.id.profile_image_navigation)
        CircleImageView imageView;
        @Bind(R.id.title)
        TextView titleText;
        @Bind(R.id.background)LinearLayout backgroundLayout;



        public HeaderViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);

        }
    }
}