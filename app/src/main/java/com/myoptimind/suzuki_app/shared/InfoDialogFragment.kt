package com.myoptimind.suzuki_app.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myoptimind.suzuki_app.R
import kotlinx.android.synthetic.main.dialog_info_message.*

private const val ARGS_TITLE = "args_title"
private const val ARGS_BODY  = "args_body"

class InfoDialogFragment : BaseDialogFragment() {


    companion object {
        fun newInstance(title: String, body: String): InfoDialogFragment {
            val args = Bundle()

            val fragment = InfoDialogFragment()
            args.apply {
                putString(ARGS_TITLE,title)
                putString(ARGS_BODY,body)
            }
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_info_message,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString(ARGS_TITLE,"")
        val body  = arguments?.getString(ARGS_BODY,"")
        tv_info_title.text   = title
        tv_info_message.text = body

        ib_close.setOnClickListener{
            this.dismiss()
        }
    }

    data class InfoDialogContents(
            val title: String,
            val body: String
    )
}