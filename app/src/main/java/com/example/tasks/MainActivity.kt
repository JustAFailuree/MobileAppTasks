package com.example.tasks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tasks.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityMainBinding
    private val TAG = "msg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.textView.text = ""
        binding.textView2.text = ""
        binding.textView3.text= ""
        binding.progressBar.visibility = View.INVISIBLE

        binding.button.setOnClickListener{
            var tv1 = binding.textView
            if(tv1.text.isEmpty()){
                tv1.text = "zwykły tekst"
            }else{
                tv1.text=""
            }
        }

        binding.button2.setOnClickListener{
            binding.textView2.text="początek zadania 1"

            runBlocking {
                val deffered = async(Dispatchers.Default) {
                    doTask1()
                }
                binding.textView2.text="wynik ${deffered.await().toString()}"
            }



        }

        binding.button3.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                binding.button3.isEnabled = false
                binding.textView3.text=""
                binding.progressBar.visibility = View.VISIBLE
                binding.textView3.text = "Wynik: ${doTask3()}"
                binding.progressBar.visibility = View.INVISIBLE
                binding.button3.isEnabled = true
            }
        }

        binding.button4.setOnClickListener{
            startActivity(Intent(this@MainActivity, SecondActivity::class.java))
        }
    }

    private suspend fun doTask1() : String {
        Log.d(TAG, "rozpoczęcie zadania 1 ")
        delay(5000L)
        Log.d(TAG, "zakończenie zadania 1 ")
        return "zadanie 1"
    }

    private suspend fun doTask2() : String {
        Log.d(TAG, "rozpoczęcie zadania 2 ")
        delay(7000L)
        Log.d(TAG, "zakończenie zadania 2 ")
        return (0..100).random().toString()
    }

    private suspend fun doTask3() : String{
        Log.d(TAG, "rozpoczęcie zadania 3 ")
        var num = 25678999
        var result = countPi(num)
        Log.d(TAG, "zakończenie zadania 3 ")
        return result.toString()
    }


    fun countPi(num : Int) : Double{
        var pointsInCircle = 0
        for (i in 0 until num){
            val x = 2 * Math.random() - 1
            val y = 2 * Math.random() - 1
            if (x*x + y*y <= 1) {
                pointsInCircle += 1
            }
        }
        return 4.0 * pointsInCircle / num
    }
}