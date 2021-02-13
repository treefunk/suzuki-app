package com.myoptimind.suzuki_motors.features.safety_tips

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.shared.TitleOnlyFragment
import kotlinx.android.synthetic.main.fragment_selected_safety_tip.*

class SelectedSafetyTipsFragment: TitleOnlyFragment() {

    override fun getTitle() = "Safety Tips"
    private val args: SelectedSafetyTipsFragmentArgs by navArgs()
    private val viewModel: SafetyTipsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_selected_safety_tip,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val safetyTip = viewModel.getSingleSafetyTip(args.index)
        if(safetyTip != null){
            tv_title.text = safetyTip.heading
//            tv_body.text = safetyTip.content
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tv_body.text = Html.fromHtml(safetyTip.content,FROM_HTML_MODE_LEGACY)
            } else {
                tv_body.text = Html.fromHtml(safetyTip.content)
            }
            tv_caption.text = safetyTip.formattedCreatedAt

            if(safetyTip.videoUrl.isNullOrBlank()){ // if image load url to image view
                wv_video.visibility = View.INVISIBLE
                iv_main_image.visibility = View.VISIBLE
                Glide.with(this)
                        .load(safetyTip.banner)
                        .fitCenter()
                        .into(iv_main_image)
            }else{ // if not an image load url as web view
                wv_video.visibility = View.VISIBLE
                iv_main_image.visibility = View.INVISIBLE
                wv_video.webViewClient = WebViewClient()
                wv_video.settings.apply {
                    javaScriptEnabled = true
                }
                wv_video.loadUrl(safetyTip.videoUrl)
            }

        }
    }


}