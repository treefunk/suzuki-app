package com.myoptimind.suzuki_app.features.sidenav.changepass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.myoptimind.suzuki_app.R
import com.myoptimind.suzuki_app.features.shared.AppSharedPref
import com.myoptimind.suzuki_app.features.shared.BaseDialogFragment
import com.myoptimind.suzuki_app.features.shared.api.Result
import com.myoptimind.suzuki_app.features.shared.izNotBlank
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_change_password.*
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordDialogFragment(): BaseDialogFragment(){

    private val viewModel: ChangePasswordViewModel by viewModels()

    @Inject
    lateinit var appSharedPref: AppSharedPref

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.dialog_change_password,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ib_close.setOnClickListener {
            dismiss()
        }

        viewModel.changePasswordResult.observe(viewLifecycleOwner){ result ->
            when(result){
                Result.Loading -> {

                }
                is Result.Success -> {
                    val meta = result.data.meta
                    Toast.makeText(requireContext(),meta.message,Toast.LENGTH_LONG).show()
                    if(meta.status == "200"){
                        et_old_password.text.clear()
                        et_new_password.text.clear()
                        et_confirm_new_password.text.clear()
                        dismiss()
                    }
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(),result.error.message,Toast.LENGTH_LONG).show()
                }
            }
        }
        btn_submit.setOnClickListener {
            if(et_old_password.izNotBlank() && et_new_password.izNotBlank() && et_confirm_new_password.izNotBlank()){
                if(et_new_password.text.toString() != et_confirm_new_password.text.toString()){
                    Toast.makeText(requireContext(),"Password does not match.",Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.changePassword(appSharedPref.getUserId(),et_old_password.text.toString(),et_new_password.text.toString(),et_confirm_new_password.text.toString())
                }
            }else{
                Toast.makeText(requireContext(),"Please fill in required fields.",Toast.LENGTH_SHORT).show()
            }
        }
    }
}