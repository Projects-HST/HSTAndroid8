package com.hst.ops.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hst.ops.R;

public class LinkedinFragment extends Fragment {

    private View rootView;
    WebView mWebView;

    public static LinkedinFragment newInstance(int position) {
        LinkedinFragment frag = new LinkedinFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_socialo_media, container, false);

        mWebView = (WebView) rootView.findViewById(R.id.webview);
        mWebView.loadUrl("https://www.linkedin.com/in/panneerselvam-o-033762192/");

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());



        return rootView;
    }

}