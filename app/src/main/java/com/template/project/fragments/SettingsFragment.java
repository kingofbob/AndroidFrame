package com.template.project.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.template.project.R;
import com.template.project.adapters.SettingsAdapter;
import com.template.project.base.BaseMainFragment;
import com.template.project.objects.GeneralObject;
import com.template.project.utils.QuickUtils;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 70061667 on 19/12/2016.
 */

public class SettingsFragment extends BaseMainFragment {
    static final String[] SETTINGS = new String[] { "App Settings" };

    @Bind(R.id.toolbar)Toolbar mToolbar;
    @Bind(R.id.toolbar_frame)FrameLayout toolbarFrame;

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String title = getString(R.string.settings).toLowerCase();
        title = WordUtils.capitalize(title);
        mToolbar.setTitle(title);
        initToolbarNav(mToolbar);
        QuickUtils.addStatusBarGab(getActivity(), toolbarFrame);

        initSettings();
    }

    private void initSettings(){
        List<GeneralObject> settings = new ArrayList<>();
        settings.add(new GeneralObject(0,"App Settings"));

        ListAdapter settingsAdapter = new SettingsAdapter(getContext(), settings);
        ListView settingsListView = (ListView) getActivity().findViewById(R.id.lvSettings);
        settingsListView.setAdapter(settingsAdapter);

        settingsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    }
                }
        );
    }
}
