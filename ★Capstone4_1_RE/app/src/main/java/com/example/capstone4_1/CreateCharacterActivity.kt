package com.example.capstone4_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.example.capstone4_1.databinding.ActivityCreateCharacterBinding
import com.rd.draw.controller.DrawController.ClickListener

class CreateCharacterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateCharacterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCharacterBinding.inflate(layoutInflater)

        //스텁 코드
        binding.viewPager.adapter = CharacterListPagerAdapter(getCharacterImageId())

        binding.viewPagerIndicater.setClickListener(ClickListener {
            binding.viewPagerIndicater.setSelected(it)
            binding.viewPager.setCurrentItem(it, true)
        })

        setContentView(binding.root)
    }

    fun getCharacterImageId(): ArrayList<Int> {
        val baseName = "sprite_char"
        val temp = ArrayList<Int>()

        var i = 1
        while (true) {
            val readName = baseName + i++

            val imageId = this.resources.getIdentifier(readName, "drawable", this.packageName)

            if (imageId == 0)
                break

            temp.add(imageId)
        }

        return temp
    }

    fun reset(view: View) {
        binding.name.setText("")
        binding.gender.clearCheck()
        binding.interestList.clearCheck()
        binding.viewPagerIndicater.setSelected(0)
        binding.viewPager.setCurrentItem(0, false)
    }

    fun confirm(view: View) {
        if (!check()) {
            Toast.makeText(this, "입력값을 다시 확인해 주세요.", Toast.LENGTH_SHORT)
            return
        }

        Character.name = binding.name.text.toString()
        Character.gender = if (binding.male.isChecked) Gender.MALE else Gender.FEMALE

        for (i in Interest.values()) {
            val selected = findViewById<TextView>(binding.interestList.checkedRadioButtonId).text

            if (selected == i.value)
                Character.interest = i
        }

        finish()
    }

    private fun check(): Boolean {
        if (binding.name.text.toString().trim() == "")
            return false
        else if (!binding.gender.isSelected)
            return false
        else if (!binding.interestList.isSelected)
            return false

        return true
    }
}

class CharacterListPagerAdapter(private val data: ArrayList<Int>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val view = inflater.inflate(R.layout.character_list, container, false)

        return view.apply {
            findViewById<ImageView>(R.id.imageCharacter).setImageResource(data[position])

            container.addView(this)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View?)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return data.size
    }
}