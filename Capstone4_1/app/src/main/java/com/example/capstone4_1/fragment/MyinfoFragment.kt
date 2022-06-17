package com.example.capstone4_1.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.capstone4_1.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyinfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyinfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_myinfo, container, false)
        val listView = view.findViewById<ListView>(R.id.statisticsListView)
        val button = view.findViewById<ImageView>(R.id.tierInfo)

        button.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            val inflater = requireActivity().layoutInflater;
            builder.setView(inflater.inflate(R.layout.quest_show_popup, null))
            builder.show()

        }
        //화면 초기화
        view.findViewById<ImageView>(R.id.myCharacter).setImageResource(Character.icon)
        view.findViewById<TextView>(R.id.myInfoName).append("이름:${Character.name}")
        view.findViewById<TextView>(R.id.myInfoGender).append("성별:${Character.gender.value}")
        view.findViewById<TextView>(R.id.myInfoInterest).append("관심:${Character.interest.value}")
        view.findViewById<RatingBar>(R.id.hp_Bar).rating = Character.hp

        //리스트뷰 연결
        listView.adapter = StatisticsAdapter(requireContext())

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyinfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                MyinfoFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}

