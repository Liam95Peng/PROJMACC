package com.example.projmacc.Game.gameobject

import com.example.projmacc.Game.gamepanel.Buttons


class PlayerState(player: Player, buttons: Buttons) {
    enum class State {
        NOT_SHOOTING,
        SHOOTING

    }

    var state: State  = State.SHOOTING
        private set
    private val player : Player = player
    private val buttons : Buttons = buttons

    fun update() {
        when (state) {
            State.NOT_SHOOTING -> {
                if (
                    buttons.getIsPressed()
                ) {
                    state = State.SHOOTING
                }
            }
            State.SHOOTING -> {
                if (
                    !buttons.getIsPressed()
                ) {
                    state = State.NOT_SHOOTING
                }
            }
        }


    }
}