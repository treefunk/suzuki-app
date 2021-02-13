package com.myoptimind.suzuki_motors.features.shared

import android.util.Patterns
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.json.JSONObject
import retrofit2.HttpException


fun EditText.initDatePicker(
        fragmentManager: FragmentManager,
        targetFragment: Fragment,
        dialogTag: String,
        requestCode: Int,
        maxDate: Long? = DateTime().millis,
        minDate: Long? = null
) {
    this.setOnClickListener {
        val dt = if (this.text.toString().isEmpty()) {
            DateTime()
        } else {
            DateTime.parse(this.text.toString(), DateTimeFormat.forPattern("MMMM d, yyyy"))
        }

       DatePickerDialogFragment.newInstance(
               dt.year,
               dt.monthOfYear - 1,
               dt.dayOfMonth,
               maxDate,
               minDate
       ).also {
           it.setTargetFragment(targetFragment, requestCode)
           it.show(fragmentManager, dialogTag)
       }


    }
}

fun HttpException.getMessage(): String {
    val errorJsonString = this.response()?.errorBody()?.string()
    return JSONObject(errorJsonString!!).getJSONObject("meta").getString("message")
}

fun EditText.izNotBlank(): Boolean {
    return this.text.toString().isNotBlank()
}

fun EditText.toDateTime(): DateTime {
    return DateTime.parse(this.text.toString(), DateTimeFormat.forPattern("MMMM d, yyyy"))
}

fun String.toDateTime(): DateTime {
    return DateTime.parse(this, DateTimeFormat.forPattern("MMMM d, yyyy"))
}

fun DateTime.toReadableDateTime(): String {
    val pattern = "MMMM dd, yyyy"
    return DateTimeFormat.forPattern(pattern).print(this)
}

fun String.validateEmail(): Boolean {
//    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
    val emailPattern = Patterns.EMAIL_ADDRESS.toRegex()
    return this.matches(emailPattern)

}

fun RecyclerView.setOnScrollEnd(onScrollEnd: () -> Unit){
    this.addOnScrollListener(object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE){
                onScrollEnd()
            }
        }
    })
}

/*
rv_motorcycle_models.addOnScrollListener(object: RecyclerView.OnScrollListener(){
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if(!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE){
            if(total != null && total!! > 0){
                viewModel.increaseRowCount()
            }
        }
    }
})*/
