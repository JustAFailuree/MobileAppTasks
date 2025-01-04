package com.example.tasks

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tasks.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private var points: Int = 0
    private var mBound = false
    private var num = 0
    private var result1 : Int = 0
    private var result2 : Double = 0.0

    var myService: MyService? = null

    var myConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            val binder: MyService.LocalBinder = service as MyService.LocalBinder
            myService = binder.getService()
            mBound = true
            Toast.makeText(applicationContext, "Serwis podłączony", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            myService = null
            mBound = false
            Toast.makeText(applicationContext, "Serwis odłączony", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        if (!mBound) {
            this.bindService(
                Intent(this, MyService::class.java),
                myConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    override fun onStop() {
        super.onStop()
        if (mBound) {
            this.unbindService(myConnection)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecondBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.result1TV.text = ""
        binding.numberET.setText("0")

        binding.result2TV.text = ""
        binding.pointsEt.setText("0")

        binding.countBTN.setOnClickListener {
            num = Integer.parseInt(binding.numberET.text.toString())
            result1 = myService?.count(num) ?: 0
            binding.result1TV.text = "Wynik: $result1"
        }

        binding.PiBTN.setOnClickListener {
            num = Integer.parseInt(binding.pointsEt.text.toString())
            result2 = myService?.countPi(num) ?: 0.0
            binding.result2TV.text = "Pi: $result2"
        }

        binding.textView4.text = ""
        binding.editTextNumber.setText("0")

        // Obsługa przycisku do obliczania ciągu Fibonacciego
        binding.button5.setOnClickListener {
            val inputText = binding.editTextNumber.text.toString()

            if (inputText.isNotEmpty()) {
                val n = inputText.toInt()
                val fibonacciSequence = generateFibonacci(n)
                binding.textView4.text = "Fibonacci: ${fibonacciSequence.joinToString(", ")}"
            } else {
                Toast.makeText(this, "Wpisz liczbę!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Funkcja generująca ciąg Fibonacciego
    private fun generateFibonacci(n: Int): List<Int> {
        val fibSequence = mutableListOf<Int>()
        for (i in 0 until n+1) {
            fibSequence.add(fibonacci(i))
        }
        return fibSequence
    }

    // Funkcja rekurencyjna do obliczania n-tego elementu ciągu Fibonacciego
    private fun fibonacci(n: Int): Int {
        if (n <= 1) return n
        return fibonacci(n - 1) + fibonacci(n - 2)
    }
}
