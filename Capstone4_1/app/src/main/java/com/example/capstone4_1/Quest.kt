package com.example.capstone4_1

import java.util.*

class Quest(val image: Int, val name: String, val content: String, val value: Int = -1) {
    val explain: String
        get() {
            return String.format(content, value)
        }

    companion object {
        private val healthQuest = arrayListOf<Quest>(
            Quest(R.drawable.sit_up, "윗몸 일으키기", "윗몸 일으키기 %d번 하기", 30),
            Quest(R.drawable.push_up, "팔굽혀 펴기", "팔굽혀 펴기 %d번 하기", 30),
            Quest(R.drawable.running, "뜀걸음", "뜀걸음 %d분 하기", 10),
            Quest(R.drawable.pull_up, "풀업", "풀업 %d번 하기", 10)
        )

        private val languageQuest = arrayListOf<Quest>(
            Quest(R.drawable.abc, "영단어", "영단어 %d개 외우기", 5),
            Quest(R.drawable.abc, "영단어", "영단어 %d개 외우기", 10),
            Quest(R.drawable.abc, "영단어", "영단어 %d개 외우기", 15),
            Quest(R.drawable.reading, "독해", "영어문장 %d개 독해하기", 3),
            Quest(R.drawable.reading, "독해", "영어문장 %d개 독해하기", 5),
            Quest(R.drawable.speaking, "회화", "영어문장 %d개 말하기", 3),
            Quest(R.drawable.speaking, "회화", "영어문장 %d개 말하기", 5),
            Quest(R.drawable.speaking, "회화", "영어문장 %d개 말하기", 7),
            Quest(R.drawable.speaking, "회화", "일본어문장 %d개 말하기", 3),
            Quest(R.drawable.speaking, "회화", "일본어문장 %d개 말하기", 5),
            Quest(R.drawable.speaking, "회화", "일본어문장 %d개 말하기", 7),
        )

        private val codingQuest = arrayListOf<Quest>(
            Quest(R.drawable.coding, "백준", "백준 1단계 - %d개 풀기", 1),
            Quest(R.drawable.coding, "백준", "백준 1단계 - %d개 풀기", 2),
            Quest(R.drawable.coding, "백준", "백준 1단계 - %d개 풀기", 3),
            Quest(R.drawable.coding, "백준", "백준 2단계 - %d개 풀기", 1),
            Quest(R.drawable.coding, "백준", "백준 2단계 - %d개 풀기", 2),
            Quest(R.drawable.coding, "백준", "백준 3단계 - %d개 풀기", 1),
            Quest(R.drawable.coding, "백준", "백준 3단계 - %d개 풀기", 2),
            Quest(R.drawable.coding, "백준", "백준 4단계 - %d개 풀기", 1),
            Quest(R.drawable.coding, "백준", "백준 4단계 - %d개 풀기", 2),
            Quest(R.drawable.coding, "백준", "백준 5단계 - %d개 풀기", 1),
            Quest(R.drawable.coding, "백준", "백준 5단계 - %d개 풀기", 2),
            Quest(R.drawable.coding, "백준", "백준 분류 - '문자열' 문제 %d개 풀기", 2),
            Quest(R.drawable.coding, "백준", "백준 분류 - '수학' 문제 %d개 풀기", 2),
            Quest(R.drawable.coding, "백준", "백준 분류 - '정렬' 문제 %d개 풀기", 2),
            Quest(R.drawable.coding, "백준", "백준 분류 - '스택' 문제 %d개 풀기", 2),
            Quest(R.drawable.coding, "백준", "백준 분류 - '큐' 문제 %d개 풀기", 2),
            Quest(R.drawable.coding, "백준", "백준 분류 - 전체 문제 랜덤 문제 풀기- %d 포인트", 5),

            )

        private val defaultQuestList = arrayOf(healthQuest, languageQuest, codingQuest)

        fun getRandomList(interest: Interest): ArrayList<Quest> {
            val questList = arrayListOf<Quest>()
            var selectInterest: ArrayList<Quest> = arrayListOf<Quest>()

            selectInterest = when (interest) {
                Interest.HEALTH ->
                    healthQuest
                Interest.LANGUAGE ->
                    languageQuest
                Interest.CODING ->
                    codingQuest
            }

            val r = Random()

            for (i in 1..3) {
                questList.add(selectInterest[r.nextInt(selectInterest.size)])
            }

            for (i in 1..7) {
                val selectRandomInterest = defaultQuestList[r.nextInt(defaultQuestList.size)]

                questList.add(selectRandomInterest[r.nextInt(selectRandomInterest.size)])
            }

            return questList
        }
    }
}