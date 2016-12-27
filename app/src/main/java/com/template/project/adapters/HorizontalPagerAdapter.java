package com.template.project.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.template.project.R;
import com.template.project.activity.MainActivity;
import com.template.project.listobjects.NavigationRowObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HorizontalPagerAdapter extends PagerAdapter {


    List<NavigationRowObject>datas = new ArrayList<>();

    private Context context;
    private LayoutInflater mLayoutInflater;


    public HorizontalPagerAdapter(Context context) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setDatas(List<NavigationRowObject>datas){
        List<NavigationRowObject> dataWithIcon = new ArrayList<>();
        for (NavigationRowObject navigationRowObject: datas){
            if (navigationRowObject.getTYPE() == NavigationRowObject.TYPE_WITH_ICON){
                dataWithIcon.add(navigationRowObject);
            }
        }
        this.datas = dataWithIcon;

    }

    @Override
    public int getCount() {

        return datas.size();
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View view = mLayoutInflater.inflate(R.layout.row_landingpage_tiles, container, false);
        CardViewHolder cardViewHolder = new CardViewHolder(view);
        cardViewHolder.iconImage.setImageResource(datas.get(position).getDrawble());
        cardViewHolder.titleText.setText(datas.get(position).getTitle());
        cardViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).onNavigationItemSelected(datas.get(position).getId());
            }
        });
        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }


    class CardViewHolder {

        @Bind(R.id.title)TextView titleText;
        @Bind(R.id.image)ImageView iconImage;
        @Bind(R.id.cardview)CardView cardView;

        public CardViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);

        }
    }
}