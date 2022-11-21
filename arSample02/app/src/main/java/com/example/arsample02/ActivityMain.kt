package com.example.arsample02

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.arsample02.databinding.ActivityMainBinding
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.rendering.AnimationData
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment
import com.google.ar.sceneform.ux.TransformableNode


class ActivityMain : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var arFragment: ArFragment
    private lateinit var anchor: Anchor

    private val anchorNodeList: ArrayList<AnchorNode> = ArrayList()

    private var animator: ModelAnimator? = null
    private val nimoRenderable: ModelRenderable? = null
    private var nextAnimation = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment
        val deleteButton = binding.deleteButton  //삭제버튼
        val animationplayButton = binding.animationplayButton //재생버튼
        animationplayButton.setEnabled(false)
        val allDeleteButton = binding.allDeleteButton //전체삭제버튼

        //한개씩 삭제 버튼 리스너
        deleteButton.setOnClickListener { AnchorNode(anchor).let { it1 -> removeAnchorNode(it1) } }
        //전체 삭제 버튼 리스너
        allDeleteButton.setOnClickListener { removeAllSticker(arFragment) }
        //애니메이션 삭제 버튼 리스너
        animationplayButton.setOnClickListener { onPlayAnimation() }

        arFragment.setOnTapArPlaneListener(BaseArFragment.OnTapArPlaneListener { hitResult, plane, motionEvent ->
            anchor = hitResult.createAnchor()

            ModelRenderable.builder()
                .setSource(this, Uri.parse("Playful dog.sfb"))
                .build()
                .thenAccept {
                    addModelToScence(anchor, it)
                    Log.d("★★", anchorNodeList.toString())
                }
                .exceptionally {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setMessage(it.localizedMessage).show()
                    return@exceptionally null
                }

            val TFN = TransformableNode(arFragment.transformationSystem)
            TFN.setOnTapListener { hitTestResult, _ ->
                removeAnchorNode(AnchorNode(anchor))
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

    //제거
    private fun removeAnchorNode(seleNode: AnchorNode) {
        val indNum: Int = anchorNodeList.size - 1
        if (indNum >= 0) {
            arFragment.getArSceneView().getScene().removeChild(seleNode)
            seleNode.setParent(null);
            seleNode.getAnchor()?.detach();
            seleNode.renderable = null
            toastShow("제거 성공")

        } else toastShow("제거 할 것이 없다.")

    }

    //전체삭제
    private fun removeAllSticker(fragment: ArFragment) {
        val indNum: Int = anchorNodeList.size - 1
        if (indNum >= 0) {
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
            toastShow("제거 성공")
        } else toastShow("제거 할 것이 없다.")
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

    //토스트 메시지 출력
    private fun toastShow(message: String) {
        var toast: Toast? = null
        // 토스트 메서드
        if (toast == null) {
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        } else toast.setText(message)
        toast?.show()
    }
}