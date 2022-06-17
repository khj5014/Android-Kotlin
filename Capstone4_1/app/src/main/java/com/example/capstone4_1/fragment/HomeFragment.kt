package com.example.capstone4_1.fragment

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.capstone4_1.Character
import com.example.capstone4_1.Character.survivalDays
import com.example.capstone4_1.R
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val mainCharacterIcon = view.findViewById<ImageView>(R.id.mainCharacterIcon)

        //초기화
        view.findViewById<TextView>(R.id.SVday)
            .setText(survivalDays()?.plus(1).toString() + " 일차 생존")
        view.findViewById<ImageView>(R.id.mainCharacterIcon).setImageResource(Character.icon)

        mainCharacterIcon.setOnClickListener { //이미지 클릭시
            val pos = IntArray(2) //[0]:x  [1]:y
            mainCharacterIcon.getLocationOnScreen(pos)
            makeToast(mainCharacterIcon.height) //토스트 출력

        }

        return view
    }

    private fun makeToast(y: Int) {

        val inflater = layoutInflater
        val layout: View = inflater.inflate(
            R.layout.toastborder,
            view?.findViewById(R.id.toast_layout) as ViewGroup?
        )
        val randomStrings = arrayOf("안녕하세요", "반갑습니다", "배고파")
        val message = layout.findViewById<TextView>(R.id.toastText)
        message.setText(randomStrings[Random().nextInt(randomStrings.size)])

        mToast?.cancel()

        mToast = Toast(context)
        mToast!!.setGravity(Gravity.BOTTOM, 0, y + 170)
        mToast!!.duration = Toast.LENGTH_SHORT
        mToast!!.setView(layout);
        mToast!!.show()

    }

    override fun onStop() {
        super.onStop()
        mToast?.cancel()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}