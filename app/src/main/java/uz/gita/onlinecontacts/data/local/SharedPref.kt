package uz.gita.onlinecontacts.data.local

import android.content.Context

class SharedPref(context: Context) {

    companion object {

        private var instance: SharedPref? = null

        fun init(context: Context) {
            instance = SharedPref(context)
        }

        fun getInstance(): SharedPref = instance!!

    }

    private val pref = context.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)

    var name: String
        set(value) = pref.edit().putString("NAME", value).apply()
        get() = pref.getString("NAME", "")!!


    var password: String
        set(value) = pref.edit().putString("PASSWORD", value).apply()
        get() = pref.getString("PASSWORD", "")!!


    var token: String
        set(value) = pref.edit().putString("TOKEN", value).apply()
        get() = pref.getString("TOKEN", "")!!


    var isLogedIn: Boolean
        set(value) = pref.edit().putBoolean("IS_LOGED_IN", value).apply()
        get() = pref.getBoolean("IS_LOGED_IN", false)

}