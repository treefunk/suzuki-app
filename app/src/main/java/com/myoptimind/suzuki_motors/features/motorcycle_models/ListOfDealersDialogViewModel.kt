package com.myoptimind.suzuki_motors.features.motorcycle_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myoptimind.suzuki_motors.features.dealer_locator.data.DealerLocatorsListItem
import java.util.ArrayList

class ListOfDealersDialogViewModel(): ViewModel() {

    val listOfDealersResult: LiveData<List<DealerLocatorsListItem>> get() = _listOfDealersResults

    private val _listOfDealers = MutableLiveData<List<DealerLocatorsListItem>>()
    private val _listOfDealersResults = MediatorLiveData<List<DealerLocatorsListItem>>()

        private val searchKeyword = MutableLiveData<String>()
    private val selectedCity = MutableLiveData<String>()
    val filters: LiveData<List<String>> get() = _filters
    private val _filters = MutableLiveData<List<String>>()

    fun setListOfDealers(list: ArrayList<DealerLocatorsListItem>){
        _listOfDealers.value = list
    }

    fun setFilters(list: ArrayList<String>){
        _filters.value = list
    }

    init {

        _listOfDealersResults.addSource(searchKeyword){ _ ->
            _listOfDealersResults.value = getFilteredDealers()
        }
        _listOfDealersResults.addSource(selectedCity){ _ ->
            _listOfDealersResults.value = getFilteredDealers()
        }

        searchKeyword.value = ""
        selectedCity.value = ""
    }

    private fun getFilteredDealers(): List<DealerLocatorsListItem>{
        var c = selectedCity.value
        if(c.equals("All",true)){ c = "" }
        return _listOfDealers.value!!.filter { it.city!!.contains(c!!,true) && it.name!!.contains(searchKeyword!!.value.toString(),true) }
    }

    fun searchListOfDealers(keyword: String){
        searchKeyword.value = keyword
    }

    fun updateCity(city: String){
        selectedCity.value = city
    }
}