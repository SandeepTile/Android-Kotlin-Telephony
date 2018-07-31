package com.example.sandy.koltin_telephony

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sms.setOnClickListener({

            var numbers=et1.text.toString().split(",")

            var sManager=SmsManager.getDefault()
           // sManager.sendTextMessage(numbers,null)

        })
    }
}
