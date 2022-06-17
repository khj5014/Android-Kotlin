package com.example.capstone4_1


import org.json.JSONException
import org.json.JSONObject

public final class JsonUtil {
    companion object {

        fun toJson(user: Character): String { //object to json  // stub
            try {
                val json = JSONObject()
                json.put("name", user.name)
                json.put("gender", user.gender)
                json.put("interest", user.interest)

                return json.toString()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return ""
        }
        fun toJson(user: Character, questlist: ArrayList<Quest>): String { //quest = guestjson this fun not used if you need to use this fun you might test fun
            val statisticList = user.statisticsList //Array<Statics>

            try {
                val user_json = JSONObject()
                user_json.put("name", user.name)
                user_json.put("gender", user.gender.value)
                user_json.put("interest", user.interest.value)
                user_json.put("icon" ,user.icon)

                user_json.put("createTime",user.createTime)
                user_json.put("currentLogin",user.currentLogin)
                user_json.put("doingQuest",user.doingQuestCount)
                user_json.put("hp", user.hp)

                val quests_ary = org.json.JSONArray()

                for(quests in questlist){  // questlist in quests
                    var quest_json = JSONObject()
                    quest_json.put("image",quests.image)
                    quest_json.put("name",quests.name )
                    quest_json.put("content",quests.content)
                    quest_json.put("value",quests.value)
                    quests_ary.put(quest_json)
                }

                val statistics_ary = org.json.JSONArray()

                for(statistics in statisticList){
                    var static_json = JSONObject()
                    static_json.put("title",statistics.title)
                    static_json.put("count",statistics.count)
                    statistics_ary.put(static_json)
                }

                user_json.put("statistics", statistics_ary)
                user_json.put("quests", quests_ary)


                return user_json.toString()

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return ""
        }

    }

}
