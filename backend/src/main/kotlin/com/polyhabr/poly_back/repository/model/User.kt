package com.polyhabr.poly_back.repository.model

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("email")
    var email: String = "mail@mail.ru",

    @SerializedName("login")
    var login: String = "me",

    @SerializedName("password")
    var password: String = "123",

    @SerializedName("name")
    var name: String = "John",

    @SerializedName("surname")
    var surname: String = "Doe",
) {

}