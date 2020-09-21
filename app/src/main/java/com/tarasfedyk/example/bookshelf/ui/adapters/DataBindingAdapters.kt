package com.tarasfedyk.example.bookshelf.ui.adapters

import android.net.Uri
import android.view.View
import android.webkit.WebView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController

@BindingConversion
fun toVisibility(value: Boolean) = if (value) View.VISIBLE else View.GONE

@BindingAdapter(value = ["navController", "appBarConfiguration"])
fun setUpWithNavController(
    toolbar: Toolbar,
    navController: NavController,
    appBarConfiguration: AppBarConfiguration
) {
    toolbar.setupWithNavController(navController, appBarConfiguration)
}

@BindingAdapter(value = ["isJavaScriptEnabled"])
fun setJavaScriptEnabled(
    webView: WebView,
    isJavaScriptEnabled: Boolean
) {
    webView.settings.javaScriptEnabled = isJavaScriptEnabled
}

@BindingAdapter(value = ["url"])
fun loadUrl(
    webView: WebView,
    url: Uri
) {
    webView.loadUrl(url.toString())
}
