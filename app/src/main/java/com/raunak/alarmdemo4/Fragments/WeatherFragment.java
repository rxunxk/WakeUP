package com.raunak.alarmdemo4.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.raunak.alarmdemo4.R;

public class WeatherFragment extends Fragment {

    private WebView mWebView;
    private ImageView img;
    private Button btnTryAgain;
    private LinearLayout mLinearLayout;
    private ConnectivityManager conMgr;
    private NetworkInfo networkInfo;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                mWebViewGoBack();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather,container,false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        mWebView = getView().findViewById(R.id.webView);
        img = getView().findViewById(R.id.img404);
        btnTryAgain = getView().findViewById(R.id.btnTryAgain);
        mLinearLayout = getView().findViewById(R.id.linearLayout404);

        //Checking if user is connected to the internet or not.
        conMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = conMgr.getActiveNetworkInfo();

        //if INTERNET connected
        if (isConnected()) {
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.loadUrl("https://www.accuweather.com/");
        } else { //if INTERNET not connected
            mLinearLayout.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
        }

        //TryAgain Button Event handling
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {
                    mLinearLayout.setVisibility(View.GONE);
                    mWebView.setVisibility(View.VISIBLE);
                    mWebView.setWebViewClient(new WebViewClient());
                    mWebView.loadUrl("https://www.accuweather.com/");
                    Toast.makeText(getContext(), "Connected!", Toast.LENGTH_SHORT).show();
                } else { //if INTERNET not connected
                    Toast.makeText(getContext(), "No Internet :(", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //webView display settings
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_BACK
                        &&keyEvent.getAction() == MotionEvent.ACTION_UP
                        && mWebView.canGoBack()){
                    mHandler.sendEmptyMessage(1);
                    return true;
                }
                return false;
            }
        });

    }
    private void mWebViewGoBack(){
        mWebView.goBack();
    }
    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //if INTERNET connected
        if (isConnected()) {
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.loadUrl("https://www.accuweather.com/");
        } else { //if INTERNET not connected
            mLinearLayout.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
        }
    }
}
