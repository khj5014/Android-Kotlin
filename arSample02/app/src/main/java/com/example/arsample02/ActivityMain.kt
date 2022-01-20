package com.example.arsample02

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.amodtech.ar.lineview.LineViewMainActivity
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
    private var currentSelectedAnchorNode: AnchorNode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        })

        val deleteButton = binding.mybutton
        deleteButton.setOnClickListener(View.OnClickListener { //Delete the Anchor if it exists
            Log.d(LineViewMainActivity.TAG, "Deleteing anchor")
            removeAnchorNode()
            currentSelectedAnchorNode = null

        })
    }

    private fun addModelToScence(anchor: Anchor, it: ModelRenderable?) {
        val anchorNode: AnchorNode = AnchorNode(anchor)
        val transform: TransformableNode = TransformableNode(arFragment.transformationSystem)
        transform.setParent(anchorNode)
        transform.renderable = it
        arFragment.arSceneView.scene.addChild(anchorNode)
        transform.select()
    }

    private fun removeAnchorNode(nodeToRemove: AnchorNode) {
        //Remove an Anchor node
        arFragment.getArSceneView().getScene().removeChild(nodeToRemove)
        nodeToRemove.getAnchor()?.detach()
        nodeToRemove.setParent(null)
        nodeToRemove.renderable = null
    }
}