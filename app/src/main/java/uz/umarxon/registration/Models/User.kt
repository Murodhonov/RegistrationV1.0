package uz.umarxon.registration.Models

import java.io.Serializable

class User:Serializable {

    var id: Int? = null
    var name: String? = null
    var phone: String? = null
    var image: String? = null
    var countries: Int? = null
    var address: String? = null
    var password: String? = null

    constructor()
    constructor(
        id: Int?,
        name: String?,
        phone: String?,
        image: String?,
        countries: Int?,
        address: String?,
        password: String?,
    ) {
        this.id = id
        this.name = name
        this.phone = phone
        this.image = image
        this.countries = countries
        this.address = address
        this.password = password
    }

    constructor(
        name: String?,
        phone: String?,
        image: String?,
        countries: Int?,
        address: String?,
        password: String?,
    ) {
        this.name = name
        this.phone = phone
        this.image = image
        this.countries = countries
        this.address = address
        this.password = password
    }


}