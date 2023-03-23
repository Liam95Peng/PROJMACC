package com.example.projmacc.Game.gameobject

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.example.projmacc.R
import com.example.projmacc.Utils
import com.example.projmacc.Game.GameDisplay
import com.example.projmacc.Game.ThreadGame
import com.example.projmacc.Game.gamepanel.Buttons
import com.example.projmacc.Game.gamepanel.HealthBar
import com.example.projmacc.Game.gamepanel.Joystick
import com.example.projmacc.Game.graphics.Animator

/*
    Player is the main character of the game, which the user can control
    Player is an extension of Circle which is an extension of ArenaObject
 */
class Player(context: Context, joystick: Joystick,
             positionX: Double, positionY: Double,
             radius: Double, animator: Animator, actionControl: Buttons)
    : Circle(context, ContextCompat.getColor(context , R.color.player) ,
        positionX , positionY , radius) {

    companion object {
        const val SPEED_PIXELS_PER_SECOND = 400.0
        private val MAX_SPEED: Double = SPEED_PIXELS_PER_SECOND / ThreadGame.MAX_UPS
        const val MAX_HEALTH_POINTS = 10F

    }

    private val context: Context
    private val joystick : Joystick
    private val healthBar : HealthBar
    private var healthPoints : Float
    private val animator : Animator
    val playerState : PlayerState
    val actionControl : Buttons

    override fun update() {
        //update velocity based on actuator of joystick
        velocityX = joystick.getActuatorX() * MAX_SPEED
        velocityY = joystick.getActuatorY() * MAX_SPEED

        //CHANGES
        if(((3808.0 - (this.positionX+velocityX)) > 0.0)
            and
            ((this.positionX+velocityX) > 32.0)){
                this.positionX += velocityX
        }
        if(((3808.0 - (this.positionY+velocityY)) > 0.0)
            and
            ((this.positionY+velocityY) > 32.0)){
            this.positionY += velocityY
        }

        //ORIGINAL
        //update position
        //this.positionX += velocityX
        //this.positionY += velocityY

        //update direction
        if (velocityX != 0.0 || velocityY != 0.0){
            //normalize velocity to get direction (unit vector of velocity)
            var distance : Double = Utils.getDistanceBetweenPoints(0.0 , 0.0 , velocityX , velocityY)
            directionX = velocityX/distance
            directionY = velocityY/distance
        }
        playerState.update()
    }

    override fun draw(canvas: Canvas?, gameDisplay: GameDisplay?) {
        //super.draw(canvas, arenaDisplay)
        animator.drawPlayer(canvas , gameDisplay!!, this)
        healthBar.draw(canvas , gameDisplay)

    }

    fun getHealthPoints() : Float{
        return healthPoints
    }

    fun setHealthPoints(healthPoints : Float){
        if (healthPoints >= 0) {
            this.healthPoints = healthPoints
        }
    }

    init {
        this.joystick = joystick
        this.context = context
        this.healthPoints = MAX_HEALTH_POINTS
        this.healthBar = HealthBar(context , this)
        this.animator = animator
        this.actionControl = actionControl
        this.playerState = PlayerState(this , actionControl)
    }
}

