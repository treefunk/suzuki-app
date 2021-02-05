package com.myoptimind.suzuki_app.features.whats_new

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoptimind.suzuki_app.MainActivity
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.shared.TitleOnlyFragment
import com.myoptimind.suzuki_app.features.shared.api.Result
import com.myoptimind.suzuki_app.features.shared.setOnScrollEnd
import com.myoptimind.suzuki_app.features.spare_parts.SparePartsViewModel
import com.myoptimind.suzuki_app.features.whats_new.data.Article
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_whats_new.*
import kotlinx.android.synthetic.main.fragment_whats_new.tv_filter_value

import timber.log.Timber

@AndroidEntryPoint
class WhatsNewFragment : TitleOnlyFragment() {

    override fun getTitle() = "What's New"

    private lateinit var viewModel: WhatsNewViewModel
    private var adapter: WhatsNewAdapter? = null
    private val whatsNewList = ArrayList<Article>()
    private var total: Int? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // todo edit this
        val m = activity as MainActivity
        m.hideBottomNav(false)
        return inflater.inflate(R.layout.fragment_whats_new,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.resetResult()
        viewModel.offset = 0
        viewModel.getInitialWhatsNew()



        adapter = WhatsNewAdapter(whatsNewList,object: WhatsNewAdapter.WhatsNewListener{
            override fun onClickWhatsNew(index: Int) {
                WhatsNewFragmentDirections.actionWhatsNewFragmentToSelectedWhatsNewFragment(index).also {
                    findNavController().navigate(it)
                }
            }
        })

        rv_whats_new.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        rv_whats_new.adapter = adapter
        rv_whats_new.setOnScrollEnd {
            if(total != null && total!! > 0){
                viewModel.increaseRowCount()
            }
        }


        initClickListeners()
        initObservers()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(WhatsNewViewModel::class.java)
    }

    private fun initObservers() {
        viewModel.whatsNewResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
//                    adapter?.articleList = result.data.data.result
                    whatsNewList.clear()
                    whatsNewList.addAll(result.data.data.result)

                    total = result.data.data.result.size
                    adapter?.notifyDataSetChanged()
                    hideLoading()
                    rv_whats_new.visibility = View.VISIBLE
//                    viewModel.resetResult()
                }
                is Result.Error -> {
                    Timber.e(result.error.message.toString())
//                    viewModel.resetResult()
                    hideLoading()
                }
                Result.Loading -> {
                    showLoading()
                    rv_whats_new.visibility = View.GONE
                }
            }

        }

        viewModel.filterList.observe(viewLifecycleOwner) { filters ->

            val filtersWithBlank = arrayOf("All") + filters.map{ it.name }
            val adapterList = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    filtersWithBlank
            )

            tv_filter_value.apply {
                setText(filtersWithBlank[0])
                setAdapter(adapterList)
                setOnItemClickListener { _, _, index, _ ->
                    whatsNewList.clear()
                    val i = if((index - 1) < 0) null else index - 1
                    val selectedFilter = if(i == null) filtersWithBlank[0] else filters[i].id
                    viewModel.updateCategory(selectedFilter)
                }
            }

        }
    }

    private fun initClickListeners() {
        tv_filter_value.setOnClickListener {
            tv_filter_value.showDropDown()
        }
    }

}