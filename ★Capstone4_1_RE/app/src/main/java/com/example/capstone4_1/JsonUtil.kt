package com.example.capstone4_1
import android.content.Context
import android.view.View
import org.json.*
import java.io.*
import java.lang.Exception

public final class JsonUtil {
    companion object {
        fun toJson(user: Character): String { //object to json
            try {
                val json = JSONObject()
                json.put("name", user.name)
                json.put("gender", user.gender)
                json.put("attention", user.interest)

                return json.toString()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return ""
        }


    }

    fun saveJson(user: Character, con :Context){
        val filename = "data.json"
        val data = Character.initialize(user.name, user.gender, user.interest)
        val output :FileOutputStream
        try {
            output =con.openFileOutput(filename,Context.MODE_PRIVATE)
            output.write(JsonUtil.toJson(user).toByteArray())
            output.close()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}
