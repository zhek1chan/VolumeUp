package com.example.playlistmaker.history

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//Для того, чтобы находить уже присутствующие элементы в списке, используем map, в котором ключом является значение элемента, а значением - ссылка на родитель элемента в связном списке.

//class for elements of the search
class Chain<T>(val value: T, var prev: Chain<T>? = null, var next: Chain<T>? = null) {
    override fun toString(): String {
        return "$value"
    }
}


class LinkedRepository<T>(private val maxSize: Int) {
    private var head: Chain<T>? = null
    private var tail: Chain<T>? = null
    private var size: Int = 0
    private var map: MutableMap<T, Chain<T>?> = HashMap()

    fun getSize(): Int {
        return this.size
    }

    //LIFO
    fun add(item: T) {
        Log.d("CurrentID", "Adding $item")
        if (item in map) { // сначала надо проверить, есть ли элемент в списке
            Log.d("CurrentID", "Key in the map: ${this.getMapKeys()}")
            if (map[item] == null) {
                this.head = this.removeHead()
            } else {
                val parentChain = map[item]
                parentChain?.next = map[item]?.next?.next
                if (parentChain?.next != null) {
                    parentChain.next?.prev = parentChain
                } else {
                    this.tail = parentChain
                }
                map.remove(item)
            }
            this.size-- // если элемент был в списке, то уменьшаем размер
        }
        var newChain = Chain<T>(item)
        if (this.size == this.maxSize) { // вариант, когда размер списка превышает максимум
            map.remove(head?.value)
            var head = this.removeHead()
            this.size--
            map[head?.value!!] = null
            var tail = this.tail
            map.put(item, tail)
            tail?.next = newChain
            newChain.prev = tail
            this.tail = newChain
        } else if (this.size == 0) { // вариант, когда список пуст
            this.head = newChain
            this.tail = newChain
            map.put(item, null)
        } else { // вариант, когда список не пуст и меньше максимума
            var tail = this.tail
            tail?.next = newChain
            newChain.prev = tail
            map.put(item, tail)
            this.tail = newChain
        }
        this.size++
        Log.d("CurrentID", "New size - ${this.getSize()}")
    }

    fun removeHead(): Chain<T>? {
        head = this.head
        if (head?.next == null) {
            this.map.remove(head?.value)
            this.tail = null
            this.head = null
            return null
        } else {
            var head = head
            this.map.remove(head?.value)
            this.head = head?.next
            this.head?.prev = null
            return this.head
        }
    }

    fun get(reversed: Boolean): ArrayList<T>? {
        val list = ArrayList<T>()
        var node = if (reversed) tail else head
        while (node != null) {
            list.add(node.value)
            node = if (reversed) node.prev else node.next
        }
        return list
    }

    fun clear() {
        this.head = null
        this.tail = null
        this.size = 0
        this.map = HashMap()
    }

    fun getMapKeys(): String {
        return this.map.keys.toString()
    }

    fun restoreFromSharedPreferences(prefs_name: String, key: String, context: Context) {
        val gson = Gson()
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE)
        this.clear()
        val json = sharedPreferences.getString(key, "[]")
        if ((json != "null") || (json != "[]")) {
            val type = object : TypeToken<List<T>>() {}.type
            val list: List<T> = gson.fromJson(json, type)
            for (item in list) {
                add(item as T)
            }
        } else {
            this.clearSharedPreferences(prefs_name, key, context)
        }
    }

    fun saveToSharedPreferences(prefs_name: String, key: String, context: Context) {
        val gson = Gson()
        val json = gson.toJson(this.get(reversed = false))
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, json).apply()
    }

    fun clearSharedPreferences(prefs_name: String, key: String, context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE)
        sharedPreferences.edit().remove(key).apply()
    }

    override fun toString(): String {
        return "${this.head}, ${this.tail}, ${this.size}"
    }

    fun toJson(): String {
        val gson = Gson()
        return gson.toJson(this.get(reversed = true))
    }

}