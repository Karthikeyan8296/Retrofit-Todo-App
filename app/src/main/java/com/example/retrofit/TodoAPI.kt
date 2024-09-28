package com.example.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


//all the function to access the API will be defines in this file//
//get, update, delete - like these functions will be declared here//
interface TodoAPI {

    //Response is used to return the values from the json//
    //baseURL ku aprom vara part uh ethula mention pannaum//
    @GET("/todos")
    suspend fun  getTodos(): Response<List<Todo>>

}