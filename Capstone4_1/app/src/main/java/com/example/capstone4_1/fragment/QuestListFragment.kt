package com.example.capstone4_1.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
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
 * Use the [QuestListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestListFragment : Fragment() {
    private lateinit var qSize: TextView
    private val CLICK_INTERVAL = 300
    private var lastClickedTime: Long = System.currentTimeMillis()
    private var sToast: Toast? = null

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
        val view = inflater.inflate(R.layout.fragment_quest_list, container, false)
        val rootView = view.findViewById<ListView>(R.id.questListView)
        val questAdapter = QuestAdapter(requireContext())
        //리스트 갯수
        qSize = view.findViewById<TextView>(R.id.questListSize)

        //퀘스트 진행도 초기화
        val doQuest = view.findViewById<TextView>(R.id.doQuest)
        doQuest.setText(Character.doingQuestCount.toString() + " / 3")

        //퀘스트 버튼
        val fab = view.findViewById<Button>(R.id.fab)
        //퀘스트 생성
        fab.setOnClickListener {
            val intent = Intent(requireContext(), CreateQuestActivity::class.java)
            startActivity(intent)
        }

        rootView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, Iview, position, _ ->
                if (!isSafe())
                    return@OnItemClickListener

                lastClickedTime = System.currentTimeMillis()

                val selectQuest = parent.getItemAtPosition(position) as Quest
                val dialog = AlertDialog.Builder(requireContext()).apply {
                    //다이얼로그 이름
                    setTitle(selectQuest.name)
                    //다이얼로그 설명
                    setMessage(" 이 퀘스트를 수행하셨나용? ")
                    // 확인 버튼 클릭시 동작할 것들!!!
                    setPositiveButton("완료") { _, _ ->
                        showToast(requireContext(),selectQuest.name + "\n퀘스트를 완료하셨습니다.")

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
                        qSize.text = String.format("남은 퀘스트: %d", Character.questList.count())
                        doQuest.text = Character.doingQuestCount.toString() + " / 3"
                    }

                }

                dialog.setNegativeButton("취소") { _, _ -> }

                dialog.show()

            }
        return view
    }

    fun showToast(context: Context?, message: String?) {
        sToast?.cancel()
        if (sToast == null) {
            sToast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        } else {
            sToast!!.setText(message)
        }
        sToast!!.show()
    }

    /*--------------------------------------------- 메인 끝 ---------------------------------------------*/
    override fun onResume() {
        super.onResume()
        //리스트뷰 갱신
        view?.findViewById<ListView>(R.id.questListView)?.adapter =
            QuestAdapter(requireContext())
        QuestAdapter(requireContext()).notifyDataSetChanged()
        qSize.text = String.format("(남은 퀘스트 : %d)", Character.questList.count())
    }

    override fun onStop() {
        super.onStop()
        sToast?.cancel()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuestListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuestListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}
