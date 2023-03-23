package com.example.projmacc

class LoginResponse {
    //"email":"marcololshine123@gmail.com","id":"123442bbd","name":"Mark"
    private var email: String = ""
    private var id: String = ""
    private var name: String = ""

    fun setId(id:String) {
        this.id = id
    }
    fun setName(name: String) {
        this.name = name
    }

    fun setEmail(email: String){
        this.email = email
    }
    fun getId(): String {
        return this.id
    }
    fun getName(): String {
        return this.name
    }

    fun getEmail(): String {
        return this.email
    }
}