package com.example.phonebook.presenter

import com.example.phonebook.model.Subscriber

interface ILogicPresenter {

    fun getSubs():List<Subscriber>
    fun getSub(position:Int): Subscriber
    fun edit(position:Int, sub: Subscriber):Int
    fun deleteAll():Int
    fun deleteSub(position:Int):Int
    fun addSub(sub:Subscriber, permittedName:String = ""):Int
    fun findSub(name:String)
    fun countSubs(): Int

    fun saveData()
}