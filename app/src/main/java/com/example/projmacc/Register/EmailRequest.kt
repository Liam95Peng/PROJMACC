package com.example.projmacc.Register

class EmailRequest {
    private var id: String = ""
    private var name: String = ""
    private var email: String = ""
    private var password: String = ""

    fun setPass(password:String) {
        this.password = password
    }
    fun setId(id:String) {
        this.id = id
    }
    fun setName(name: String) {
        this.name = name
    }
    fun setEmail(email: String){
        this.email = email
    }
    fun getPass(): String {
        return this.password
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