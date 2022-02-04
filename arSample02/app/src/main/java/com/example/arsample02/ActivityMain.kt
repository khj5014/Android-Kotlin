package com.example.arsample02

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.arsample02.databinding.ActivityMainBinding
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode


class ActivityMain : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var arFragment: ArFragment
    private lateinit var anchor: Anchor
    private val anchorNodeList: ArrayList<AnchorNode> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val deleteButton = binding.mybutton
        deleteButton.setOnClickListener { removeAnchorNode() }

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment

        arFragment.setOnTapArPlaneListener(BaseArFragment.OnTapArPlaneListener { hitResult, plane, motionEvent ->
            anchor = hitResult.createAnchor()

            ModelRenderable.builder()
                .setSource(this, Uri.parse("andy.sfb"))
                .build()
                .thenAccept { addModelToScence(anchor, it) }
                .exceptionally {

                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setMessage(it.localizedMessage)
                        .show()

                    return@exceptionally null
                }
        })

    }

    //생성
    private fun addModelToScence(anchor: Anchor, it: ModelRenderable?) {
        val anchorNode = AnchorNode(anchor)
        val transform = TransformableNode(arFragment.transformationSystem)
        transform.setParent(anchorNode)
        transform.renderable = it
        arFragment.arSceneView.scene.addChild(anchorNode)
        transform.select()

        anchorNodeList.add(anchorNode)  //리스트에 노드 추가

        toastShow("${anchorNodeList.size} 생성")

    }

    //제거 (마지막 생성먼저)
    private fun removeAnchorNode() {
        val indNum: Int = anchorNodeList.size - 1
        if(indNum >= 0) {
            val anchorNode: AnchorNode = anchorNodeList[indNum]

            anchorNode.getAnchor()?.detach()         //앵커 분리
            anchorNode.setParent(null)
            anchorNode.renderable = null

            anchorNodeList.remove(anchorNode)

            toastShow("제거 성공")

        }else toastShow("제거 할 것이 없다.")
    }

    //토스트 메시지 출력
    private fun toastShow(message: String) {
        var toast: Toast? = null
        // 토스트 메서드
        if (toast == null) {
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        } else toast.setText(message)
        toast?.show()
    }

    //전체삭제
//    fun removeAllSticker(fragment: ArFragment) {
//        val nodeList = ArrayList(fragment.getArSceneView().getScene().getChildren())
//        for (childNode in nodeList) {
//            if (childNode is AnchorNode) {
//                if ((childNode as AnchorNode).anchor != null) {
//                    (childNode as AnchorNode).anchor!!.detach()
//                    fragment.getArSceneView().getScene().removeChild(childNode)
//                    (childNode as AnchorNode).setParent(null)
//
//                }
//            }
//
//        }
//    }
}


