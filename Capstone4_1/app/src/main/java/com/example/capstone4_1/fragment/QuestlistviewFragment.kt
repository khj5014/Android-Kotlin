package com.example.capstone4_1.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.capstone4_1.*

class QuestlistviewFragment : Fragment() {
    private val CLICK_INTERVAL = 300
    private var lastClickedTime: Long = System.currentTimeMillis()

    private fun isSafe(): Boolean {
        return System.currentTimeMillis() - lastClickedTime > CLICK_INTERVAL
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.quest_list, container, false)
        val rootView = view.findViewById<ListView>(R.id.questListView)
        val questAdapter = QuestAdapter(requireContext())

        rootView.adapter = questAdapter

        rootView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, Iview, position, id ->
                if (!isSafe())
                    return@OnItemClickListener

                lastClickedTime = System.currentTimeMillis()

                val dialog = AlertDialog.Builder(requireContext())
                val selectQuest = parent.getItemAtPosition(position) as Quest

                //다이얼로그 이름
                dialog.setTitle(selectQuest.name)
                //다이얼로그 설명
                dialog.setMessage(" 이 퀘스트를 수행하셨나용? ")

                // 확인 버튼 클릭시 동작할 것들!!!
                dialog.setPositiveButton("완료") { dialogInterface, i ->
                    Toast.makeText(
                        requireContext(),
                        selectQuest.name + "\n 퀘스트를 완료하셨습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                    if (Character.questList[position].value == -1) {
                        val realPosition = position - Character.randomQuestList.count()
                        Character.customQuestList.removeAt(realPosition)
                    } else {
                        //퀘스트 넘김
                        Statistics.addCount(Character.randomQuestList[position])
                        //선택 퀘스트 제거
                        Character.randomQuestList.removeAt(position)
                        //퀘스트 수행 완료 시 증가
                        Character.doingQuestCount++
                    }
                    //리스트 갱신
                    rootView.adapter = QuestAdapter(requireContext())
                    questAdapter.notifyDataSetChanged()
                }
                dialog.setNegativeButton("취소") { dialogInterface, i -> }
                dialog.show()
            }

        return view
    }
}