package com.myoptimind.suzuki_app.features.motorcycle_models

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myoptimind.suzuki_app.MainActivity
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.customer_care.CustomerCareFragment
import com.myoptimind.suzuki_app.features.motorcycle_models.data.ColorVariant
import com.myoptimind.suzuki_app.features.motorcycle_models.data.Feature
import com.myoptimind.suzuki_app.features.shared.InfoDialogFragment
import com.myoptimind.suzuki_app.features.shared.TitleOnlyFragment
import com.myoptimind.suzuki_app.features.shared.api.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_selected_motorcycle.*





@AndroidEntryPoint
class SelectedMotorcycleFragment: TitleOnlyFragment(){

    val args: SelectedMotorcycleFragmentArgs by navArgs()
    val viewModel: SelectedMotorcycleViewModel by activityViewModels()

    private var motorcycleSpecsAdapter: SelectedMotorcycleSpecsAdapter? = null
    private var motorcycleFeaturesAdapter: SelectedMotorcycleFeaturesAdapter? = null
    private var motorcycleColorVariantsAdapter: SelectedMotorcycleColorVariantsAdapter? = null
    var index = 0

    var parentLayout: ConstraintLayout? = null


    override fun getTitle() = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initMotorcycleModel(args.id)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val v = inflater.inflate(R.layout.fragment_selected_motorcycle, container, false)
        parentLayout = v.findViewById(R.id.parent_selected_motorcycle_view) as ConstraintLayout
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        motorcycleColorVariantsAdapter = SelectedMotorcycleColorVariantsAdapter(ArrayList())
        rv_color_variant.layoutManager = linearLayoutManagerFourItems(0)
        rv_color_variant.adapter = motorcycleColorVariantsAdapter
//        rv_color_variant.initArrows(ib_color_variant_left_arrow,ib_color_variant_right_arrow)




        motorcycleFeaturesAdapter = SelectedMotorcycleFeaturesAdapter(ArrayList())
        rv_motorcycle_features.layoutManager = linearLayoutManagerFourItems(6)
        rv_motorcycle_features.adapter = motorcycleFeaturesAdapter
        rv_motorcycle_features.initArrows(ib_features_left_arrow, ib_features_right_arrow)

        motorcycleSpecsAdapter = SelectedMotorcycleSpecsAdapter(ArrayList())
        rv_motorcycle_specs.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rv_motorcycle_specs.adapter = motorcycleSpecsAdapter





        initSpecListeners()



        viewModel.motorcycleModelResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    val motorcycle = result.data.data
                    setNewTitle(motorcycle.category)
                    Glide.with(this)
                            .load(motorcycle.logo)
                            .fitCenter()
                            .into(iv_motorcycle_logo)

                    setMainImage(motorcycle.thumbnail)

                    tv_motorcycle_caption.text = motorcycle.tagline
                    tv_motorcycle_price.text = motorcycle.price
//                    tv_motorcycle_name.text = motorcycle.name


                    viewModel.activeSpecTab.observe(viewLifecycleOwner) { specTab ->
                        val activeSpecAdapter = when (specTab) {
                            SpecTab.ENGINE_SPEC -> SelectedMotorcycleSpecsAdapter(motorcycle.engineSpecs)
                            SpecTab.CHASSIS_SPEC -> SelectedMotorcycleSpecsAdapter(motorcycle.chassisSpecs)
                            SpecTab.DIMENSIONS_SPEC -> SelectedMotorcycleSpecsAdapter(motorcycle.dimensions)
                        }
                        rv_motorcycle_specs.adapter = activeSpecAdapter
                        motorcycleSpecsAdapter?.notifyDataSetChanged()
                    }

                    motorcycleFeaturesAdapter = SelectedMotorcycleFeaturesAdapter(motorcycle.features, object : SelectedMotorcycleFeaturesAdapter.FeatureListener {
                        override fun onClickItem(feature: Feature) {
                            DialogWebViewFragment.newInstance(
                                    feature.image, true
                            ).show(parentFragmentManager, "feature_dialog")
                        }
                    })
                    rv_motorcycle_features.adapter = motorcycleFeaturesAdapter
                    motorcycleFeaturesAdapter?.notifyDataSetChanged()



                    motorcycleColorVariantsAdapter = SelectedMotorcycleColorVariantsAdapter(
                            motorcycle.colorVariants
/*                            ,object : ColorVariantListener {
                                override fun onPressed(colorvariant: ColorVariant) {
                                    setMainImage(colorvariant.image)
                                }
                            }*/
                    )

/*
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(parentLayout)
                    constraintSet.constrainDefaultWidth(R.id.rv_color_variant,ConstraintSet.MATCH_CONSTRAINT)
                    constraintSet.applyTo(parentLayout)
*/

                    val size = motorcycle.colorVariants.size

                    val linearLayout = object : LinearLayoutManager(requireContext()) {
                        override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                            if (size > 4) {
                                lp?.width = (width / motorcycle.colorVariants.size)
                            } else {
                                lp?.width = 130
                            }
                            return true
                        }
                    }.also {
                        it.orientation = RecyclerView.HORIZONTAL
                    }


                    rv_color_variant.layoutManager = linearLayout
                    rv_color_variant.adapter = motorcycleColorVariantsAdapter
                    motorcycleColorVariantsAdapter?.notifyDataSetChanged()

                    if (motorcycle.colorVariants.isNotEmpty()) {
                        viewModel.setActiveColorVariantIndex(index)
                    }
                    ib_color_variant_left_arrow.setOnClickListener {
                        if (index != 0) {
                            viewModel.setActiveColorVariantIndex(index - 1)
                            index--
                        }
                    }
                    ib_color_variant_right_arrow.setOnClickListener {
                        if (index < motorcycle.colorVariants.size - 1) {
                            viewModel.setActiveColorVariantIndex(index + 1)
                            index++
                        }
                    }

                    ib_view_360.setOnClickListener {
                        if(motorcycle.url360.isBlank()){
                            InfoDialogFragment.newInstance("Notice!","No 360 View available for this motorcycle").show(parentFragmentManager,"product_video_dialog")
                        }else{
                            val dialog360 = DialogWebViewFragment.newInstance(motorcycle.url360)
                            dialog360.show(parentFragmentManager, "360")
                        }
                    }


                    tv_link_watch.setOnClickListener {
                        if(motorcycle.productVideoUrl.isBlank()){
                            InfoDialogFragment.newInstance("Notice!","No Product video available for this motorcycle").show(parentFragmentManager,"product_video_dialog")
                        }else{
                            val dialogVideo = DialogWebViewFragment.newInstance(motorcycle.productVideoUrl, youtubeUrl = MainActivity.URL_YOUTUBE)
                            dialogVideo.show(parentFragmentManager, "video")
                        }
                    }

                    tv_link_nearest_dealer.setOnClickListener {
                        if(motorcycle.listOfDealers.isEmpty()){
                            InfoDialogFragment.newInstance("Notice!","No dealers available for this motorcycle").show(parentFragmentManager,"list_of_dealer_dialog")
                        }else{
                            ListOfDealersDialogFragment.newInstance(
                                    motorcycle.listOfDealers,
                                    motorcycle.listOfDealers.map { it.city }.distinct() as List<String>
                            ).show(childFragmentManager, "List_of_dealers")
                        }
                    }

                    if (motorcycle.brochure.isNotBlank()) {
                        btn_download_brochure.visibility = View.VISIBLE
                        btn_download_brochure.setOnClickListener {
                            val uri: Uri = Uri.parse(motorcycle.brochure)
                            val request = DownloadManager.Request(uri)
                            val filename: String = motorcycle.brochure.substring(motorcycle.brochure.lastIndexOf('/') + 1)
//                        val filename = motorcycle.name.split(" ").joinToString("-") + "-brochure"
                            request.setTitle(filename)
                            request.setDescription(filename)
                            request.setVisibleInDownloadsUi(true)
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)

                            val mgr = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                            mgr.enqueue(request)
                            Toast.makeText(requireContext(), "Downloading brochure...", Toast.LENGTH_LONG).show()
                        }
                    }else{
                        btn_download_brochure.visibility = View.GONE
                    }

                    btn_book_test_drive.setOnClickListener {
                        SelectedMotorcycleFragmentDirections.actionSelectedMotorcycleFragmentToCustomerCareFragment(CustomerCareFragment.InquiryType.TEST_RIDE.id).also {
                            findNavController().navigate(it)
                        }
                    }

                    hideLoading()
                }
                is Result.Error -> {
                    hideLoading()
                }
                Result.Loading -> {
                    showLoading()
                }
            }
        }

        viewModel.activeColorVariant.observe(viewLifecycleOwner){ colorVariant ->
            setMainImage(colorVariant.image)
            tv_motorcycle_name.setText(colorVariant.variantName)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setMainImage(image: String){
        Glide.with(this)
                .load(image)
                .fitCenter()
                .into(iv_motorcycle_image)
    }

    private fun initSpecListeners() {
        label_engine_specs.setOnClickListener { changeSpecTab(SpecTab.ENGINE_SPEC) }
        label_chassis_specs.setOnClickListener { changeSpecTab(SpecTab.CHASSIS_SPEC) }
        label_dimensions.setOnClickListener { changeSpecTab(SpecTab.DIMENSIONS_SPEC) }
    }

    private fun linearLayoutManagerFourItems(margin: Int = 0): LinearLayoutManager {
        return object : LinearLayoutManager(requireContext()){
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                lp?.width = (width / 4) - (margin * 2)
                lp?.marginEnd = margin
                lp?.marginStart = margin
                return true
            }
        }.also {
            it.orientation = RecyclerView.HORIZONTAL
        }
    }

    private fun changeSpecTab(specTab: SpecTab){

        viewModel.setActiveSpecTab(specTab)

        label_engine_specs.disableSpecState()
        label_chassis_specs.disableSpecState()
        label_dimensions.disableSpecState()

        val activeTextView = view?.findViewById<TextView>(specTab.labelId)

        if(activeTextView != null){
            val drawableTransparent = requireContext().getDrawable(android.R.color.transparent)
            val whiteTextColor = ContextCompat.getColor(requireContext(), R.color.color_blue_1000)

            activeTextView.background = drawableTransparent
            activeTextView.setTextColor(whiteTextColor)
        }

    }

    private fun TextView.disableSpecState(){
        val drawableWhite = ContextCompat.getDrawable(requireContext(), android.R.color.white)
        val greyTextColor = ContextCompat.getColor(requireContext(), R.color.color_grey_disabled)

        this.background = drawableWhite
        this.setTextColor(greyTextColor)
    }

    private fun RecyclerView.initArrows(leftArrow: ImageButton, rightArrow: ImageButton){
        val lm = this.layoutManager as LinearLayoutManager
        leftArrow.setOnClickListener {
            if(lm.findLastVisibleItemPosition() - 1 >= 0){
                scrollToPosition(lm.findLastVisibleItemPosition() - 4)
            }
        }
        rightArrow.setOnClickListener {
            scrollToPosition(lm.findFirstVisibleItemPosition() + 4)
        }
    }


}



enum class SpecTab(
        val labelId: Int
){
    ENGINE_SPEC(R.id.label_engine_specs),
    CHASSIS_SPEC(R.id.label_chassis_specs),
    DIMENSIONS_SPEC(R.id.label_dimensions)
}

