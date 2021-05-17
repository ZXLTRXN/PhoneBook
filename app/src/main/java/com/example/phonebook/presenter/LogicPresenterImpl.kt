package com.example.phonebook.presenter

import com.example.phonebook.R
import com.example.phonebook.model.IRepository
import com.example.phonebook.model.InternalStorage
import com.example.phonebook.model.Subscriber
import com.example.phonebook.views.viewInterfaces.ILogicView
import java.io.File
import java.lang.Exception
import java.lang.ref.WeakReference

class LogicPresenterImpl :ILogicPresenter {

    //service
    private var repo: IRepository? = null
    private var viewState:WeakReference<ILogicView>?=null

    //such as constructor
    fun attachView(view: ILogicView){
        viewState = WeakReference(view)

        loadData()
    }

    //controls
    private var subList = mutableListOf<Subscriber>()

    private var searchableSubList:MutableList<Subscriber>? = null

    override fun getSubs(): List<Subscriber> = subList

    override fun getSub(position:Int): Subscriber {
        if(searchableSubList != null){
            return if(searchableSubList!!.isNotEmpty())   searchableSubList!![position]
            else subList[position]
        }else {
            return subList[position]
        }
    }

    override fun edit(position:Int, sub:Subscriber):Int {
        var pos = position
        if(searchableSubList != null) {
            if (searchableSubList!!.isNotEmpty()) pos = findInSubs(position)
        }

        if(subList[pos] != sub){
            if(addSub(sub,subList[pos].name) == 1) return 1
            subList.removeAt(pos)
            subList.sortBy{it.name.toLowerCase()}
        }else{
            viewState?.get()?.showError(R.string.no_changes)
        }
        return 0
    }

    override fun deleteAll():Int {
        subList.clear()
        searchableSubList = null
        return 0
    }

    override fun deleteSub(position: Int):Int {
        var pos = position
        var flag = false
        if(searchableSubList != null) {
            if (searchableSubList!!.isNotEmpty()) {
                pos = findInSubs(position)
                flag = true
            }
        }

        try{
            subList.removeAt(pos)
            if(flag) searchableSubList?.removeAt(position)
            viewState?.get()?.showSuccess()
            return 0
        }catch(e:Exception){
            viewState?.get()?.showError(R.string.error_delete)
            return 1
        }
    }

    override fun addSub(sub: Subscriber, permittedName:String):Int {
        var flag = true
        var validStatus = 0

        val (name, pNum1) = sub
        if(!validateName(name)) {
            flag = false
            validStatus += 1
        }
        if(!validatePhone(pNum1)) {
            flag = false
            validStatus += 2
        }

        if(flag) {
            for(el in subList){
                if(el.name == sub.name && permittedName != sub.name) {
                    flag = false
                    break
                }
            }
        }else{
            if(validStatus == 1)
                viewState?.get()?.showError(R.string.error_validation_name)
            if(validStatus == 2)
                viewState?.get()?.showError(R.string.error_validation_phone)
            if(validStatus == 3)
                viewState?.get()?.showError(R.string.error_validation_all)
            return 1
        }

        if(flag) {
            subList.add(sub)
            if(permittedName == "") subList.sortBy { it.name.toLowerCase() }
            viewState?.get()?.showSuccess()
            return 0
        }
        else {
            viewState?.get()?.showError(R.string.error_exists)
            return 1
        }
    }

    override fun findSub(name: String) {
        if(name == "") searchableSubList = null
        else searchableSubList = subList.filter { it.name.toLowerCase().contains(name.toLowerCase()) }.toMutableList()
    }

    override fun countSubs():Int {
        var size = subList.size
        if(searchableSubList != null)
            if(searchableSubList!!.isNotEmpty()) size = searchableSubList!!.size
            else size = 0

        return size
    }


    //utils
    private fun validateName(name:String):Boolean{
        return name.matches(Regex("""^\S[a-zA-Z а-яА-Я.-]{1,29}$"""))
    }

    private fun validatePhone(phone:String):Boolean{
        return phone.matches(Regex("""(^8\d{10}$)|(^\+7\d{10}$)|(^\d{6,7}$)"""))
    }

    private fun findInSubs(position:Int):Int = subList.indexOf(searchableSubList!![position])

    //data repository
    override fun saveData(){
        repo?.write(subList)
    }

    private fun loadData(){
        var filedir = viewState?.get()?.getFileDirectory()
        if(filedir != null)
            repo = InternalStorage(filedir)

        val result = repo?.read()
        if(result != null)
            subList = result.sortedBy{ it.name.toLowerCase() }.toMutableList()
    }
}

