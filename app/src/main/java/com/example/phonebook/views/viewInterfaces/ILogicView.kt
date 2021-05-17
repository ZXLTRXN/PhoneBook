package com.example.phonebook.views.viewInterfaces

import java.io.File

//taking out context-dependent logic from the presenter
interface ILogicView{
    fun showSuccess()
    fun showError(message: String)
    fun showError(message: Int)
    fun getFileDirectory(): File
}