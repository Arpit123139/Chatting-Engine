package com.example.chatting_engine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter:UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth=FirebaseAuth.getInstance()
        val uid=mAuth.uid
        mDbRef=FirebaseDatabase.getInstance().getReference()

        userList= ArrayList()           // Add the user to the ArrayList by getting it from the database

        userRecyclerView=findViewById(R.id.userRecyclerView)
        adapter=UserAdapter(this,userList)

        userRecyclerView.layoutManager=LinearLayoutManager(this)
        userRecyclerView.adapter=adapter            // setting the adapter


        mDbRef.child("user").addValueEventListener(object:ValueEventListener{


            override fun onDataChange(snapshot: DataSnapshot) {
                //snapshot is use to get the data from the dataBase
                userList.clear()            // we have to clear this because whenever there is a change in data it is called so to prevent repeating the value we do this
                for(postSnapshot in snapshot.children){
                    val currentUser=postSnapshot.getValue(User::class.java)
                    if(currentUser?.uid==uid)
                        continue;
                    userList.add(currentUser!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })






    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.logout){
            mAuth.signOut()
            val intent= Intent(this,Login::class.java)
            startActivity(intent)


            return true;
        }
        return false
    }
}