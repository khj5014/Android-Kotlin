package com.example.capstone4_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MyInfoActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)

        var name = findViewById<TextView>(R.id.myInfoName)
        var gender = findViewById<TextView>(R.id.myInfoGender)
        var interest = findViewById<TextView>(R.id.myInfoInterest)

        var nameValue = Character.name
        var genderValue = Character.gender
        var interestValue = Character.interest


        name.append(" $nameValue")
        gender.append(" $genderValue")
        interest.append(" $interestValue")

    }
}