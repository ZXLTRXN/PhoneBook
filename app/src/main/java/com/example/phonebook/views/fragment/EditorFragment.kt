package com.example.phonebook.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.phonebook.R
import com.example.phonebook.model.Subscriber
import com.example.phonebook.presenter.LogicPresenterImpl
import com.example.phonebook.views.viewInterfaces.OnFragmentSendDataListener

class EditorFragment(presenter:LogicPresenterImpl): Fragment(){
    private val presenter = presenter

    //communication channel to Activity
    private lateinit var fragmentSendDataListener: OnFragmentSendDataListener

    private  var etName:EditText? = null
    private  var etPhone:EditText? = null
    private  var bSave: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater.inflate(R.layout.fragment_editor,container,false)

        etName = rootView.findViewById(R.id.et_name)
        etPhone = rootView.findViewById(R.id.et_phone)
        bSave = rootView.findViewById(R.id.bSave)



        var position:Int? = arguments?.getString("position")?.toInt()
        if(position !=null){
            onEdit(position)
        }

        bSave?.setOnClickListener {
            val sub = Subscriber(etName?.text.toString(),etPhone?.text.toString())
            var status = if(position != null) presenter.edit(position,sub)
            else presenter.addSub(sub)
            if(status == 0){
                fragmentManager?.popBackStack()
            }

        }

        return rootView
    }

    private fun onEdit(position:Int){
        val (name, pNum1) = presenter.getSub(position)
        etName?.setText(name)
        etPhone?.setText(pNum1)
    }


}