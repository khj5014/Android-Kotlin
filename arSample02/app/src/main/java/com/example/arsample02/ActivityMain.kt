package com.example.arsample02

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.arsample02.databinding.ActivityMainBinding
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.rendering.AnimationData
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode


class ActivityMain : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var arFragment: ArFragment
    private lateinit var anchor: Anchor
    private val anchorNodeList: ArrayList<AnchorNode> = ArrayList()
    private var animator: ModelAnimator? = null
    private val nimoRenderable: ModelRenderable? = null
    private var nextAnimation = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val deleteButton = binding.mybutton  //삭제버튼
        val animationplayButton = binding.mybutton2 //재생버튼
        animationplayButton.setEnabled(false)
        val allDeleteButton = binding.mybutton3 //전체삭제버튼


        //한개씩 삭제 버튼 리스너
        deleteButton.setOnClickListener { removeAnchorNode() }
        //전체 삭제 버튼 리스너
        allDeleteButton.setOnClickListener { removeAllSticker(arFragment) }
        //한개씩 삭제 버튼 리스너
        animationplayButton.setOnClickListener { onPlayAnimation() }
        //테스트용 리스너
        deleteButton.setOnLongClickListener {
            toastShow("길게 누르기")
            true
        }

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment

        arFragment.setOnTapArPlaneListener(BaseArFragment.OnTapArPlaneListener { hitResult, plane, motionEvent ->
            anchor = hitResult.createAnchor()

            ModelRenderable.builder()
                .setSource(this, Uri.parse("3dmodel.sfb"))
                .build()
                .thenAccept { addModelToScence(anchor, it) }
                .exceptionally {

                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setMessage(it.localizedMessage)
                        .show()

                    return@exceptionally null
                }
        })
        if (!animationplayButton.isEnabled()) {
            animationplayButton.setBackgroundTintList(
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black))
            )

        } else {
            animationplayButton.setEnabled(true)


        }


    }

    //생성
    private fun addModelToScence(anchor: Anchor, it: ModelRenderable?) {
        val anchorNode = AnchorNode(anchor)
        val transform = TransformableNode(arFragment.transformationSystem)

        transform.getScaleController().setMinScale(0.400f);
        transform.getScaleController().setMaxScale(0.500f);
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
        if (indNum >= 0) {
            val anchorNode: AnchorNode = anchorNodeList[indNum]

            anchorNode.getAnchor()?.detach()         //앵커 분리
            anchorNode.setParent(null)
            anchorNode.renderable = null

            anchorNodeList.remove(anchorNode)

            toastShow("제거 성공")

        } else toastShow("제거 할 것이 없다.")
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
    private fun removeAllSticker(fragment: ArFragment) {
        val nodeList = ArrayList(fragment.getArSceneView().getScene().getChildren())
        for (childNode in nodeList) {
            if (childNode is AnchorNode) {
                if ((childNode as AnchorNode).anchor != null) {
                    (childNode as AnchorNode).anchor!!.detach()
                    fragment.getArSceneView().getScene().removeChild(childNode)
                    (childNode as AnchorNode).setParent(null)
                    anchorNodeList.removeAll(anchorNodeList)
                }
            }

        }
    }

    private fun onPlayAnimation() {
        if (animator == null || !animator!!.isRunning) {
            val data: AnimationData = nimoRenderable!!.getAnimationData(nextAnimation)
            nextAnimation = (nextAnimation + 1) % nimoRenderable.getAnimationDataCount()
            animator = ModelAnimator(data, nimoRenderable)
            animator!!.start()

            val toast = Toast.makeText(this, data.name, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
}




