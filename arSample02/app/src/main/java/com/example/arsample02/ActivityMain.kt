package com.example.arsample02

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.arsample02.databinding.ActivityMainBinding
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode

class ActivityMain : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var arFragment: ArFragment
    private var currentSelectedAnchorNode: AnchorNode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val deleteButton = binding.mybutton

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment

        arFragment.setOnTapArPlaneListener(BaseArFragment.OnTapArPlaneListener { hitResult, plane, motionEvent ->
            var anchor: Anchor = hitResult.createAnchor()

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
            deleteButton.setOnClickListener(View.OnClickListener { //Delete the Anchor if it exists
                removeAllSticker(arFragment)
                currentSelectedAnchorNode = null

            })

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
    }

    //제거
    private fun removeAnchorNode(nodeToRemove: AnchorNode) {
        //Remove an Anchor node
        arFragment.getArSceneView().getScene().removeChild(nodeToRemove)

//        nodeToRemove.getAnchor()?.detach()
//        nodeToRemove.setParent(null)
//        nodeToRemove.renderable = null
    }
    fun removeAllSticker(fragment: ArFragment) {
        val nodeList = ArrayList(fragment.getArSceneView().getScene().getChildren())
        for (childNode in nodeList) {
            if (childNode is AnchorNode) {
                    childNode.anchor != null
                    childNode.anchor!!.detach()
                    fragment.getArSceneView().getScene().removeChild(childNode)
                    childNode.setParent(null)

            }
        }
    }
}