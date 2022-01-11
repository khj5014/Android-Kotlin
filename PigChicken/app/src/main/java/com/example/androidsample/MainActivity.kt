package com.example.androidsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import java.lang.Exception
import java.lang.NumberFormatException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun converCurrency(view: View) {
        try {
            if (value1.text.isNotEmpty() && value2.text.isNotEmpty()) {

                val pig = Pig(value1)
                val chicken = Chicken(value2)


                if (pig == 0) {
                    textView.text = getString(R.string.re_enter4)
                } else textView.text = pig.toString() + " 마리"


                if (chicken == 0) {
                    textView2.text = getString(R.string.re_enter2)
                } else textView2.text = chicken.toString() + " 마리"

                textView3.text = ""
            } else textView3.text = getString(R.string.no_value_String)
        } catch (e: NumberFormatException) {
            textView3.text = "정수값 최대치는 2147483647 입니다"
        }

    }

    fun Pig(a: EditText): Int {
            val calcuvalue = a.text.toString().toInt()

            if ((calcuvalue % 4) != 0) {
                return 0
            } else if (calcuvalue is Int) {
                val Pigreg = calcuvalue / 4
                return Pigreg
            }

        return 0
    }

    fun Chicken(a: EditText): Int {
            val calcuvalue = a.text.toString().toInt()

            if ((calcuvalue % 2) != 0) {
                return 0
            } else if (calcuvalue is Int) {
                val Chickenwing = calcuvalue / 2
                return Chickenwing
            }

        return 0
    }
}