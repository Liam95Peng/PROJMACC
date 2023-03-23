package com.example.projmacc

class LoginRequest {
    private var id:String = ""
    private var email: String =""
    private var name: String = ""

    fun setId(id: String){
        this.id=id
    }
    fun setName(name: String) {
        this.name = name
    }

    fun setEmail(email: String){
        this.email=email
    }

    fun getId(): String{
        return this.id
    }
    fun getEmail(): String{
        return this.email
    }
    fun getName(): String{
        return this.name
    }

}