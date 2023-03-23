package com.example.projmacc.Comments

class CommentsRequest {
    private var numbId: String = ""
    private var email: String = ""
    private var comment: String = ""

    fun setEmail (name:String) {
        this.email =name
    }
    fun getEmail():String {
        return email
    }

    fun setComment (comment:String) {
        this.comment = comment
    }
    fun getComment():String {
        return comment
    }

    fun getNumbId():String {
        return numbId
    }

    fun setNumbId (numbId:String) {
        this.numbId = numbId
    }
}