// Class is a part of package
package com.example.booleangoes

//Lets you open other screens by loading them all in to ""memory""
import android.content.Intent
//Lets on create revive activity state
import android.os.Bundle
import android.view.View

import android.widget.Button
import android.widget.FrameLayout

import androidx.appcompat.app.AppCompatActivity

//Android info bar padding
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


//Main class, run on start.
class MainActivity : AppCompatActivity() {
//Creates the app environment on start
    override fun onCreate(savedInstanceState: Bundle?) {
        //Default page parameters are loaded
        super.onCreate(savedInstanceState)
        //Load page of classes namesake
        setContentView(R.layout.activity_main)
        //Load page layout by id
        val main: View = findViewById(R.id.main)

        //Finds the button id's and loads them in to a button variable
        val btnAddMember: Button = findViewById(R.id.btnAddMember)
        val btnMembersList: Button = findViewById(R.id.btnMembersList)
        val btnEditMember: Button = findViewById(R.id.btnEditMember)
        val btnRemoveMember: Button = findViewById(R.id.btnRemoveMember)
        val btnBack: FrameLayout = findViewById(R.id.btnBack)

        //A listener hook for when a button is pressed
        btnAddMember.setOnClickListener {
            //Creates a request to open new page
            val intent = Intent(this, CreateActivity::class.java)
            //Opens page created
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
            //Go back to previous page (unused on main)
            finish()
        }

        //Keeps the app below the Android info bar by listening for spacing
        ViewCompat.setOnApplyWindowInsetsListener(main) { v, insets -> val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
        //Object that stores framing data
        insets
        }
    }
}