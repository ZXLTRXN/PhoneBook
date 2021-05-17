package com.example.phonebook.views.adapter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.phonebook.R
import com.example.phonebook.model.Subscriber
import com.example.phonebook.presenter.LogicPresenterImpl
import com.example.phonebook.views.viewInterfaces.OnFragmentSendDataListener


class ListAdapter(val presenter:LogicPresenterImpl, val sendData:OnFragmentSendDataListener):RecyclerView.Adapter<ListAdapter.ViewHolder>(){

    //filling one item
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val view = view// context from here (inflater.inflate in onCreateViewHolder)

        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvPhone = view.findViewById<TextView>(R.id.tv_phone)
        val image = view.findViewById<ImageView>(R.id.iv)
        val bCall = view.findViewById<ImageButton>(R.id.bCall)
        val bEdit = view.findViewById<ImageButton>(R.id.bEdit)
        val bDelete = view.findViewById<ImageButton>(R.id.bDelete)

        fun bind(listItem:Subscriber,position:Int){
            tvName.text = listItem.name
            tvPhone.text = listItem.pNum1
            image.setImageResource(R.drawable.ic_user_40)

            bCall.setOnClickListener(){
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:"+listItem.pNum1)
                view.context.startActivity(intent)

                try {
                    view.context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Log.e("ListAdapter","bad call intent")
                }
            }
            bEdit.setOnClickListener(){
                onEdit(position)
            }
            bDelete.setOnClickListener(){
                onDelete(position)
            }
        }
    }

    //create on item view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.subscriber_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return presenter.countSubs()
    }

    // run filling all recyclerView one by one
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var listItem = presenter.getSub(position)
        holder.bind(listItem,position)
    }


    fun onDelete(position:Int){
        presenter.deleteSub(position)
        notifyDataSetChanged()
    }

    fun onEdit(position:Int){
        sendData.onSendData(position.toString())
    }


}