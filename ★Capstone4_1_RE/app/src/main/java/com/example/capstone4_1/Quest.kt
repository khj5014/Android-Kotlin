package com.example.capstone4_1

import java.util.*

class Quest(val image: Int, val name: String, val explain: String) {
    companion object {
        private val healthQuest = arrayListOf<Quest>(
            Quest(R.drawable.bike, "윗몸 일으키기", "윗몸 일으키기 30번 하기"),
            Quest(R.drawable.bike, "팔굽혀 펴기", "팔굽혀 펴기 30번 하기"),
            Quest(R.drawable.bike, "뜀걸음", "뜀걸음 10분 하기"),
            Quest(R.drawable.bike, "걷기", "걷기 30분하기")
        )

        private val languageQuest = arrayListOf<Quest>(
            Quest(R.drawable.bike, "영단어", "영단어 20개 외우기"),
            Quest(R.drawable.bike, "독해", "영어문장 5개 독해하기"),
            Quest(R.drawable.bike, "회화", "영어문장 5개 말하기")
        )

        private val codingQuest = arrayListOf<Quest>(
            Quest(R.drawable.bike, "백준", "백준 문제 3개 풀기")
        )

        private val defaultQuestList = arrayOf(healthQuest, languageQuest, codingQuest)


        fun getRandomList(interest: Interest): ArrayList<Quest> {
            val questList = arrayListOf<Quest>()
            var selectInterest: ArrayList<Quest> = arrayListOf<Quest>()

            when (interest) {
                Interest.HEALTH ->
                    selectInterest = healthQuest
                Interest.LANGUAGE ->
                    selectInterest = languageQuest
                Interest.CODING ->
                    selectInterest = codingQuest
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