package com.example.chatapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class UserAdapter(val context: Context, val userList: ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerviewdesign, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]

        holder.txtName.text = currentUser.name
        holder.txtEmail.text = currentUser.email

        holder.itemView.setOnClickListener{
            val intent = Intent(context,ChatActivity::class.java)
            val receiverUid = currentUser.uid
            intent.putExtra("name", currentUser.name)
            intent.putExtra("email", currentUser.email)
            intent.putExtra("uid",currentUser.uid)
            intent.putExtra("uid",receiverUid)
            context.startActivity(intent)

        }

    }


    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<TextView>(R.id.txtName)
        val txtEmail = itemView.findViewById<TextView>(R.id.txtEmail)
        /*val txtDate = itemView.findViewById<TextView>(R.id.txtDate)
        val txtTime = itemView.findViewById<TextView>(R.id.txtTime)*/

    }
}