package com.example.phonebook.views.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.phonebook.presenter.LogicPresenterImpl

class FragmentFactoryImpl(val presenter:LogicPresenterImpl): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            ListFragment::class.java.name -> ListFragment(presenter)
            EditorFragment::class.java.name -> EditorFragment(presenter)
            else -> super.instantiate(classLoader, className)
        }
    }
}