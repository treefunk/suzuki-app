package com.myoptimind.suzuki_app.features.whats_new

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.shared.TitleOnlyFragment
import kotlinx.android.synthetic.main.fragment_selected_safety_tip.*
import kotlinx.android.synthetic.main.fragment_selected_whats_new.*
import kotlinx.android.synthetic.main.fragment_selected_whats_new.iv_main_image
import kotlinx.android.synthetic.main.fragment_selected_whats_new.tv_body
import kotlinx.android.synthetic.main.fragment_selected_whats_new.tv_caption
import kotlinx.android.synthetic.main.fragment_selected_whats_new.tv_title

class SelectedWhatsNewFragment: TitleOnlyFragment(){

    private val args: SelectedWhatsNewFragmentArgs by navArgs()
    private val viewModel: WhatsNewViewModel by activityViewModels()

    override fun getTitle() = "What's New"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_selected_whats_new,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = viewModel.getSingleArticle(args.index)
        if(article != null){
            tv_title.text = article.heading
//            tv_body.text = article.content
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tv_body.text = Html.fromHtml(article.content, HtmlCompat.FROM_HTML_MODE_LEGACY)
            } else {
                tv_body.text = Html.fromHtml(article.content)
            }
            if(article.category.equals("promo",true)){
                tv_caption.text = article.subHeading
            }else{
                tv_caption.text = article.formattedCreatedAt
            }
            tv_type.text = article.category
            Glide.with(this)
                    .load(article.banner)
                    .centerCrop()
                    .into(iv_main_image)
        }
    }
}