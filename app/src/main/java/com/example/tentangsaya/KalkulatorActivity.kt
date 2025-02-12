package com.example.tentangsaya

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tentangsaya.databinding.ActivityKalkulatorBinding
import java.util.Stack

class KalkulatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKalkulatorBinding
    private var input: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKalkulatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtons()
        handleUser()
    }

    private fun setupButtons() {
        val buttons = listOf(
            binding.angka0, binding.angka1, binding.angka2, binding.angka3,
            binding.angka4, binding.angka5, binding.angka6, binding.angka7,
            binding.angka8, binding.angka9, binding.koma, binding.tambah,
            binding.kurang, binding.kali, binding.bagi, binding.persen,
            binding.hapus, binding.hapussemua, binding.samadengan
        )

        for (button in buttons) {
            button.setOnClickListener { onButtonClick(it) }
        }
    }

    private fun onButtonClick(view: View) {
        when (view.id) {
            binding.angka0.id -> appendToInput("0", R.raw.click1)
            binding.angka1.id -> appendToInput("1", R.raw.click2)
            binding.angka2.id -> appendToInput("2", R.raw.click17)
            binding.angka3.id -> appendToInput("3", R.raw.click4)
            binding.angka4.id -> appendToInput("4", R.raw.click21)
            binding.angka5.id -> appendToInput("5", R.raw.click6)
            binding.angka6.id -> appendToInput("6", R.raw.click7)
            binding.angka7.id -> appendToInput("7", R.raw.click8)
            binding.angka8.id -> appendToInput("8", R.raw.click9)
            binding.angka9.id -> appendToInput("9", R.raw.click19)
            binding.koma.id -> appendToInput(".", R.raw.click11)
            binding.tambah.id -> appendToInput("+", R.raw.click12)
            binding.kurang.id -> appendToInput("-", R.raw.click13)
            binding.kali.id -> appendToInput("*", R.raw.click14)
            binding.bagi.id -> appendToInput("/", R.raw.click15)
            binding.persen.id -> appendToInput("%", R.raw.click18)
            binding.hapus.id -> removeLastCharacter(R.raw.click3)
            binding.hapussemua.id -> clearInput(R.raw.click16)
            binding.samadengan.id -> calculateResult(R.raw.click10)
        }
        updateDisplay()
    }

    private fun appendToInput(value: String, soundResId: Int) {
        playSound(soundResId)
        input += value
    }

    private fun removeLastCharacter(soundResId: Int) {
        playSound(soundResId)
        if (input.isNotEmpty()) {
            input = input.substring(0, input.length - 1)
        }
    }

    private fun clearInput(soundResId: Int) {
        playSound(soundResId)
        input = ""
    }

    private fun calculateResult(soundResId: Int) {
        playSound(soundResId)
        try {
            val result = evaluateExpression(input)
            input = if (result.isNaN()) "Error" else result.toString()
        } catch (e: Exception) {
            input = "Error"
        }
        updateDisplay()
    }

    private fun evaluateExpression(expression: String): Double {
        return try {
            val postfix = infixToPostfix(expression)
            evaluatePostfix(postfix)
        } catch (e: Exception) {
            Double.NaN
        }
    }

    private fun infixToPostfix(expression: String): List<String> {
        val output = mutableListOf<String>()
        val operators = Stack<Char>()
        var number = ""

        for (char in expression) {
            when {
                char.isDigit() || char == '.' -> number += char
                char == '%' -> {
                    if (number.isNotEmpty()) {
                        output.add((number.toDouble() / 100).toString())
                        number = ""
                    }
                }

                "+-*/".contains(char) -> {
                    if (number.isNotEmpty()) {
                        output.add(number)
                        number = ""
                    }
                    while (operators.isNotEmpty() && precedence(operators.peek()) >= precedence(char)) {
                        output.add(operators.pop().toString())
                    }
                    operators.push(char)
                }
            }
        }

        if (number.isNotEmpty()) {
            output.add(number)
        }
        while (operators.isNotEmpty()) {
            output.add(operators.pop().toString())
        }

        return output
    }

    private fun evaluatePostfix(postfix: List<String>): Double {
        val stack = Stack<Double>()

        for (token in postfix) {
            when {
                token.toDoubleOrNull() != null -> stack.push(token.toDouble())
                "+-*/".contains(token) -> {
                    val b = stack.pop()
                    val a = stack.pop()
                    stack.push(
                        when (token) {
                            "+" -> a + b
                            "-" -> a - b
                            "*" -> a * b
                            "/" -> a / b
                            else -> 0.0
                        }
                    )
                }
            }
        }
        return if (stack.isNotEmpty()) stack.pop() else Double.NaN
    }

    private fun precedence(op: Char): Int {
        return when (op) {
            '+', '-' -> 1
            '*', '/' -> 2
            else -> 0
        }
    }

    private fun updateDisplay() {
        binding.teks.text = input
    }

    private fun handleUser() {
        binding.logout.setOnClickListener {
            finish()
        }
    }

    private fun playSound(soundResId: Int) {
        val mediaPlayer = MediaPlayer.create(this, soundResId)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { mp ->
            mp.release()
        }
    }
}