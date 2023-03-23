package com.example.projmacc.Game

class UpdateScoreRequest {
    private var id: String = ""
    private var score: String =  ""

    fun getId(): String {
        return this.id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getScore(): String {
        return this.score
    }

    fun setScore(score: String){
        this.score = score
    }

}