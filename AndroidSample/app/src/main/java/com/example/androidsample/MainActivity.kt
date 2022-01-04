package com.example.androidsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun converCurrency(view: View){
        if (dollarText.text.isNotEmpty()){
            val dollarValue = dollarText.text.toString().toFloat()
            val dec = DecimalFormat("#,###.####"  + "원")
            val euroValue = dec.format(dollarValue * 1188)




            textView.text = euroValue.toString()
        } else
            textView.text = getString(R.string.no_value_String)
    }

}