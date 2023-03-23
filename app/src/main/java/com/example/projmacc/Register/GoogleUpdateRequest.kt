package com.example.projmacc.Register

class GoogleUpdateRequest() {
    //{"id":"bellissinmo123","name":"Mark","email":"marcololshine123@gmail.com",}
    private var id: String = ""
    private var name: String = ""
    private var email: String = ""

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