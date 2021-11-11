package uz.umarxon.registration.DB

import uz.umarxon.registration.Models.User

interface ServiceInterface {
    fun addMember(user:User)
    fun editMember(user:User):Int
    fun deleteMember(user:User)
    fun getAllMember():ArrayList<User>
}