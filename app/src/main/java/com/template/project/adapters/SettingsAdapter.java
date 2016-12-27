package com.template.project.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.template.project.R;
import com.template.project.objects.GeneralObject;

import java.util.List;

/**
 * Created by 70061667 on 5/12/2016.
 */

public class SettingsAdapter extends ArrayAdapter<GeneralObject> {

    public SettingsAdapter(Context context, List<GeneralObject> settings){
        super(context, R.layout.row_settings, settings);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.row_settings, parent, false);

        GeneralObject singleSetting = getItem(position);
        TextView setting = (TextView) view.findViewById(R.id.tvSetting);
        setting.setText(singleSetting.getTitle());
        return view;
    }
}
