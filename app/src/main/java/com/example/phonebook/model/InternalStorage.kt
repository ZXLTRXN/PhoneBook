package com.example.phonebook.model

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import java.io.*
import java.lang.Exception
import java.lang.ref.WeakReference


class InternalStorage(private val fileDir:File):IRepository {
    private val fileName = "subscribers"

    override fun read(): List<Subscriber> {
        val userStorage = File(fileDir,fileName)
        val gson:Gson = Gson()
        var data:String =""
        try {
            if (!userStorage.isFile) {
                userStorage.createNewFile()
            }

            val fis = FileInputStream(userStorage)
            val isr = InputStreamReader(fis, "utf-8")
            val br = BufferedReader(isr)
            data += br.readLine()
            br.close()
            isr.close()
            fis.close()
        }catch(e:Exception){
            Log.e("int Storage", e.toString())
        }

        var list:List<Subscriber>? = null

        try{
            if(data != "")
                list = gson.fromJson<List<Subscriber>>(data,object: TypeToken<List<Subscriber>>() {}.type)
        }catch(e:JSONException){
            Log.e("int Storage", e.toString())
        }
        return list ?: listOf<Subscriber>()
    }

    override fun write(list: List<Subscriber>): Int {
        val gson:Gson = Gson()
        val data:String = gson.toJson(list)
        try {
            val userStorage = File(fileDir, fileName)
            val fos = FileOutputStream(userStorage)
            val osw = OutputStreamWriter(fos, "utf-8")
            osw.write(data)
            osw.close()
            fos.close()
        }catch (e:Exception){
            Log.e("int Storage", e.toString())
        }
        return 0
    }
}
