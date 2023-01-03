package com.example.currency_cal

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.currency_cal.databinding.ActivityMainBinding
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(getLayoutInflater())
        setContentView(binding!!.getRoot()); // 2
    }

    fun ConverCurrency(view: View) {

        if (binding?.dollarText!!.text!!.isNotEmpty()) {
            val dollarValue = binding?.dollarText!!.text.toString().toFloat()
            val dec = DecimalFormat("#,###.####" + "원")
            val euroValue = dec.format(dollarValue * 1260)


            binding!!.textView.text = euroValue.toString()
        } else binding!!.textView.text = getString(R.string.no_value_String)
    }

}