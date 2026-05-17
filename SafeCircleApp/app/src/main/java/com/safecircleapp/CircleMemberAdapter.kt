package com.safecircleapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class CircleMemberAdapter(
    private val context: Context,
    private val memberList: List<CircleMember>
) : RecyclerView.Adapter<CircleMemberAdapter.MemberViewHolder>() {

    class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePicture: CircleImageView = itemView.findViewById(R.id.profile_picture)
        val memberName: TextView = itemView.findViewById(R.id.member_name)
        val memberEmail: TextView = itemView.findViewById(R.id.member_email)
        val statusIndicator: ImageView = itemView.findViewById(R.id.status_indicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_circle_member, parent, false)
        return MemberViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val circleMember = memberList[position]

        holder.memberName.text = circleMember.name
        holder.memberEmail.text = circleMember.email

        // Load the profile picture using Glide
        Glide.with(context)
            .load(circleMember.profilePictureUrl)
            .placeholder(R.drawable.placeholder_image)  // Placeholder image
            .into(holder.profilePicture)

        // Set the sharing status indicator
        if (circleMember.isSharing == "true") {
            holder.statusIndicator.setImageResource(R.drawable.green_dot)
        } else {
            holder.statusIndicator.setImageResource(R.drawable.red_dot)
        }
    }

    override fun getItemCount() = memberList.size
}