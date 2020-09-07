package com.tarasfedyk.example.bookshelf.ui.adapters

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.webkit.WebViewAssetLoader
import com.tarasfedyk.example.bookshelf.R
import com.tarasfedyk.example.bookshelf.biz.models.SpineItem
import kotlinx.android.synthetic.main.spine_item_view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class MainSpineItemsAdapter @Inject constructor(
    fragment: Fragment
) : SpineItemsAdapter(fragment) {

    override var items: List<SpineItem>? by Delegates.observable(null) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun createFragment(position: Int): Fragment {
        val item = items!![position]
        return SpineItemFragment.newInstance(item)
    }
}

class SpineItemFragment : Fragment() {
    private lateinit var spineItem: SpineItem

    init {
        lifecycleScope.launchWhenCreated {
            spineItem = requireArguments().getParcelable(KEY_SPINE_ITEM)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.spine_item_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        web_view.isVisible = false
        progress_bar.isVisible = true
        web_view.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(webView: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    web_view?.isVisible = true
                    progress_bar?.isVisible = false
                }
            }
        }

        val bookDir = spineItem.bookDir
        val bookDirName = bookDir.name
        val webViewAssetLoader = WebViewAssetLoader
            .Builder()
            .addPathHandler(
                "/$bookDirName/",
                WebViewAssetLoader.InternalStoragePathHandler(
                    requireContext(),
                    bookDir
                )
            )
            .build()
        web_view.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                return webViewAssetLoader.shouldInterceptRequest(request.url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val link: Uri = Uri.parse(url)

                if (link.host == WebViewAssetLoader.DEFAULT_DOMAIN) {
                    return false
                }

                startActivity(Intent(Intent.ACTION_VIEW, link))
                return true
            }
        }

        web_view.settings.javaScriptEnabled = true

        val spineItemUrl = Uri
            .Builder()
            .scheme("https")
            .authority(WebViewAssetLoader.DEFAULT_DOMAIN)
            .appendPath(bookDirName)
            .appendEncodedPath(spineItem.relativeFilePath)
            .build()
        web_view.loadUrl(spineItemUrl.toString())
    }

    companion object {
        private const val KEY_SPINE_ITEM = "spine_item"

        fun newInstance(spineItem: SpineItem) =
            SpineItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_SPINE_ITEM, spineItem)
                }
            }
    }
}
