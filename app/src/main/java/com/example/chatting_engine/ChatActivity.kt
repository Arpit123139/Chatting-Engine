package com.example.chatting_engine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageBox:EditText
    private lateinit var sendButton:ImageButton
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList:ArrayList<message>

    private lateinit var mDbRef:DatabaseReference

    //creates a unique room for the pair of user one sender and reciever so that the msg are not visible to every User
    var receiverRoom:String?=null
    var senderRoom:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //Recieving The Intent ...................................as we have passed the parameter
        val name=intent.getStringExtra("name")
        val recieveruid=intent.getStringExtra("uid")
        val senderuid= FirebaseAuth.getInstance().currentUser?.uid                // this is a method in the Firebase which gives the currentUser
        mDbRef=FirebaseDatabase.getInstance().getReference()

        senderRoom=recieveruid+senderuid
        receiverRoom=senderuid+recieveruid

        supportActionBar?.title=name

        recyclerView=findViewById(R.id.ChatRecyclerView)
        messageBox=findViewById(R.id.messageBox)
        sendButton=findViewById(R.id.sendButton)
        messageList= ArrayList()
        messageAdapter= MessageAdapter(this,messageList)

        recyclerView.layoutManager=LinearLayoutManager(this)
        recyclerView.adapter=messageAdapter

        //Adding the msg to the DataBase
        sendButton.setOnClickListener{
            val message=messageBox.text.toString()
            val messageObject=message(message,senderuid)

            //create a node of chat
            mDbRef.child("chats").child(senderRoom!!).child("messages").push().setValue(messageObject).addOnSuccessListener {
                mDbRef.child("chats").child(receiverRoom!!).child("messages").push().setValue(messageObject)

            }
            messageBox.setText("")

        }

        //logic for adding data to Recycler View

        mDbRef.child("chats").child(senderRoom!!).child("messages").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                messageList.clear()

                for(postSnapshot in snapshot.children){
                   val message=postSnapshot.getValue(message::class.java)
                    messageList.add(message!!)
                }

                messageAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }
}