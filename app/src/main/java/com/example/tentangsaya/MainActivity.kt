package com.example.tentangsaya

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tentangsaya.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleUser()
        setupKalkulatorButton()
        setupNoteButton()
    }

    private fun handleUser(){
        binding.profileDescription.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Tentang saya")
            builder.setMessage("Tentang saya : Saya adalah murid kelas 12")
            builder.setPositiveButton("OK"){dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
            Toast.makeText(applicationContext, "Tentang saya", Toast.LENGTH_LONG).show()
        }

        binding.logoutButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun setupKalkulatorButton() {
        binding.kalkulator.setOnClickListener {
            startActivity(Intent(this, KalkulatorActivity::class.java))
        }
    }

    private fun setupNoteButton() {
        binding.note.setOnClickListener {
            startActivity(Intent(this, NoteActivity::class.java))
        }
    }


}