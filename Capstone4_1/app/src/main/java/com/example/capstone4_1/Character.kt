package com.example.capstone4_1


import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import org.json.JSONObject
import java.io.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

// 할당된 값은 전부 스텁코드이며, 이후 필요한 스텁코드는 여기서 작성
// 실제 구동시에는 initialize 시리즈 함수를 이용하여 초기화 시켜 사용해야함
object Character {
    val filename: String = "data.json" // data file
    var name: String = "nameStub"
    var gender: Gender = Gender.MALE
    var interest: Interest = Interest.HEALTH
    var icon: Int = R.drawable.sprite_char1
    var customQuestList = arrayListOf<Quest>()
    var randomQuestList = arrayListOf<Quest>()
    var doingQuestCount = 0  // 금일 퀘스트 수행   0 / 3
    lateinit var currentLogin: LocalDateTime //최근 로그인
    @RequiresApi(Build.VERSION_CODES.O)
    var createTime: LocalDateTime = LocalDateTime.now() //캐릭터 생성시점
    var hp: Float = 0.0f // 나태함
    var isInitialized: Boolean = false

    val statisticsList: Array<Statistics> = Statistics.statisticsList

    val remainTimes: String
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val remainQuestTime: LocalTime
                val nowTime = LocalTime.now()
                val resetTime = LocalTime.parse("06:00:00") // stub code must be changed
                var duration = Duration.between(resetTime, nowTime).seconds
//            Log.d("Duration :","${duration.toString()}" )
                if (duration < 0) {
                    duration = -1 * duration
                    val hour = duration / 3600
                    duration %= 3600
                    val minutes = duration / 60
                    duration %= 60
                    val seconds = duration
                    remainQuestTime = LocalTime.of(hour.toInt(), minutes.toInt(), seconds.toInt())
                } else {
                    duration = 86400 - duration
                    val hour = duration / 3600
                    duration %= 3600
                    val minutes = duration / 60
                    duration %= 60
                    val seconds = duration
                    remainQuestTime = LocalTime.of(hour.toInt(), minutes.toInt(), seconds.toInt())
                }

                return remainQuestTime.toString()
            }
            return ""
        }

    var questList: ArrayList<Quest>
        get() {
            return (randomQuestList + customQuestList) as ArrayList<Quest>
        }
        set(qValue) {
            var tempRandom = arrayListOf<Quest>()
            var tempCustom = arrayListOf<Quest>()

            for (i in qValue) {
                if (i.value == -1)
                    tempCustom.add(i)
                else
                    tempRandom.add(i)
            }

            customQuestList = tempCustom
            randomQuestList = tempRandom
        }

    fun initializeQuest() {
        randomQuestList = Quest.getRandomList(interest)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun survivalDays(): Int { // return int  & must be tested
        return Period.between(createTime.toLocalDate(), currentLogin?.toLocalDate()).days
    }

    fun saveCharacter(context: Context) {//save
        if (!isInitialized)
            return

        val output: FileOutputStream

        try {
            output = context.openFileOutput(filename, Context.MODE_PRIVATE)
            output.write(JsonUtil.toJson(this, questList).toByteArray())
            output.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadCharacter(context: Context): String {// load
        val filepath = context.filesDir.toString() + "/" + filename
        val file = File(filepath)
        try {
            val reader = BufferedReader(FileReader(file))
            var result: String? = ""
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                result += line
            }
            try {
                val data = JSONObject(result)
                val ary_quests = data.optJSONArray("quests")
                val ary_statistics = data.optJSONArray("statistics")

                val tmp_quests = ArrayList<Quest>()

                for (statistic in 0 until ary_statistics.length()) {
                    var tmp_statistic = ary_statistics.getJSONObject(statistic)
                    statisticsList.forEach { it ->
                        if (it.title == tmp_statistic.getString("title")) {
                            it.count = tmp_statistic.getInt("count")
                        }
                    }

                }

                for (quest in 0 until ary_quests.length()) {  // need to update
                    val tmp_object = ary_quests.getJSONObject(quest)
                    val tmp_quest = Quest(
                        tmp_object.getInt("image"),
                        tmp_object.getString("name"),
                        tmp_object.getString("content"),
                        tmp_object.getInt("value")
                    )

                    tmp_quests.add(tmp_quest)
//                    Log.d("test_value", tmp_object.getString("image").toString() )
//                    Log.d("test_value", tmp_object.getString("name") )
//                    Log.d("test_value", tmp_object.getString("content") )
                }
                this.questList = tmp_quests
                this.doingQuestCount = data.getInt("doingQuest")
                this.name = data.getString("name")
                this.hp = data.getDouble("hp").toFloat()
                this.currentLogin = LocalDateTime.parse(data.getString("currentLogin"))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // createTime load check sdk v
                    this.createTime = LocalDateTime.parse(
                        data.getString("createTime"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.KOREA)
                    )
//                    Log.d("crateTime end", "create time excuted ")
//                    Log.d("createvalue", this.createTime.toString())
                }

//                Log.d("log_name",this.name.toString())

                for (i in Gender.values()) { // this.gender = data.getString(Gender!)
                    if (i.value == data.getString("gender")) {
                        this.gender = i
                        break
                    }
                }

                for (i in Interest.values()) {// this.interest = data.getString(Interest!)
                    if (i.value == data.getString("interest")) {
                        this.interest = i
                        break
                    }
                }

                this.icon = data.getInt("icon")// icon
                this.isInitialized = true

//                Log.d("createTime",this.createTime.toString())
//                Log.d("log_gender",this.gender.toString())
//                Log.d("log_interest",this.interest.toString())
//                Log.d("log_quests",this.questList.toString())


            } catch (e: java.lang.Exception) {
                Log.d("에러", e.printStackTrace().toString())
            }
            reader.close()
        } catch (e1: FileNotFoundException) {
            Log.d("파일못찾음", e1.printStackTrace().toString())
        } catch (e2: IOException) {
            Log.d("읽기오류", e2.printStackTrace().toString())
        }
        return ""
    }

}