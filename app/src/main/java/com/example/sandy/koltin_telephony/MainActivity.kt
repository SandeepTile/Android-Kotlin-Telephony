package com.example.sandy.koltin_telephony

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    var uri:Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sendsms=ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)
        var callphone=ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)
        var readwrite=ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
        var internet=ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET)

        if (sendsms== PackageManager.PERMISSION_GRANTED&&callphone== PackageManager.PERMISSION_GRANTED&&
                readwrite== PackageManager.PERMISSION_GRANTED&&internet== PackageManager.PERMISSION_GRANTED){

            smsservice()
            phonecall()
            mailservices()
        }else{

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS,Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET),11)

        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0]==PackageManager.PERMISSION_GRANTED&&
                grantResults[1]==PackageManager.PERMISSION_GRANTED &&
                grantResults[2]== PackageManager.PERMISSION_GRANTED &&
                grantResults[3]== PackageManager.PERMISSION_GRANTED){

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

        attach.setOnClickListener {

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

        }

        smail.setOnClickListener {

            var i = Intent( )
            i.action = Intent.ACTION_SEND
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf(mailto.text.toString()))
            i.putExtra(Intent.EXTRA_SUBJECT, mailSubject.text.toString())
            i.putExtra(Intent.EXTRA_TEXT,mailtext.text.toString())
            i.putExtra(Intent.EXTRA_STREAM,uri)
            i.type = "message/rfc822"
            startActivity(i)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==123&&requestCode==Activity.RESULT_OK){

            var bmp = data!!.extras.get("data") as Bitmap
            uri = getImageUri(this@MainActivity, bmp)

        }else if (requestCode==124&&requestCode==Activity.RESULT_OK){


            uri = data!!.data

        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }
}
