package com.example.capstone4_1

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class CreateQuestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_quest)
        val backbtn = findViewById<ImageButton>(R.id.backbtn)
        val makebtn = findViewById<ImageButton>(R.id.Makebtn)
        val qname = findViewById<EditText>(R.id.qname)
        val qexplain = findViewById<EditText>(R.id.qexplain)
        val questAdapter= QuestAdapter(this)

        makebtn.setOnClickListener() {
            if(qname.text.toString() == ""){
                Toast.makeText(this, "퀘스트 이름을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }else
            Character.customQuestList.add(Quest(R.drawable.ic_custom,qname.text.toString(),qexplain.text.toString()))
            Toast.makeText(this, "${qname.text} 퀘스트가 생성되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        backbtn.setOnClickListener() {
            questAdapter.notifyDataSetChanged()
            finish()
        }

    }

}