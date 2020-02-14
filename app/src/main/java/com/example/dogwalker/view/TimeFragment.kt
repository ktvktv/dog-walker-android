package com.example.dogwalker.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

class TimeFragment(val textView: TextView) : TimePickerDialog.OnTimeSetListener, DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hh = calendar.get(Calendar.HOUR_OF_DAY)
        val mm = calendar.get(Calendar.MINUTE)
        return TimePickerDialog(activity!!, this, hh, mm, true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val h = if(hourOfDay < 10) "0$hourOfDay" else "$hourOfDay"
        val m = if(minute < 10) "0$minute" else "$minute"
        textView.text = "$h:$m:00"
    }
}