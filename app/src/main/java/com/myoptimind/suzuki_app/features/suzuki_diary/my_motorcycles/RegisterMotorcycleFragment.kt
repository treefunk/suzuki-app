package com.myoptimind.suzuki_app.features.suzuki_diary.my_motorcycles

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.login.LoginFeaturedMotorcycleAdapter
import com.myoptimind.suzuki_app.features.login.data.LoginFeaturedMotorcycle
import com.myoptimind.suzuki_app.features.shared.*
import com.myoptimind.suzuki_app.features.shared.api.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register_motorcycle.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import timber.log.Timber
import javax.inject.Inject


private const val REQUEST_CODE_DATE_PICKER = 100




@AndroidEntryPoint
class RegisterMotorcycleFragment(): TitleOnlyFragment(){
    override fun getTitle() = "My Suzuki Diary"

    private val viewModel: MyMotorcyclesViewModel by activityViewModels()
    private val args: RegisterMotorcycleFragmentArgs by navArgs()

    @Inject
    lateinit var appSharedPref: AppSharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initRegisterMotorcycleModels()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_register_motorcycle,container,false)
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initClickListeners()

        if(args.index != -1){
            viewModel.initRegisteredMotorcycleId(args.index)
        }else{
            viewModel.clearExistingMotorcycle()
        }

    }

    private fun initClickListeners() {
        et_date_purchased.initDatePicker(
                parentFragmentManager,
                this@RegisterMotorcycleFragment,
                "date_purchased",
                REQUEST_CODE_DATE_PICKER
        )
        btn_register.setOnClickListener {
            if(validateFields()){
                viewModel.registerMyMotorcycle(
                        et_beast_name.text.toString(),
                        appSharedPref.getUserId(),
                        ac_select_motorcycle.tag.toString(),
                        et_engine_number.text.toString(),
                        et_frame_number.text.toString(),
                        et_date_purchased.text.toString(),
                        et_purchased_in.text.toString(),
                        true
                )
            }else{
                showRequiredToast()
            }

        }
        btn_add_another.setOnClickListener {
            if(validateFields()){
                viewModel.registerMyMotorcycle(
                        et_beast_name.text.toString(),
                        appSharedPref.getUserId(),
                        ac_select_motorcycle.tag.toString(),
                        et_engine_number.text.toString(),
                        et_frame_number.text.toString(),
                        et_date_purchased.text.toString(),
                        et_purchased_in.text.toString(),
                        false
                )
            }else{
                showRequiredToast()
            }

        }
    }

    private fun showRequiredToast() {
        Toast.makeText(requireContext(),"Please fill in required fields.", Toast.LENGTH_LONG).show()
    }
    private fun validateFields(): Boolean {
        return et_beast_name.izNotBlank() &&
                ac_select_motorcycle.izNotBlank() &&
                et_engine_number.izNotBlank() &&
                et_frame_number.izNotBlank() &&
                et_date_purchased.izNotBlank() &&
                et_purchased_in.izNotBlank()
    }

    private fun clearFields() {
        et_beast_name.text.clear()
        ac_select_motorcycle.tag = ""
        ac_select_motorcycle.text.clear()
        et_engine_number.text.clear()
        et_frame_number.text.clear()
        et_date_purchased.text.clear()
        et_purchased_in.text.clear()
    }

    private fun initObservers() {

        //re-using from login
        val adapter = LoginFeaturedMotorcycleAdapter(ArrayList())
        vp_featured_motorcycle.adapter = adapter
        vp_featured_motorcycle.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewModel.featuredMotorcyclesResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    adapter.loginFeaturedMotorcycles = result.data.data
                    vp_featured_motorcycle.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            val activeItem = adapter.loginFeaturedMotorcycles[position]
                            initActiveFeaturedMotorcycle(activeItem)
                        }
                    })
                    adapter.notifyDataSetChanged()
                    hideLoading()
                }
                is Result.Error -> hideLoading()
                Result.Loading -> showLoading()
            }
        }

        TabLayoutMediator(tablayout_suzuki_diary_featured,vp_featured_motorcycle){ _ , _ ->
            //empty
        }.attach()

        viewModel.motorcycleModelsResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    val adapter: ArrayAdapter<String> = ArrayAdapter(
                            requireContext(),android.R.layout.simple_dropdown_item_1line,
                            result.data.data.result.map{ it.name }
                    )
                    ac_select_motorcycle.setAdapter(adapter)
                    ac_select_motorcycle.apply {
                        setOnClickListener { showDropDown() }
                        setOnItemClickListener { adapterView, view, i, l ->
                            this.setText(adapter.getItem(i),false)
                            et_frame_number.setText(result.data.data.result[i].frameNumber)
                            et_engine_number.setText(result.data.data.result[i].engineNumber)
                            ac_select_motorcycle.tag = result.data.data.result[i].id
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

        viewModel.redirectToList.observe(viewLifecycleOwner){ redirect ->
            if(redirect){
                Timber.v("redirecting..")
                findNavController().navigate(R.id.action_registerMotorcycleFragment_to_myMotorcyclesListFragment)
                viewModel.clearRedirect()
            }else{
                clearFields()
            }
        }

        viewModel.registerMotorcycleResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    val meta = result.data.meta
/*                    InfoDialogFragment.newInstance(
                            meta.message,
                            ""
                    ).show(parentFragmentManager,"success_registration")*/
                    Toast.makeText(requireContext(),meta.message,Toast.LENGTH_LONG).show()
                    viewModel.clearResults()
                    et_purchased_in.clearFocus()
                    et_engine_number.clearFocus()
                    et_frame_number.clearFocus()
                    et_beast_name.clearFocus()
                    hideLoading()
                }

                is Result.Error -> {
                    Timber.v("error - ${result.error.message}")
                    viewModel.clearResults()
                    hideLoading()
                }
                Result.Loading -> {
                    showLoading()
                }
            }
        }

        viewModel.existingRegisteredMotorcycle.observe(viewLifecycleOwner){ registeredMotorcycle ->
            if(registeredMotorcycle != null){
                et_beast_name.setText(registeredMotorcycle.beastNickname)
                ac_select_motorcycle.setText(registeredMotorcycle.motorcycleModelName,false)
                ac_select_motorcycle.tag = registeredMotorcycle.motorcycleModelId
                et_engine_number.setText(registeredMotorcycle.engineNumber)
                et_frame_number.setText(registeredMotorcycle.frameNumber)
                et_date_purchased.setText(registeredMotorcycle.datePurchased)
                et_purchased_in.setText(registeredMotorcycle.purchasedIn)

                btn_register.text = "Update"
                btn_add_another.visibility = View.GONE
            }else{
                btn_register.text = "Register"
                btn_add_another.visibility = View.VISIBLE
            }

        }
    }

    private fun initActiveFeaturedMotorcycle(activeItem: LoginFeaturedMotorcycle) {
        Glide.with(this)
                .load(activeItem.background)
                .centerCrop()
                .into(iv_header_background_image)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_DATE_PICKER && resultCode == Activity.RESULT_OK){
            val dateString = data?.getStringExtra(DatePickerDialogFragment.EXTRA_DATE)
//            val dateTimeFormatter = DateTimeFormat.forPattern(DATE_FORMAT_DATE_PURCHASED)
            et_date_purchased.setText(dateString)
        }
    }


}