package com.example.phonebook.model

interface IRepository {
    fun read():List<Subscriber>
    fun write(list:List<Subscriber>):Int
}