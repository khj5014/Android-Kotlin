package com.example.capstone4_1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class EndActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end)
        val shareBtn: Button = findViewById(R.id.shareBtn)
        val restartBtn: Button = findViewById(R.id.restartBtn)
        findViewById<ImageView>(R.id.endCharacterIcon).setImageResource(Character.icon)
        findViewById<TextView>(R.id.endSVday).setText(Character.survivalDays()?.plus(1).toString() + " 일 생존")

        //공유버튼
        shareBtn.setOnClickListener(View.OnClickListener {
            capture()
        })

        //리셋버튼
        restartBtn.setOnClickListener(View.OnClickListener {
            deletefile()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
            System.exit(0)

        })

        val name = findViewById<TextView>(R.id.name)
        val gender = findViewById<TextView>(R.id.endgender)
        val interest = findViewById<TextView>(R.id.interest)

        val nameValue = Character.name
        val genderValue = Character.gender.value
        val interestValue = Character.interest.value

        name.append(nameValue)
        gender.append(genderValue)
        interest.append(interestValue)

    }
    fun deletefile(){ //must be tested
        val filepath = filesDir.toString() +"/" + Character.filename
        Log.d("deletefile fun", "filepath: ${filepath}")
        Log.d( "file exist ", "${File(filepath).exists()}")
        var file = File(filepath)
        file.delete()
        Log.d( "file exist ", "${File(filepath).exists()}")
    }

    fun capture(): String? {
        val now = SimpleDateFormat("yyyyMMdd_hhmmss").format(Date(System.currentTimeMillis()))
        val mPath = cacheDir.absolutePath+"/$now.png"

        var bitmap: Bitmap? = null
        val captureView = window.decorView.rootView	//캡처할 뷰

        bitmap = Bitmap.createBitmap(captureView.width, captureView.height, Bitmap.Config.ARGB_8888)

        var canvas = Canvas(bitmap)
        captureView.draw(canvas)

        if(bitmap == null) {
            return null
        }else {
            val imageFile = File(mPath)
            val outputStream = FileOutputStream(imageFile)
            outputStream.use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
            }
            if(imageFile == null) {
                //캡쳐 실패한 경우
            }else{
                try {
                    val intent = Intent(Intent.ACTION_SEND)
                    val fileUri : Uri? = FileProvider.getUriForFile(applicationContext, "com.example.capstone4_1", imageFile)
                    fileUri?.let {
                        intent.putExtra(Intent.EXTRA_STREAM, fileUri)
                        intent.type = "image/*"
                        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        val shareIntent = Intent.createChooser(intent, null)
                        startActivity(intent)
                    }
                }catch(e: ActivityNotFoundException) {
                    //공유할 수 있는 어플리케이션이 없을 때 처리
                }
            }
        }
        return mPath

    }
    override fun onBackPressed(){
        
    }

}