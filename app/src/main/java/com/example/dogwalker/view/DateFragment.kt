package com.example.dogwalker.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.util.*

class DateFragment(val textView: TextView) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val yy = calendar.get(Calendar.YEAR)
        val mm = calendar.get(Calendar.MONTH)
        val dd = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(activity!!, this, yy, mm, dd)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        val m = if(month < 10) "0${month+1}" else "${month+1}"
        textView.text = "$day-$m-$year"
    }

}