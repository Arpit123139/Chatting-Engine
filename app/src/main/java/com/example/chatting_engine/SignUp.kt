package com.example.chatting_engine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.chatting_engine.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.sign


class SignUp : AppCompatActivity() {

    private lateinit var edtName:EditText
    private lateinit var edtEmail:EditText
    private lateinit var edtPassword:EditText
    private lateinit var signup:Button

    private lateinit var mAuth:FirebaseAuth
    private lateinit var mDbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()

        mAuth= FirebaseAuth.getInstance()
        edtName=findViewById(R.id.name)
        edtEmail=findViewById(R.id.emailSignup)
        edtPassword=findViewById(R.id.passwordSignup)
        signup=findViewById(R.id.signupbtn)




        signup.setOnClickListener{
            val email=edtEmail.text.toString()
            val password=edtPassword.text.toString()
            val name=edtName.text.toString()
            signupMethod(name,email,password)
        }

    }

    private fun signupMethod(name:String,email:String,password:String){

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //Add user to the dataBase
                    addUserToDataBase(name,email,mAuth.currentUser?.uid!!)
                    // Sign in success, update UI with the signed-in user's information
                    val intent=Intent(this@SignUp,MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this,"Signup Not Successfull",Toast.LENGTH_SHORT).show()

                }
            }

    }

    private fun addUserToDataBase(name: String, email: String, uid: String){

        mDbRef=FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name,email,uid))

    }
}