package com.example.projmacc.Game


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Insets
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.*
import android.widget.Toast
import com.example.projmacc.Game.gameobject.Circle
import com.example.projmacc.Game.gameobject.Enemy
import com.example.projmacc.Game.gameobject.Player
import com.example.projmacc.Game.gameobject.Spell
import com.example.projmacc.Game.gamepanel.*
import com.example.projmacc.Game.graphics.Animator
import com.example.projmacc.Game.graphics.SpriteSheet
import com.example.projmacc.Game.map.Tilemap
import com.example.projmacc.Home.HomeFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Game manages all objects in the game and is responsible for updating all states and render all
 * objects to the screen
 */
class GameView(context: Context?,  height: Int, width: Int) : SurfaceView(context), SurfaceHolder.Callback {
    private var joystickPointerId = 0
    private val tilemap : Tilemap
    private val joystick: Joystick
    private val fireBtn: Buttons
    private var firePointerId = 0
    private val player: Player
    private var gameLoop: ThreadGame
    private val enemyList: MutableList<Enemy> = mutableListOf()
    private val spellList: MutableList<Spell> = mutableListOf()
    private val spriteSheet : SpriteSheet
    private var buttonsId : Int
    private var numberOfSpellsToCast = 0
    private val gameOver: GameOver
    private var performance: Performance
    private val gameDisplay: GameDisplay
    private val score : Score
    var homeScore: String =""

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Handle user input touch event actions
        when (event!!.actionMasked) {
            // 1
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                if (!fireBtn.getIsPressed()){
                     firePointerId = event.getPointerId((event.actionIndex))
                }

                if (!joystick.getIsPressed()) {
                    joystickPointerId = event.getPointerId(event.actionIndex)
                }
                    // Joystick was pressed before this event -> cast spell
                if(fireBtn.isPressed(event.getX(event.findPointerIndex(firePointerId)).toDouble(),
                        event.getY(event.findPointerIndex(firePointerId)).toDouble())
                        and !fireBtn.getIsPressed()){
                    numberOfSpellsToCast++
                    fireBtn.setIsPressed(true)
                }

                if (joystick.isPressed(event.getX(event.findPointerIndex(joystickPointerId)).toDouble(),
                        event.getY(event.findPointerIndex(joystickPointerId)).toDouble())
                    and !joystick.getIsPressed()){
                    joystick.setIsPressed(true)
                }

                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (joystick.getIsPressed()) {
                    // Joystick was pressed previously and is now moved
                    joystick.setActuator(event.getX(event.findPointerIndex(joystickPointerId)).toDouble(),
                    event.getY(event.findPointerIndex(joystickPointerId)).toDouble())
                }
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                if (joystick.getIsPressed() and (joystickPointerId == event.getPointerId(event.actionIndex))) {
                    // joystick pointer was let go off -> setIsPressed(false) and resetActuator()
                    joystick.setIsPressed(false)
                    joystick.resetActuator()
                }
                if(fireBtn.getIsPressed() and (firePointerId == event.getPointerId(event.actionIndex))){
                    // joystick pointer was let go off -> setIsPressed(false) and resetActuator()
                    fireBtn.setIsPressed(false)
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d("Game.java", "surfaceCreated()")
        if (gameLoop.state.equals( Thread.State.TERMINATED)) {
            val surfaceHolder = getHolder()
            surfaceHolder.addCallback(this)
            gameLoop = ThreadGame(this, surfaceHolder)
        }
        gameLoop.startThreadGame()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d("Game.java", "surfaceChanged()")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d("Game.java", "surfaceDestroyed()")
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas!!)
        tilemap.draw(canvas , gameDisplay)
        // Draw game objects
        player.draw(canvas, gameDisplay)
        for (enemy in enemyList) {
            enemy.draw(canvas, gameDisplay)
        }
        for (spell in spellList) {
            spell.draw(canvas, gameDisplay)
        }

        // Draw game panels
        joystick.draw(canvas!!)
        fireBtn.draw(canvas)

        performance.draw(canvas!!)

        score.drawScore(canvas)

        // Draw Game over if the player is dead
        if (player.getHealthPoints() <= 0) {
            gameOver.draw(canvas, "LOSE" , score.getScore())

        }

    }

    fun googleUpdateScoreRequest( googleId: String?, score: String?): GoogleScoreRequest {
        val updateScoreRequest = GoogleScoreRequest()
        try {
            updateScoreRequest.setId(googleId!!)
            updateScoreRequest.setScore(score!!)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return updateScoreRequest
    }
    fun emailUpdateScoreRequest( googleId: String?, score: String?): UpdateScoreRequest {
        val updateScoreRequest = UpdateScoreRequest()
        try {
            updateScoreRequest.setId(googleId!!)
            updateScoreRequest.setScore(score!!)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return updateScoreRequest
    }
    var updateScore = false
    fun update() {

        performance = Performance(context , gameLoop)
        // Stop updating the game if the player is dead
        if (player.getHealthPoints() <= 0) {
            enemyList.clear()

            //update pythonanywhere database
            if(!updateScore) {
                Log.d("final score: ", score.getScore().toString())
                score.setScore(score.getScore())
                val activity: Activity? = getActivity(context)
                val credential = GoogleSignIn.getLastSignedInAccount(activity!!)
                if (credential != null) {
                    val googleId: String? = credential.id
                    Log.d("google id: ", googleId.toString())
                    GlobalScope.launch(Dispatchers.IO) {
                        val requests: GoogleScoreRequest =
                            googleUpdateScoreRequest(googleId, score.getScore().toString())
                        async { updateGoogleScore(requests) }
                    }
                }else{
                    lateinit var  mAuth: FirebaseAuth
                    mAuth = FirebaseAuth.getInstance()

                    val emailId = mAuth.uid.toString()
                    GlobalScope.launch(Dispatchers.IO) {
                        val request: UpdateScoreRequest =
                            emailUpdateScoreRequest(emailId, score.getScore().toString())
                        async { updateEmailScore(request) }
                    }
                }
                updateScore = true

            }



        }
        // Update game state
        joystick.update()

        player.update()

        // Spawn enemy
        if (Enemy.readyToSpawn()) {
            enemyList.add(Enemy(context, player, spriteSheet.enemySpriteArray))
        }
        while (numberOfSpellsToCast > 0) {
            spellList.add(Spell(context, player, spriteSheet.spellSpriteArray))
            numberOfSpellsToCast--
        }
        // Update states of all enemies
        for (enemy in enemyList) {
            enemy.update()
        }

        // Update states of all spells



        // Iterate through enemyList and Check for collision between each enemy and the player and
        // spells in spellList.
        val iteratorEnemy: MutableIterator<Enemy> = enemyList.iterator()

        while (iteratorEnemy.hasNext()) {
            var enemy: Circle = iteratorEnemy.next()
            if (Circle.isColliding(enemy, player)) {
                // Remove enemy if it collides with the player
                iteratorEnemy.remove()
                player.setHealthPoints(player.getHealthPoints()-1F)
                continue
            }
            val iteratorSpell: MutableIterator<Spell> = spellList.iterator()
            while (iteratorSpell.hasNext()) {
                var spell: Circle = iteratorSpell.next()
                // Remove enemy if it collides with a spell
                if (Circle.isColliding(spell, enemy)) {
                    iteratorSpell.remove()
                    iteratorEnemy.remove()
                    score.update()
                    break
                }
            }
            for (spell in spellList) {
                spell.update()
            }
        }
        gameDisplay.update()


        // Update gameDisplay so that it's center is set to the new center of the player's 
        // game coordinates
    }

    private fun updateEmailScore(request: UpdateScoreRequest) {
        val userResponseCall = UpdateScoreApi.getUserService().updateEmailScore(request)

        userResponseCall.enqueue(object : Callback<UpdateScoreResponse?> {
            override fun onResponse(call: Call<UpdateScoreResponse?>, response: Response<UpdateScoreResponse?>){
                if (response.isSuccessful) {
                    Log.d("risposta: ", response.body()!!.getId())
                    Log.d("risposta1: ", response.body()!!.getScore())
                    Toast.makeText( context, "Account updated",
                        Toast.LENGTH_SHORT).show()

                } else {
                    Log.d("account update", "error")
                    Toast.makeText( context, "Account not updated",
                        Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<UpdateScoreResponse?>, t: Throwable) {
                Log.d("update score", "Request Failed" + t.localizedMessage)
            }



        })
    }


    private fun updateGoogleScore(request: GoogleScoreRequest) {
        val userResponseCall = UpdateScoreApi.getUserService().updateGoogleScore(request)

        userResponseCall.enqueue(object : Callback<GoogleUpdateResponse?> {
            override fun onResponse(call: Call<GoogleUpdateResponse?>, response: Response<GoogleUpdateResponse?>){
                if (response.isSuccessful) {
                    Log.d("risposta: ", response.body()!!.getId())
                    Log.d("risposta1: ", response.body()!!.getScore())
                    Toast.makeText( context, "Account updated",
                        Toast.LENGTH_SHORT).show()

                } else {
                    Log.d("account update", "error")
                    Toast.makeText( context, "Account not updated",
                        Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<GoogleUpdateResponse?>, t: Throwable) {
                Log.d("update score", "Request Failed" + t.localizedMessage)
            }
        })
    }

    fun pause() {
        gameLoop.stopLoop()
    }

    init {
        val currentMetrics : WindowMetrics = (getContext() as Activity).windowManager.currentWindowMetrics
        val windowInsets : WindowInsets = currentMetrics.windowInsets
        val insets : Insets = windowInsets.getInsetsIgnoringVisibility(0)
        val insetsWidth : Int = insets.right + insets.left
        val insetsHeight : Int = insets.top + insets.bottom
        val bounds : Rect = currentMetrics.bounds
        val legacySize = Size(bounds.width() - insetsWidth , bounds.height() - insetsHeight)

        // Get surface holder and add callback
        val surfaceHolder = holder
        surfaceHolder.setFixedSize(legacySize.width , legacySize.height)
        surfaceHolder.addCallback(this)
        joystickPointerId = 0
        buttonsId = 0
        numberOfSpellsToCast = 0

        gameLoop = ThreadGame(this, surfaceHolder)


        // Initialize game panels
        performance = Performance(context!!, gameLoop)
        gameOver = GameOver(context, legacySize.width/2.0 , legacySize.height/2.0)
        joystick = Joystick(190.0 , height - 200.0, 90.0, 40.0)

        Log.d("height", height.toString())
        fireBtn = Buttons(width -350.0,height - 200.0,90.0)
        score = Score(context)


        // Initialize game objects
        this.spriteSheet = SpriteSheet(context)
        val animator = Animator(spriteSheet.playerSpriteArray)
        player = Player(context, joystick, legacySize.width/2.0 , legacySize.height/2.0,32.0, animator,fireBtn)


        // Initialize display and center it around the player
        val displayMetrics = DisplayMetrics()
        (getContext() as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        gameDisplay = GameDisplay(displayMetrics.widthPixels, displayMetrics.heightPixels, player)
        this.tilemap = Tilemap(spriteSheet)
        // Initialize Tilemap
        isFocusable = true
    }
}