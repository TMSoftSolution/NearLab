package com.nft.maker.ui.login_with_near

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import com.nft.maker.BaseActivity
import com.nft.maker.databinding.ActivityLoginWithNearBinding
import android.webkit.WebResourceError

import android.webkit.WebResourceRequest

import android.webkit.WebView

import androidx.annotation.RequiresApi

import android.webkit.WebViewClient
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginWithNearActivity : BaseActivity() {

    private lateinit var loginWithNearBinding: ActivityLoginWithNearBinding

    override val layoutId: View
        get() {
            loginWithNearBinding = ActivityLoginWithNearBinding.inflate(layoutInflater)
            return loginWithNearBinding.root
        }
    override val tag: String
        get() = LoginWithNearActivity::class.java.simpleName

    override fun created(savedInstance: Bundle?) {

        loginWithNearBinding.webView.settings.javaScriptEnabled = true
        loginWithNearBinding.webView.settings.blockNetworkLoads = false

        loginWithNearBinding.webView.settings.allowContentAccess = true
        loginWithNearBinding.webView.settings.setAppCacheEnabled(true)
        loginWithNearBinding.webView.settings.domStorageEnabled = true
        loginWithNearBinding.webView.settings.useWideViewPort = true
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            loginWithNearBinding.webView.addJavascriptInterface(JavaScriptInterface(), "javascriptinterface")

        loginWithNearBinding.webView.evaluateJavascript("javascript:window.localStorage.getItem('ncd-ii_wallet_auth_key')",
            ValueCallback<String?> { s -> Log.e("OnRecieve", s!!) })
//        loginWithNearBinding.webView.setWebChromeClient(new WebChromeClient() {
//            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
//
//                Log.d("MyApplication", message + " -- From line "
//                        + lineNumber + " of "
//                        + sourceID);
//            }
//        });

        loginWithNearBinding.webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                Log.d("returned url-->", url!!)
                Log.d("returned url--->", view?.url!!)

            }
            override fun onPageFinished(view: WebView, url: String) {
                Log.d("returned url-->", url)
                Log.d("returned url--->", view.url!!)
                if (view.url === "https://nftmaker.algorepublic.com/near_login/login.html") {
                }
            }

            //            @Override
            //            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //                System.out.println("onPageStarted: " + url);
            //            }
            //
            //            @Override
            //            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            //                System.out.println("shouldOverrideUrlLoading: " + url);
            ////                webView.loadUrl(url);
            //                return true;
            //            }
            @RequiresApi(api = Build.VERSION_CODES.M)
            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                Log.d("TAG", error.description.toString())
                // handler.proceed(); This line wont make a different on API 29, Webview still bank
            }
        }
        loginWithNearBinding.webView.loadUrl("https://nftmaker.algorepublic.com/near_login/login.html")
    }

    private class JavaScriptInterface {
        @JavascriptInterface
        fun callback(value: String?) {
            Log.d("JS", value!!)
        }
    }
}