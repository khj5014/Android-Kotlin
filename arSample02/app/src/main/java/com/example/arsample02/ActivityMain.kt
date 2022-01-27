package com.example.arsample02

import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.arsample02.databinding.ActivityMainBinding
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.HitTestResult
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode


class ActivityMain : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var arFragment: ArFragment
    private val anchorNodeList: ArrayList<AnchorNode> = ArrayList()
    private var numberOfAnchors = 0
    private var currentSelectedAnchorNode: AnchorNode? = null
    private lateinit var anchor: Anchor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val deleteButton = binding.mybutton

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment

        arFragment.setOnTapArPlaneListener(BaseArFragment.OnTapArPlaneListener { hitResult, plane, motionEvent ->
            anchor = hitResult.createAnchor()

            numberOfAnchors++
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
            removeAnchorNode(AnchorNode(anchor))
        })
        val transform: TransformableNode = TransformableNode(arFragment.transformationSystem)
        transform.setOnTapListener { hitTestResult: HitTestResult, Event: MotionEvent? ->
            val nodeToRemove: Node? = hitTestResult.getNode()
            val anchorNode = anchorNode(nodeToRemove)
            anchorNode.removeChild(nodeToRemove)
        }

    }

    //생성
    private fun addModelToScence(anchor: Anchor, it: ModelRenderable?) {
        val anchorNode: AnchorNode = AnchorNode(anchor)
        val transform: TransformableNode = TransformableNode(arFragment.transformationSystem)
        transform.setParent(anchorNode)
        transform.renderable = it
        arFragment.arSceneView.scene.addChild(anchorNode)
        transform.select()
    }

    //제거
    private fun removeAnchorNode(nodeCut: AnchorNode?) {
        //Remove an anchor node
        if (nodeCut != null) {
            arFragment.getArSceneView().getScene().removeChild(nodeCut)

            anchorNodeList.remove(nodeCut)
            nodeCut.getAnchor()?.detach()
            nodeCut.setParent(null)
            nodeCut.renderable = null
            numberOfAnchors--
        }
    }

    fun removeAllSticker(fragment: ArFragment) {
        val nodeList = ArrayList(fragment.getArSceneView().getScene().getChildren())
        for (childNode in nodeList) {
            if (childNode is AnchorNode) {
                if ((childNode as AnchorNode).anchor != null) {
                    (childNode as AnchorNode).anchor!!.detach()
                    fragment.getArSceneView().getScene().removeChild(childNode)
                    (childNode as AnchorNode).setParent(null)
                }
            }
        }
    }

}

