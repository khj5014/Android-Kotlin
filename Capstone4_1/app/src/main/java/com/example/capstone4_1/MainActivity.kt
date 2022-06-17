package com.example.capstone4_1

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.capstone4_1.databinding.ActivityMainBinding
import com.example.capstone4_1.fragment.HomeFragment
import com.example.capstone4_1.fragment.MyinfoFragment
import com.example.capstone4_1.fragment.QuestListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val RESPONSE_CREATE_CHARACTER = 50

    var myInfoFragment: Fragment = MyinfoFragment()
    var questListFragment: Fragment = QuestListFragment()
    var homeFragment: Fragment = HomeFragment()
    var FM = supportFragmentManager

    // 최초 실행 시 초기화 함수
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initialize() {
        // 캐릭터 클래스 초기화
        val filepath = filesDir.toString() + "/data.json"
        val file = File(filepath)

        if (file.exists()) { // 파일이 존재 할경우
            Character.loadCharacter(this)
            //날짜 초기화시
            dailyReset()
            //최근 로그인 insert
            Character.currentLogin = LocalDateTime.now()
            findViewById<BottomNavigationView>(R.id.menu_bottom_navigation).selectedItemId =
                R.id.home
        } else { // 아닐경우
            excuteCreateCharacterActivity()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startService(Intent(this, AutoSave::class.java))

        // setupEvent()
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.menu_bottom_navigation)

        //프래그먼트 메뉴
        bottomNavigation.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            val transaction = FM.beginTransaction()

            when (item.itemId) {
                R.id.myInfo -> transaction.replace(R.id.mainFrag, myInfoFragment).commit()
                R.id.home -> transaction.replace(R.id.mainFrag, homeFragment).commit()
                R.id.questList -> transaction.replace(R.id.mainFrag, questListFragment).commit()
            }
            true
        })

        initialize()

        thread(start = true, true) {

            while (true) {
                val mainFrag = findViewById<FrameLayout>(R.id.mainFrag) ?: continue
                val t = mainFrag.findViewById<TextView>(R.id.remainTime) ?: continue
                runOnUiThread {
                    t.text = " " + Character.remainTimes
                }
                Thread.sleep(100)
            }
        }

    }

    //하루 초기화시 리셋할 것들
    @RequiresApi(Build.VERSION_CODES.O)
    fun dailyReset() {

        //최근 로그인 날짜시간
        val currentDay = Character.currentLogin.minusHours(6)

        Log.d("dailyReset", "current day" + currentDay)

        //현재 날짜시간
        val nowday = LocalDateTime.now()
        //날짜 차이
        val duration = Duration.between(currentDay, nowday).toDays()

        //로그
        Log.d("dailyReset", "날짜 차이 일수" + duration)
        Log.d("dailyReset", "저장 나태함 " + Character.hp)

        val questFailed = Character.doingQuestCount < 3

        for (i in 0..duration) {
            if (questFailed) {
                Character.hp += (0.5f)
                Log.d("dailyReset", "나태함 증가 " + Character.hp)

                if (Character.hp >= 3.0f) {
                    startActivity(Intent(this, EndActivity::class.java))
                }

                //랜덤퀘스트 리셋
                //doing퀘스트 리셋
                Character.doingQuestCount = 0
                Log.d("dailyReset", "퀘스트 초기화 완료")
            }
        }

        if(duration > 0)
            Character.initializeQuest()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RESPONSE_CREATE_CHARACTER -> {
                findViewById<BottomNavigationView>(R.id.menu_bottom_navigation).selectedItemId =
                    R.id.home
            }
        }
    }

    //시스템 버튼 감지
    override fun onBackPressed() {
        finish()
    }

    //내 정보 생성
    private fun excuteCreateCharacterActivity() {
        val intent = Intent(this, CreateCharacterActivity::class.java)
        startActivityForResult(intent, RESPONSE_CREATE_CHARACTER)
    }

    override fun onDestroy() {
        Character.saveCharacter(this)
        super.onDestroy()
    }
}

class AutoSave : Service() {
    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent) { //핸들링 하는 부분
        Character.saveCharacter(this)

    }
}