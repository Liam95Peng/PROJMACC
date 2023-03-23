package com.example.projmacc.Game

class GoogleUpdateResponse {
    private var id: String = ""
    private var score: String =  ""

    fun setId(id:String) {
        this.id = id
    }
    fun getId():String {
        return this.id
    }
    fun getScore():String {
        return this.score
    }
}