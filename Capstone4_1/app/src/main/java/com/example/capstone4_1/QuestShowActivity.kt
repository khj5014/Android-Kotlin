package com.example.capstone4_1

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone4_1.databinding.QuestShowPopupBinding

class QuestShowActivity : AppCompatActivity() {
    private lateinit var binding: QuestShowPopupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = QuestShowPopupBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.okBtn.setOnClickListener(View.OnClickListener {
            toast("오케이 버튼을 눌렀다")
        })
        binding.cancleBtn.setOnClickListener(View.OnClickListener {
            toast("취소 버튼을 눌렀다.")
        })


    }


    //토스트 메시지 사용법 -->  기본값 String toast("내용")  형변환 예시 --> 변수.toString()
    private fun toast(message: String) {
        var toast: Toast? = null
        // 토스트 메서드
        if (toast == null) {
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        } else toast.setText(message)
        toast?.show()
    }
}