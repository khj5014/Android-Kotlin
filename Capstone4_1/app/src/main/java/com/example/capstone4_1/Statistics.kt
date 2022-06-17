package com.example.capstone4_1

import android.util.Log

class Statistics(var genre: Int, var title: String, var limit: Array<Int>, var count: Int = 0) {
    val nextLimit: Int?
        get() {
            for (i in limit) {
                if (count < i)
                    return i
            }

            return null
        }

    val tier: String
        get() {
            if (limit[2] <= count)
                return "gold_tier"
            if (limit[1] <= count)
                return "silver_tier"
            if (limit[0] <= count)
                return "bronze_tier"

            return "unranked_tier"
        }

    companion object {
        private val healthStatistics = arrayListOf<Statistics>(
            Statistics(R.drawable.sit_up, "윗몸 일으키기", arrayOf<Int>(200, 1000, 10000)),
            Statistics(R.drawable.push_up, "팔굽혀 펴기", arrayOf<Int>(200, 1000, 10000)),
            Statistics(R.drawable.running, "뜀걸음", arrayOf<Int>(200, 1000, 5000)),
            Statistics(R.drawable.pull_up, "풀업", arrayOf<Int>(50, 300, 700))
        )

        private val languageStatistics = arrayListOf<Statistics>(
            Statistics(R.drawable.abc, "영단어", arrayOf<Int>(200, 1000, 10000)),
            Statistics(R.drawable.reading, "독해", arrayOf<Int>(50, 200, 500)),
            Statistics(R.drawable.speaking, "회화", arrayOf<Int>(100, 500, 1000))
        )

        private val codingStatistics = arrayListOf<Statistics>(
            Statistics(R.drawable.coding, "백준", arrayOf<Int>(50, 200, 500))
        )

        val statisticsList = (healthStatistics + languageStatistics + codingStatistics).toTypedArray()

        fun addCount(quest: Quest) {
            for (i in statisticsList) {
                if (i.title == quest.name) {
                    i.count += quest.value

                    break
                }
            }
        }
    }
}