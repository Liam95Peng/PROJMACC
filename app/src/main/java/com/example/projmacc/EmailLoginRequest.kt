package com.example.projmacc

class EmailLoginRequest {
    private var email: String =""
    private var password: String = ""

    fun setEmail(email:String){
        this.email = email
    }
    fun getEmail():String{
        return this.email
    }
    fun setPass(pass:String){
        this.password=pass
    }
    fun getPass():String{
        return this.password
    }
}