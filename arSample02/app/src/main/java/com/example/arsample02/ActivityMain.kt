package com.example.arsample02

import android.net.Uri
import android.os.Bundle
import android.view.View
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
    private val anchorlist: ArrayList<Anchor> = ArrayList()
    var Anchor_index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val deleteButton = binding.mybutton


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
        deleteButton.setOnClickListener(View.OnClickListener {
            removeAnchorNode(anchor)
        })
    }

    //생성
    private fun addModelToScence(anchor: Anchor, it: ModelRenderable?) {
        val anchorNode: AnchorNode = AnchorNode(anchor)
        val transform: TransformableNode = TransformableNode(arFragment.transformationSystem)
        transform.setParent(anchorNode)
        transform.renderable = it
        arFragment.arSceneView.scene.addChild(anchorNode)
        transform.select()

        anchorlist.add(anchor)

        val text = "생성" + anchorlist
        val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT)
        toast.show()

        Anchor_index++

    }

    //제거 (마지막 생성먼저)
    private fun removeAnchorNode(anchor: Anchor) {
        val An_index: Int = Anchor_index - 1
        val anchorNode: AnchorNode = AnchorNode(anchorlist.get(An_index))



        if (anchorNode != null) {
            anchorlist.remove(anchorlist.get(An_index))
            anchorNodeList.remove(anchorNode)

            anchorNode.getAnchor()?.detach()         //앵커 분리
            anchorNode.setParent(null)
            anchorNode.renderable = null


            val text = "제거" + anchorlist
            val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT)
            toast.show()

            Anchor_index--
        }

            if (Anchor_index == -1)
                Anchor_index = 0


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

