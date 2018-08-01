package com.example.sandy.koltin_telephony

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var status=ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)

        if (status== PackageManager.PERMISSION_GRANTED){

            smsservice()
            phonecall()
            mailservices()
        }else{

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS),11)

        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0]==PackageManager.PERMISSION_GRANTED){

            smsservice()
            phonecall()
            mailservices()
        }else{

            Toast.makeText(this,"You can't send SMS without permission",Toast.LENGTH_LONG).show()

        }

    }

    fun smsservice(){

        sms.setOnClickListener {

            var numbers =  et1.text.toString().split(",")
            for( number in numbers) {
                var sIntent = Intent(this@MainActivity,
                        SentActivity::class.java)
                var dIntent = Intent(this@MainActivity,
                        DeliverActivity::class.java)
                var spIntent = PendingIntent.getActivity(this@MainActivity,
                        0, sIntent, 0)
                var dpIntent = PendingIntent.getActivity(this@MainActivity,
                        0, dIntent, 0)

                var sManager = SmsManager.getDefault()
                sManager.sendTextMessage(et1.text.toString(), null,
                        et2.text.toString(), spIntent, dpIntent)



            }
        }


    }

    fun phonecall(){

        call.setOnClickListener({
            var i = Intent( )
            i.action = Intent.ACTION_CALL
            i.data = Uri.parse("tel:${et1.text.toString()}")
            startActivity(i)

        })

    }

    fun mailservices(){

        attach.setOnClickListener({

            var adialog=AlertDialog.Builder(this@MainActivity)
            adialog.setIcon(R.drawable.ic_exclamation_alert_sign_on_reminder_daily_calendar_page)
            adialog.setTitle("Message")
            adialog.setMessage("Please select an option")
            adialog.setNegativeButton("Camera",object:DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    var i = Intent("android.media.action.IMAGE_CAPTURE")
                    startActivityForResult(i,123)
                }


            })

            adialog.setPositiveButton("Files",object:DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    var i=Intent()
                    i.action=Intent.ACTION_GET_CONTENT
                    i.type="*/*"
                    startActivityForResult(i,124)
                }

            })
            adialog.show()

        })

    }
}
