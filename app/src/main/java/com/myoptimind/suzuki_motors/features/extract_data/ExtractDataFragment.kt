package com.myoptimind.suzuki_motors.features.extract_data

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myoptimind.suzuki_motors.R
import com.myoptimind.suzuki_motors.features.shared.AppSharedPref
import com.myoptimind.suzuki_motors.features.shared.TitleOnlyFragment
import com.myoptimind.suzuki_motors.features.shared.api.Result
import com.myoptimind.suzuki_motors.features.suzuki_diary.service_history.ServiceHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_extract_data.*
import org.joda.time.DateTime
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ExtractDataFragment() : TitleOnlyFragment() {

    override fun getTitle() = "Extract Data"
    private val viewModel: ServiceHistoryViewModel by activityViewModels()
    private var adapter: ExtractDataAdapter? = null

    @Inject
    lateinit var appSharedPref: AppSharedPref

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_extract_data, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getServiceHistoryList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ExtractDataAdapter(ArrayList())
        rv_extract_data.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rv_extract_data.adapter

        initObservers()
        btn_extract_all_data.setOnClickListener {
            val uri: Uri = Uri.parse("https://suzuki.optimindsolutions.com/extract_pdf/${appSharedPref.getUserId()}")
            Timber.v(uri.toString())
            val request = DownloadManager.Request(uri)
            val filename: String = "data-" + DateTime().toString("MMMM-dd-yyyy") + ".pdf"

//                        val filename = motorcycle.name.split(" ").joinToString("-") + "-brochure"
            request.setTitle(filename)
            request.setDescription(filename)
            request.setVisibleInDownloadsUi(true)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)

            val mgr = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            mgr.enqueue(request)
            Toast.makeText(requireContext(),"Downloading...", Toast.LENGTH_LONG).show()
        }


    }


    private fun initObservers() {


        viewModel.serviceHistoryList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    val serviceHistoryList = result.data.data
                    if (serviceHistoryList.isNotEmpty()) {
                        adapter = ExtractDataAdapter(serviceHistoryList, object : ExtractDataAdapter.ExtractDataListener {
                            override fun onMoreInfo(index: Int) {
                                ExtractDataFragmentDirections.actionExtractDataFragmentToSelectedServiceHistoryFragment2(index,getTitle()).also {
                                    findNavController().navigate(it)
                                    viewModel.currentIndex = index
                                }
                            }
                        })
                        rv_extract_data.adapter = adapter
                        adapter?.notifyDataSetChanged()
                    } else {

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
    }
}
