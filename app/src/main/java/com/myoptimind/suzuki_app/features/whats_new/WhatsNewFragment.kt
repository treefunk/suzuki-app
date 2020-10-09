package com.myoptimind.suzuki_app.features.whats_new

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoptimind.suzuki_app.MainActivity
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.shared.api.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_whats_new.*
import kotlinx.android.synthetic.main.fragment_whats_new.tv_filter_value

import timber.log.Timber

@AndroidEntryPoint
class WhatsNewFragment : Fragment() {

    private val viewModel: WhatsNewViewModel by viewModels()
    private var adapter: WhatsNewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // todo edit this
        val m = activity as MainActivity
        m.hideBottomNav(false)
        return inflater.inflate(R.layout.fragment_whats_new,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = WhatsNewAdapter(ArrayList())
        rv_whats_new.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        rv_whats_new.adapter = adapter

        initClickListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.whatsNewResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Result.Success -> {
                    adapter?.articleList = result.data.data.result
                    adapter?.notifyDataSetChanged()
                }
                is Result.Error -> {
                    Timber.e(result.error.message.toString())
                }
                Result.Loading -> {
                    //
                }
            }

        }

        viewModel.filterList.observe(viewLifecycleOwner) { filters ->

            val filtersWithBlank = arrayOf("None") + filters.map{ it.name }
            val adapterList = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    filtersWithBlank
            )

            tv_filter_value.apply {
                setText(filtersWithBlank[0])
                setAdapter(adapterList)
                setOnItemClickListener { _, _, index, _ ->
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