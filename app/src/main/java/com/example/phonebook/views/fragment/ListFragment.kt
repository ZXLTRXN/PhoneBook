package com.example.phonebook.views.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.phonebook.R
import com.example.phonebook.presenter.LogicPresenterImpl
import com.example.phonebook.views.adapter.ListAdapter
import com.example.phonebook.views.viewInterfaces.OnFragmentSendDataListener
import java.lang.ClassCastException

class ListFragment(presenter:LogicPresenterImpl): Fragment(){
    private val presenter = presenter

    var rv:RecyclerView? = null
    private var etSearch: EditText? = null
    private var bClear: Button? = null

    //communication channel to Activity
    private lateinit var fragmentSendDataListener: OnFragmentSendDataListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater.inflate(R.layout.fragment_list,container,false)
        etSearch = rootView.findViewById(R.id.et_search)
        bClear = rootView.findViewById(R.id.b_clear_search)

        rv = rootView.findViewById(R.id.rv_list)
        rv?.adapter = ListAdapter(presenter,fragmentSendDataListener)
        rv?.hasFixedSize()
        rv?.layoutManager =LinearLayoutManager(requireContext())


        etSearch?.doAfterTextChanged {
            presenter.findSub(it.toString())
            rv?.adapter?.notifyDataSetChanged()
        }

        bClear?.setOnClickListener { etSearch?.text?.clear() }
        return rootView
    }

    //attach for communication with Activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            fragmentSendDataListener = context as OnFragmentSendDataListener
        }catch(e:ClassCastException){
            throw ClassCastException("attach ListFragment failed")
        }
    }
}