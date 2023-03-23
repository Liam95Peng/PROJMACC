package com.example.projmacc.Home

class GoogleScoreResponse {
    private var email: String =""
    private var score: String = ""


    fun setScore(score:String) {
        this.score = score
    }

    fun getScore(): String {
        return this.score
    }
}