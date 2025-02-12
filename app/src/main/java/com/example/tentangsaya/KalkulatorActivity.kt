package com.example.tentangsaya

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tentangsaya.databinding.ActivityKalkulatorBinding
import android.media.MediaPlayer


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
            binding.angka0.id -> {
                playSound(R.raw.click1)
                appendToInput("0")
            }
            binding.angka1.id -> {
                playSound(R.raw.click2)
                appendToInput("1")
            }
            binding.angka2.id -> {
                playSound(R.raw.click17)
                appendToInput("2")
            }
            binding.angka3.id -> {
                playSound(R.raw.click4)
                appendToInput("3")
            }
            binding.angka4.id -> {
                playSound(R.raw.click21)
                appendToInput("4")
            }
            binding.angka5.id -> {
                playSound(R.raw.click6)
                appendToInput("5")
            }
            binding.angka6.id -> {
                playSound(R.raw.click7)
                appendToInput("6")
            }
            binding.angka7.id -> {
                playSound(R.raw.click8)
                appendToInput("7")
            }
            binding.angka8.id -> {
                playSound(R.raw.click9)
                appendToInput("8")
            }
            binding.angka9.id -> {
                playSound(R.raw.click19)
                appendToInput("9")
            }
            binding.koma.id -> {
                playSound(R.raw.click11)
                appendToInput(".")
            }
            binding.tambah.id -> {
                playSound(R.raw.click12)
                appendToInput("+")
            }
            binding.kurang.id -> {
                playSound(R.raw.click13)
                appendToInput("-")
            }
            binding.kali.id -> {
                playSound(R.raw.click14)
                appendToInput("*")
            }
            binding.bagi.id -> {
                playSound(R.raw.click15)
                appendToInput("/")
            }
            binding.persen.id -> {
                playSound(R.raw.click18)
                appendToInput("%")
            }
            binding.hapus.id -> {
                playSound(R.raw.click3)
                removeLastCharacter()
            }
            binding.hapussemua.id -> {
                playSound(R.raw.click16)
                clearInput()
            }
            binding.samadengan.id -> {
                playSound(R.raw.click10)
                calculateResult()
            }
        }
        updateDisplay()
    }

    private fun appendToInput(value: String) {
        input += value
    }

    private fun removeLastCharacter() {
        if (input.isNotEmpty()) {
            input = input.substring(0, input.length - 1)
        }
    }

    private fun clearInput() {
        input = ""
    }

    private fun calculateResult() {
        try {
            val result = evaluateExpression(input)
            input = if (result.isNaN()) "Error" else result.toString()
        } catch (e: Exception) {
            input = "Error"
        }
        updateDisplay()
    }

    private fun evaluateExpression(expression: String): Double {
        try {
            val tokens = expression.split("(?<=[-+*/])|(?=[-+*/])".toRegex()).toMutableList()

            for (i in tokens.indices) {
                if (tokens[i].contains("%")) {
                    val number = tokens[i].replace("%", "").toDouble() / 100
                    tokens[i] = number.toString()
                }
            }

            var result = tokens[0].toDouble()
            var operator = ""

            for (i in 1 until tokens.size) {
                when (tokens[i]) {
                    "+", "-", "*", "/" -> operator = tokens[i]
                    else -> {
                        val num = tokens[i].toDouble()
                        result = when (operator) {
                            "+" -> result + num
                            "-" -> result - num
                            "*" -> result * num
                            "/" -> result / num
                            else -> num
                        }
                    }
                }
            }
            return result
        } catch (e: Exception) {
            return Double.NaN
        }
    }

    private fun updateDisplay() {
        binding.teks.text = input
    }

    private fun handleUser(){
        binding.logout.setOnClickListener{
            playSound(R.raw.click20)
            finish()
        }
    }


    private fun playSound(soundResId: Int) {
        val mediaPlayer = MediaPlayer.create(this, soundResId)
        mediaPlayer.setOnCompletionListener { it.release() }
        mediaPlayer.start()
    }
}
