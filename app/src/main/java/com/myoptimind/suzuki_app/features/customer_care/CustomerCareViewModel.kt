package com.myoptimind.suzuki_app.features.customer_care

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myoptimind.suzuki_app.features.customer_care.api.CustomerCareService
import com.myoptimind.suzuki_app.shared.api.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class CustomerCareViewModel @ViewModelInject constructor(
        private val customerCareRepository: CustomerCareRepository
): ViewModel() {

    val submitInquiryResult: LiveData<Result<CustomerCareService.SubmitInquiryResponse>> get() = _submitInquiryResult
    private val _submitInquiryResult = MutableLiveData<Result<CustomerCareService.SubmitInquiryResponse>>()


    init {

    }

   fun submitInquiry(
    subject: String,
    fullname: String,
    landline: String,
    phoneNumber: String,
    emailAddress: String,
    location: String,
    message: String
    ){
       viewModelScope.launch(IO){
           try{
               _submitInquiryResult.postValue(Result.Loading)
               val response = customerCareRepository.submitInquiry(
                       subject,
                       fullname,
                       landline,
                       phoneNumber,
                       emailAddress,
                       location,
                       message
               )
               _submitInquiryResult.postValue(Result.Success(response))
           }catch (exception: Exception){
               _submitInquiryResult.postValue(Result.Error(exception))
           }

       }
   }
}