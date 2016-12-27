package com.template.project.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.util.Log;
import android.webkit.WebViewClient;

import com.activeandroid.query.Select;
import com.template.project.R;
import com.template.project.base.BaseMainFragment;
import com.template.project.constants.URLConst;
import com.template.project.database.entities.CMS;
import com.template.project.utils.QuickUtils;

import org.apache.commons.lang3.text.WordUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 70061667 on 24/11/2016.
 */

public class TermsAndConditionsFragment extends BaseMainFragment{
    @Bind(R.id.toolbar)Toolbar mToolbar;

    public static TermsAndConditionsFragment newInstance() {

        Bundle args = new Bundle();
        QuickUtils.showProgressBar();

        TermsAndConditionsFragment fragment = new TermsAndConditionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_terms_and_conditions, container, false);
        ButterKnife.bind(this, view);

        try{
            WebView siteViewer = (WebView) view.findViewById(R.id.wvTermsAndConditions);
            CMS termsAndConditions = new Select().from(CMS.class).where("CMS_Code=?", "TERMS_AND_CONDITIONS").executeSingle();

            if(siteViewer != null && termsAndConditions != null){
                String url = termsAndConditions.getCMS_URL();
                WebSettings settings = (WebSettings) siteViewer.getSettings();
                settings.setJavaScriptEnabled(true);
                settings.setBuiltInZoomControls(true);
                settings.setDisplayZoomControls(false);
                siteViewer.setWebViewClient(getWebViewClient());
                siteViewer.loadUrl(URLConst.PdfViewerUrl + url);
            }
        } catch (Exception e){
            Log.e(CMS.class.getSimpleName(), "execute", e);
        }

        return view;
    }

    private WebViewClient getWebViewClient(){
        return new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                QuickUtils.hideProgressBar();
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String title = getString(R.string.terms_and_conditions).toLowerCase();
        title = WordUtils.capitalize(title);
        mToolbar.setTitle(title);
        initToolbarNav(mToolbar, true);
        mToolbar.inflateMenu(R.menu.home);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QuickUtils.hideProgressBar();
    }

}
