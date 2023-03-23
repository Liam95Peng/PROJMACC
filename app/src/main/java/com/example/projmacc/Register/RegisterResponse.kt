package com.example.projmacc.Register

class RegisterResponse {
    //{"email":"marcololshine123@gmail.com","id":"bellissinmo123","name":"Mark"} response body
    private var id: String = ""
    private var email: String = ""
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