package com.myoptimind.suzuki_app.features.motorcycle_models

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.shared.BaseDialogFragment
import kotlinx.android.synthetic.main.dialog_web_view.*
import timber.log.Timber

private const val ARGS_KEY_URL = "args_key_url"
private const val ARGS_IS_IMAGE = "args_is_image"
private const val ARGS_YOUTUBE_URL = "args_youtube_url"
class DialogWebViewFragment(): BaseDialogFragment() {


    companion object {
        fun newInstance(url: String, isImage: Boolean = false, youtubeUrl: String = ""): DialogWebViewFragment {
            val args = Bundle()
            args.putString(ARGS_KEY_URL,url)
            args.putBoolean(ARGS_IS_IMAGE, isImage)
            args.putString(ARGS_YOUTUBE_URL, youtubeUrl)
            val fragment = DialogWebViewFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_web_view,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.getString(ARGS_KEY_URL)
        val isImage = arguments?.getBoolean(ARGS_IS_IMAGE) as Boolean
        val youtubeUrl = arguments?.getString(ARGS_YOUTUBE_URL, "")!!
        Timber.v("loading url $url")
        if(isImage){ // if image load url to image view
            wv_360.visibility = View.GONE
            wv_image.visibility = View.VISIBLE
            Glide.with(this)
                    .load(url)
                    .fitCenter()
                    .into(wv_image)

        }else{ // if not an image load url as web view
            if(youtubeUrl.isNotBlank()){
                btn_watch_more_videos.setOnClickListener {
                    openWebPage(youtubeUrl)
                }
                btn_watch_more_videos.visibility = View.VISIBLE
            }else{
                btn_watch_more_videos.visibility = View.GONE
            }
            wv_360.visibility = View.VISIBLE
            wv_image.visibility = View.GONE
            wv_360.webViewClient = WebViewClient()
            wv_360.settings.apply {
                javaScriptEnabled = true
            }
            wv_360.loadUrl(url)
        }

        ib_close.setOnClickListener { this.dismiss() }
    }

    private fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }
}