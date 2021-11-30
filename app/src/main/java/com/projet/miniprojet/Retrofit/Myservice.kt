package com.projet.miniprojet.Retrofit
import retrofit2.http.POST
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query
import com.projet.miniprojet.User


interface Myservice {

    @POST("users/register") //methode 2
    @FormUrlEncoded
    fun register(
                 @Field("email")email:String,
                 @Field("password")password:String
    ):Call<User>

    companion object {

        var BASE_URL = "http://10.0.2.2:3000/"

        fun create() : Myservice {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(Myservice::class.java)
        }
    }

}