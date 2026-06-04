package com.example.booleangoes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.graphics.Insets;

//Main, run on start.
class MainActivity : AppCompatActivity() {
//Creates the app environment on start
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val main: View = findViewById(R.id.main)

        val btnAddMember: Button = findViewById(R.id.btnAddMember)
        val btnMembersList: Button = findViewById(R.id.btnMembersList)
        val btnEditMember: Button = findViewById(R.id.btnEditMember)
        val btnRemoveMember: Button = findViewById(R.id.btnRemoveMember)
        val btnBack: FrameLayout = findViewById(R.id.btnBack)

        btnAddMember.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
        }

        btnMembersList.setOnClickListener {
            val intent = Intent(this, ViewActivity::class.java)
            startActivity(intent)
        }

        btnEditMember.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }

        btnRemoveMember.setOnClickListener {
            val intent = Intent(this, RemoveActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            finish()
        }

    //Keeps the app below the Android info bar
    ViewCompat.setOnApplyWindowInsetsListener(main) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

        v.setPadding(
            0,
            systemBars.top,
            0,
            0
        )

        insets
    }
    }
}