package com.example.phonebook.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.phonebook.views.fragment.FragmentFactoryImpl

import com.example.phonebook.R
import com.example.phonebook.presenter.LogicPresenterImpl
import com.example.phonebook.views.fragment.EditorFragment
import com.example.phonebook.views.fragment.ListFragment
import com.example.phonebook.views.viewInterfaces.ILogicView
import com.example.phonebook.views.viewInterfaces.OnFragmentSendDataListener
import java.io.File


class MainActivity : AppCompatActivity(), ILogicView,OnFragmentSendDataListener {
    private val presenter = LogicPresenterImpl()

    private var listFr:Fragment? = null

    private var  mAdd:MenuItem? = null
    private var  mDelete:MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        //necessarily before super.onCreate
        supportFragmentManager.fragmentFactory = FragmentFactoryImpl(presenter)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.attachView(this)

        //creating a fragment with non null constructor
        listFr = supportFragmentManager.fragmentFactory
                .instantiate(classLoader, ListFragment::class.java.name)

        //adding fragment in activity
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.frg_container, listFr!!)
        fm.commit()
    }

    override fun onPause() {
        super.onPause()

        presenter.saveData()
    }

    //menu in toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)

        mAdd = menu?.findItem(R.id.m_add)
        mDelete = menu?.findItem(R.id.m_delete)

        return super.onCreateOptionsMenu(menu)
    }

    //toolbar listener
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.m_add){
            startEditorFragment(null)
        }

        if(item.itemId == R.id.m_delete){
            if(getCurrentFrg() is ListFragment) {
                presenter.deleteAll()
                val listFrCasted = listFr as ListFragment
                listFrCasted.rv?.adapter?.notifyDataSetChanged()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getCurrentFrg():Fragment? = supportFragmentManager.findFragmentById(R.id.frg_container)

    private fun startEditorFragment(data:String?){
        val editFr = supportFragmentManager.fragmentFactory
                .instantiate(classLoader, EditorFragment::class.java.name)

        if(data != null){
            editFr.arguments = bundleOf(Pair("position",data))
        }

        val curFrg = getCurrentFrg()

        if(curFrg !is EditorFragment) {
            var fm = supportFragmentManager.beginTransaction()
            fm.replace(R.id.frg_container, editFr)
            fm.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            if(curFrg is ListFragment)
                fm.addToBackStack(null)
            fm.commit()
        }
    }

    override fun showSuccess() {
        Toast.makeText(applicationContext, getString(R.string.success), Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: Int) {
        Toast.makeText(applicationContext, getString(message), Toast.LENGTH_SHORT).show()
    }

    override fun getFileDirectory(): File {
        return this.filesDir
    }

    override fun onSendData(data: String) {
        startEditorFragment(data)
    }



}