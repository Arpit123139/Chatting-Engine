package com.example.chatting_engine

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

// we dont know which ViewHolder to return so we leave it as RecyclerView.ViewHolder
class MessageAdapter(val context: Context,val messageList:ArrayList<message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECIEVE=1
    val ITEM_SEND=2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //inflate the layout based on the condition
        if(viewType==1){
            //inflate recieve
            val view:View= LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            return MessageAdapter.RecieveViewHolder(view)
        }
        else{
            val view:View= LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return MessageAdapter.SentViewHolder(view)

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage=messageList[position]

        if(holder.javaClass == SentViewHolder::class.java)  { // this is a sent Message
            //do the stuff for the sentView Holder
            val viewHolder=holder as SentViewHolder
            viewHolder.sentmsg.text = currentMessage.message
        }else{
            val viewHolder=holder as RecieveViewHolder
            viewHolder.recievemsg.text = currentMessage.message
        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage=messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            // means we are sending the message so inflare sentView Holder
            return ITEM_SEND
        }else
            return ITEM_RECIEVE
    }

    override fun getItemCount(): Int {

        return messageList.size
    }

    class SentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sentmsg=itemView.findViewById<TextView>(R.id.txt_sent_message)

    }
    class RecieveViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){          // from here it goes to onBindView Holder
        val recievemsg=itemView.findViewById<TextView>(R.id.txt_recieve_message)
    }
}