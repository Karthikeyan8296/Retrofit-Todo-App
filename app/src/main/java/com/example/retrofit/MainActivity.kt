package com.example.retrofit

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofit.databinding.ActivityMainBinding
import com.example.retrofit.databinding.ItemTodoBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingTodoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRecyclerView()

        //Creating an coroutine//
        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible = true
            val response = try {
                RetrofitInstance.api.getTodos()
            } catch (e: IOException) {
                Log.d(TAG, "IOException, you might not have internet connection")
                Toast.makeText(this@MainActivity, "Make sure you have network connection", Toast.LENGTH_LONG).show()
                binding.progressBar.isVisible =  true
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.d(TAG, "HttpException, unexpected response")
                binding.progressBar.isVisible =  false
                return@launchWhenCreated
            }

            //If the response is successful//
            if (response.isSuccessful && response.body() != null){
                bindingTodoAdapter.todos = response.body()!!
            }
            else{
                Log.d(TAG, "Response not successful")
            }
            binding.progressBar.isVisible =  false
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setUpRecyclerView() = binding.rvTODOS.apply {
        bindingTodoAdapter = TodoAdapter()
        adapter = bindingTodoAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)
    }
}