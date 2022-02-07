package uz.gita.onlinecontacts.data

import android.content.Context
import android.webkit.WebChromeClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.gita.onlinecontacts.app.App
import uz.gita.onlinecontacts.data.local.SharedPref

object Network {

    private val pref: SharedPref = SharedPref.getInstance()

    val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("token", SharedPref.getInstance().token)
                .build()
            it.proceed(request)
        }
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://1792-213-230-76-83.ngrok.io")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

//    private val BASE_URL = "https://d63e-84-54-78-210.ngrok.io"

    //

//
//    val retrofit = Retrofit.Builder()
//        .client(client(pref, App.instance))
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
}

/*
object ApiClient {

    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("token", MyPref.getInstance().accessToken)
                .build()
            it.proceed(request)
        }

        .authenticator(TokenAuthenticator(MyPref.getInstance()))
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("http://102b-37-110-215-205.ngrok.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    class TokenAuthenticator(private val localStorage: MyPref) : Authenticator {

        private val retrofit = Retrofit.Builder()
            .client(OkHttpClient.Builder().addInterceptor(loggingInterceptor).build())
            .baseUrl("http://102b-37-110-215-205.ngrok.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        private val authApi = retrofit.create(AuthApi::class.java)

        override fun authenticate(route: Route?, response: Response): Request? {

            val refreshResponse = authApi.refreshEachAction(localStorage.refreshToken, PhoneRequest(localStorage.phone)).execute()

            if (refreshResponse.code() == 401) return null
            if (refreshResponse.body() == null) return null

            val tokenData = refreshResponse.body()!!.data

            localStorage.accessToken = tokenData!!.accessToken
            localStorage.refreshToken = tokenData.refreshToken

            return response.request
                .newBuilder()
                .removeHeader("token")
                .addHeader("token", tokenData.accessToken)
                .build()
        }

    }

}


*/