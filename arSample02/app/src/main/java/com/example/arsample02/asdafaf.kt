package com.amodtech.ar.lineview
//import com.google.ar.sceneform.samples.hellosceneform.R;
import android.R
import android.app.Activity
import android.app.ActivityManager
import android.graphics.Color
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.HitTestResult
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Material
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.BaseArFragment.OnTapArPlaneListener
import java.util.function.Consumer
import java.util.function.Function

/**
 * LineViewMainActivity - built on HelloSceneForm sample.
 */
class LineViewMainActivity : AppCompatActivity() {
    private var arFragment: ArFragment? = null
    private var andyRenderable: ModelRenderable? = null
    private var foxRenderable: ModelRenderable? = null
    private val anchorNode: AnchorNode? = null
    private val anchorNodeList: MutableList<AnchorNode> = ArrayList()
    private var numberOfAnchors = 0
    private var currentSelectedAnchorNode: AnchorNode? = null
    private var nodeForLine: Node? = null

    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return
        }
        setContentView(R.layout.activity_ux)
        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment?

        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
            .setSource(this, R.raw.andy)
            .build()
            .thenAccept(Consumer { renderable: ModelRenderable? ->
                andyRenderable = renderable
            })
            .exceptionally(
                Function<Throwable, Void?> { throwable: Throwable? ->
                    val toast =
                        Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                    null
                })
        ModelRenderable.builder()
            .setSource(this, R.raw.arcticfox_posed)
            .build()
            .thenAccept(Consumer { renderable: ModelRenderable? ->
                foxRenderable = renderable
            })
            .exceptionally(
                Function<Throwable, Void?> { throwable: Throwable? ->
                    val toast =
                        Toast.makeText(this, "Unable to load fox renderable", Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                    null
                })
        arFragment!!.setOnTapArPlaneListener { hitResult: HitResult?, plane: Plane?, motionEvent: MotionEvent? -> }
        arFragment!!.arSceneView.scene.setOnPeekTouchListener { hitTestResult: HitTestResult, motionEvent: MotionEvent ->
            handleOnTouch(
                hitTestResult,
                motionEvent
            )
        }

        //Add a listener for the back button
        val backButtom = findViewById<FloatingActionButton>(R.id.back_buttom)
        backButtom.setOnClickListener {
            //Move the anchor back
            Log.d(TAG, "Moving anchor back")
            if (currentSelectedAnchorNode != null) {
                //Get the current Pose and transform it then set a new anchor at the new pose
                val session = arFragment!!.arSceneView.session
                val currentAnchor = currentSelectedAnchorNode!!.anchor
                val oldPose = currentAnchor!!.pose
                val newPose = oldPose.compose(Pose.makeTranslation(0f, 0f, -0.05f))
                currentSelectedAnchorNode = moveRenderable(currentSelectedAnchorNode, newPose)
            }
        }

        //Add a listener for the forward button
        val forwardButtom = findViewById<FloatingActionButton>(R.id.forward_buttom)
        forwardButtom.setOnClickListener {
            //Move the anchor forward
            Log.d(TAG, "Moving anchor forward")
            if (currentSelectedAnchorNode != null) {
                //Get the current Pose and transform it then set a new anchor at the new pose
                val session = arFragment!!.arSceneView.session
                val currentAnchor = currentSelectedAnchorNode!!.anchor
                val oldPose = currentAnchor!!.pose
                val newPose = oldPose.compose(Pose.makeTranslation(0f, 0f, 0.05f))
                currentSelectedAnchorNode = moveRenderable(currentSelectedAnchorNode, newPose)
            }
        }

        //Add a listener for the left button
        val leftButtom = findViewById<FloatingActionButton>(R.id.left_button)
        leftButtom.setOnClickListener {
            //Move the anchor left
            Log.d(TAG, "Moving anchor left")
            if (currentSelectedAnchorNode != null) {
                //Get the current Pose and transform it then set a new anchor at the new pose
                val session = arFragment!!.arSceneView.session
                val currentAnchor = currentSelectedAnchorNode!!.anchor
                val oldPose = currentAnchor!!.pose
                val newPose = oldPose.compose(Pose.makeTranslation(-0.05f, 0f, 0f))
                currentSelectedAnchorNode = moveRenderable(currentSelectedAnchorNode, newPose)
            }
        }

        //Add a listener for the right button
        val rightButtom = findViewById<FloatingActionButton>(R.id.right_button)
        rightButtom.setOnClickListener {
            //Move the anchor right
            Log.d(TAG, "Moving anchor Right")
            if (currentSelectedAnchorNode != null) {
                //Get the current Pose and transform it then set a new anchor at the new pose
                val session = arFragment!!.arSceneView.session
                val currentAnchor = currentSelectedAnchorNode!!.anchor
                val oldPose = currentAnchor!!.pose
                val newPose = oldPose.compose(Pose.makeTranslation(0.05f, 0f, 0f))
                currentSelectedAnchorNode = moveRenderable(currentSelectedAnchorNode, newPose)
            }
        }

        //Add a listener for the up button
        val upButtom = findViewById<FloatingActionButton>(R.id.up_button)
        upButtom.setOnClickListener {
            //Move the anchor up
            Log.d(TAG, "Moving anchor Up")
            if (currentSelectedAnchorNode != null) {
                //Get the current Pose and transform it then set a new anchor at the new pose
                val session = arFragment!!.arSceneView.session
                val currentAnchor = currentSelectedAnchorNode!!.anchor
                val oldPose = currentAnchor!!.pose
                val newPose = oldPose.compose(Pose.makeTranslation(0f, 0.05f, 0f))
                currentSelectedAnchorNode = moveRenderable(currentSelectedAnchorNode, newPose)
            }
        }

        //Add a listener for the down button
        val downButtom = findViewById<FloatingActionButton>(R.id.down_button)
        downButtom.setOnClickListener {
            //Move the anchor down
            Log.d(TAG, "Moving anchor Down")
            if (currentSelectedAnchorNode != null) {
                //Get the current Pose and transform it then set a new anchor at the new pose
                val session = arFragment!!.arSceneView.session
                val currentAnchor = currentSelectedAnchorNode!!.anchor
                val oldPose = currentAnchor!!.pose
                val newPose = oldPose.compose(Pose.makeTranslation(0f, -0.05f, 0f))
                currentSelectedAnchorNode = moveRenderable(currentSelectedAnchorNode, newPose)
            }
        }

        //Add a listener for the drawline button
        val drawLineButton = findViewById<FloatingActionButton>(R.id.draw_buttom)
        drawLineButton.setOnClickListener { //draw a line bteween the two Anchors, if there are exactly two anchros
            Log.d(TAG, "drawing line")
            if (numberOfAnchors == 2) {
                drawLine(anchorNodeList[0], anchorNodeList[1])
            }
        }

        //Add a listener for the delete button
        val deleteButton = findViewById<FloatingActionButton>(R.id.delete_buttom)
        deleteButton.setOnClickListener(View.OnClickListener { //Delete the Anchor if it exists
            Log.d(TAG, "Deleteing anchor")
            var currentAnchorIndex: Int
            if (numberOfAnchors < 1) {
                    Toast.makeText(this@LineViewMainActivity, "All nodes deleted", Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
            }
            removeAnchorNode(currentSelectedAnchorNode)
            currentSelectedAnchorNode = null

            //Remove the line if it exists also
            removeLine(nodeForLine)
        })
    }

    private fun handleOnTouch(hitTestResult: HitTestResult, motionEvent: MotionEvent) {
        Log.d(TAG, "handleOnTouch")
        // First call ArFragment's listener to handle TransformableNodes.
        arFragment!!.onPeekTouch(hitTestResult, motionEvent)

        //We are only interested in the ACTION_UP events - anything else just return
        if (motionEvent.action != MotionEvent.ACTION_UP) {
            return
        }

        // Check for touching a Sceneform node
        if (hitTestResult.node != null) {
            Log.d(TAG, "handleOnTouch hitTestResult.getNode() != null")
            //Toast.makeText(LineViewMainActivity.this, "hitTestResult is not null: ", Toast.LENGTH_SHORT).show();
            val hitNode = hitTestResult.node
            if (hitNode!!.renderable === andyRenderable) {
                //Toast.makeText(LineViewMainActivity.this, "We've hit Andy!!", Toast.LENGTH_SHORT).show();
                //First make the current (soon to be not current) selected node not highlighted
                if (currentSelectedAnchorNode != null) {
                    currentSelectedAnchorNode!!.renderable = andyRenderable
                }
                //Now highlight the new current selected node
                val highlightedAndyRenderable = andyRenderable!!.makeCopy()
                highlightedAndyRenderable.material.setFloat3(
                    "baseColorTint", com.google.ar.sceneform.rendering.Color(
                        Color.rgb(255, 0, 0)
                    )
                )
                hitNode!!.renderable = highlightedAndyRenderable
                currentSelectedAnchorNode = hitNode as AnchorNode?
            }
            return
        } else {
            // Place the anchor 1m in front of the camera. Make sure we are not at maximum anchor first.
            Log.d(TAG, "adding Andy in fornt of camera")
            if (numberOfAnchors < MAX_ANCHORS) {
                val frame = arFragment!!.arSceneView.arFrame
                val currentAnchorIndex = numberOfAnchors
                val session = arFragment!!.arSceneView.session
                val newMarkAnchor = session!!.createAnchor(
                    frame!!.camera.pose
                        .compose(Pose.makeTranslation(0f, 0f, -1f))
                        .extractTranslation()
                )
                val addedAnchorNode = AnchorNode(newMarkAnchor)
                addedAnchorNode.renderable = andyRenderable
                addAnchorNode(addedAnchorNode)
                currentSelectedAnchorNode = addedAnchorNode
            } else {
                Log.d(TAG, "MAX_ANCHORS exceeded")
            }
        }
    }

    private fun removeAnchorNode(nodeToremove: AnchorNode?) {
        //Remove an anchor node
        var nodeToremove = nodeToremove
        if (nodeToremove != null) {
            arFragment!!.arSceneView.scene.removeChild(nodeToremove)
            anchorNodeList.remove(nodeToremove)
            nodeToremove.anchor!!.detach()
            nodeToremove.setParent(null)
            nodeToremove = null
            numberOfAnchors--
            //Toast.makeText(LineViewMainActivity.this, "Test Delete - markAnchorNode removed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(
                this@LineViewMainActivity,
                "Delete - no node selected! Touch a node to select it.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun removeLine(lineToRemove: Node?) {
        //remove the line
        var lineToRemove = lineToRemove
        Log.e(TAG, "removeLine")
        if (lineToRemove != null) {
            Log.e(TAG, "removeLine lineToRemove is not mull")
            arFragment!!.arSceneView.scene.removeChild(lineToRemove)
            lineToRemove.setParent(null)
            lineToRemove = null
        }
    }

    private fun addAnchorNode(nodeToAdd: AnchorNode) {
        //Add an anchor node
        nodeToAdd.setParent(arFragment!!.arSceneView.scene)
        anchorNodeList.add(nodeToAdd)
        numberOfAnchors++
    }

    private fun moveRenderable(
        markAnchorNodeToMove: AnchorNode?,
        newPoseToMoveTo: Pose
    ): AnchorNode? {
        //Move a renderable to a new pose
        if (markAnchorNodeToMove != null) {
            arFragment!!.arSceneView.scene.removeChild(markAnchorNodeToMove)
            anchorNodeList.remove(markAnchorNodeToMove)
        } else {
            Log.d(TAG, "moveRenderable - markAnchorNode was null, the little £$%^...")
            return null
        }
        val frame = arFragment!!.arSceneView.arFrame
        val session = arFragment!!.arSceneView.session
        val markAnchor = session!!.createAnchor(newPoseToMoveTo.extractTranslation())
        val newMarkAnchorNode = AnchorNode(markAnchor)
        newMarkAnchorNode.renderable = andyRenderable
        newMarkAnchorNode.setParent(arFragment!!.arSceneView.scene)
        anchorNodeList.add(newMarkAnchorNode)

        //Delete the line if it is drawn
        removeLine(nodeForLine)
        return newMarkAnchorNode
    }

    private fun drawLine(node1: AnchorNode, node2: AnchorNode) {
        //Draw a line between two AnchorNodes (adapted from https://stackoverflow.com/a/52816504/334402)
        Log.d(TAG, "drawLine")
        val point1: Vector3
        val point2: Vector3
        point1 = node1.worldPosition
        point2 = node2.worldPosition


        //First, find the vector extending between the two points and define a look rotation
        //in terms of this Vector.
        val difference = Vector3.subtract(point1, point2)
        val directionFromTopToBottom = difference.normalized()
        val rotationFromAToB = Quaternion.lookRotation(directionFromTopToBottom, Vector3.up())
        MaterialFactory.makeOpaqueWithColor(
            applicationContext,
            com.google.ar.sceneform.rendering.Color(0, 255, 244)
        )
            .thenAccept { material: Material? ->
                /* Then, create a rectangular prism, using ShapeFactory.makeCube() and use the difference vector
                   to extend to the necessary length.  */Log.d(
                TAG,
                "drawLine insie .thenAccept"
            )
                val model = ShapeFactory.makeCube(
                    Vector3(.01f, .01f, difference.length()),
                    Vector3.zero(), material
                )
                /* Last, set the world rotation of the node to the rotation calculated earlier and set the world position to
           the midpoint between the given points . */
                val lineAnchor = node2.anchor
                nodeForLine = Node()
                nodeForLine!!.setParent(node1)
                nodeForLine!!.renderable = model
                nodeForLine!!.worldPosition = Vector3.add(point1, point2).scaled(.5f)
                nodeForLine!!.worldRotation = rotationFromAToB
            }
    }

    companion object {
        val TAG = LineViewMainActivity::class.java.simpleName
        private const val MIN_OPENGL_VERSION = 3.0
        private const val MAX_ANCHORS = 2

        /**
         * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
         * on this device.
         *
         *
         * Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
         *
         *
         * Finishes the activity if Sceneform can not run
         */
        fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
            if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
                Log.e(TAG, "Sceneform requires Android N or later")
                Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG)
                    .show()
                activity.finish()
                return false
            }
            val openGlVersionString =
                (activity.getSystemService(ACTIVITY_SERVICE) as ActivityManager)
                    .deviceConfigurationInfo
                    .glEsVersion
            if (openGlVersionString.toDouble() < MIN_OPENGL_VERSION) {
                Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later")
                Toast.makeText(
                    activity,
                    "Sceneform requires OpenGL ES 3.0 or later",
                    Toast.LENGTH_LONG
                )
                    .show()
                activity.finish()
                return false
            }
            return true
        }
    }
}