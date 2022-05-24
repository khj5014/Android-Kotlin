package com.example.capstone4_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class QuestShowActivity : AppCompatActivity() {
    private lateinit var binding: com.example.capstone4_1.databinding.QuestShowPopupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
//    //토스트 메시지 사용법 -->  기본값 String toast("내용")  형변환 예시 --> 변수.toString()
//    private fun toast(message: String) {
//        var toast: Toast? = null
//        // 토스트 메서드
//        if (toast == null) {
//            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
//        } else toast.setText(message)
//        toast?.show()
//    }
}