package com.github.yuukidoi.photoquestion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth// ...
    val TAG = "tagMain"
// Initialize Firebase Auth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        //Firebase Authentication
        auth = FirebaseAuth.getInstance()

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            performRegister()
        }


        //move to Login
        val toLoginText = findViewById<TextView>(R.id.text_to_login)
        toLoginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }


    private fun performRegister() {
        val name = editText_name.text.toString()
        val email = editText_email.text.toString()
        val password = editText_password.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please Enter", Toast.LENGTH_SHORT).show()
            return

        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success ${email} ${password} ")
                    val user = auth.currentUser
                    addDatabaseUser(name,email,password)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.${task.exception}",
                        Toast.LENGTH_LONG
                    ).show()
                    return@addOnCompleteListener
                }

                // ...
            }
        Log.d(TAG, "createUserWithEmail:success ${email} ${password} ")

    }

    private fun addDatabaseUser(name:String, email:String, password:String){
        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()

        val user = hashMapOf(
            "name" to name,
            "email" to email,
            "password" to password
        )

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener{
                Log.w(TAG, "Error adding document", it)
            }
    }
}