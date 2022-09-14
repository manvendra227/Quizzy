package com.example.quizzy

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    var day = 0
    var mon = 0
    var year = 0
    var mDay = 0
    var mMon = 0
    var mYear = 0


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        datePicker()
    }

    private fun pickDate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            mon = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
        } else {
            Toast.makeText(this, "HELLO", Toast.LENGTH_LONG).show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun datePicker() {
        date_picker.setOnClickListener {
            pickDate()
            val dp=DatePickerDialog(this,this, year, mon, day)
            dp.show()
        }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        mDay = p3
        mMon = p2
        mYear = p1
        date.text = "$p3/$p2/$p1"
        date_box.setBackgroundColor(ContextCompat.getColor(this, R.color.edittext_fill));
    }
}