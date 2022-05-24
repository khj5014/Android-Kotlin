package com.example.capstone4_1

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.capstone4_1.databinding.ActivityMainBinding
import com.example.capstone4_1.fragment.HomeFragment
import com.example.capstone4_1.fragment.MyinfoFragment
import com.example.capstone4_1.fragment.QuestListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    var myInfoFragment: Fragment = MyinfoFragment()
    var questScreenFragment: Fragment = QuestListFragment()
    var homeFragment: Fragment = HomeFragment()

    var fragmentManager = supportFragmentManager


    // 최초 실행 시 초기화 함수
    private fun initialize() {
        // 캐릭터 클래스 초기화
        Character.initializeQuest()

    }
//사용할 때 주석제거 후 사용
//    private fun setupEvent() { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initialize()
        // setupEvent()

        //프래그먼트트랜잭션
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.mainFrag, myInfoFragment).commit()

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.menu_bottom_navigation)

        //프래그먼트 메뉴
        bottomNavigation.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            val transaction = fragmentManager.beginTransaction()

            when (item.itemId) {
                R.id.myInfo -> transaction.replace(R.id.mainFrag, myInfoFragment).commit()
                R.id.home -> transaction.replace(R.id.mainFrag, homeFragment).commit()
                R.id.questList -> transaction.replace(R.id.mainFrag, questScreenFragment).commit()
            }
            true
        })


        excuteCreateCharacterActivity()

    }

    //시스템 버튼 감지
    override fun onBackPressed() {
        if (binding.mainFrag.visibility == View.VISIBLE) {
            super.onBackPressed()
        }
    }

    private fun excuteCreateCharacterActivity() {
        val intent = Intent(this, CreateCharacterActivity::class.java)
        startActivity(intent)
    }
}