package com.safecircleapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class ContactsAdapter(
    private val contacts: List<Contact>,
    private val userId: String,
    private val selectedContacts: MutableSet<String>
) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contactName: TextView = view.findViewById(R.id.contactName)
        val contactNumber: TextView = view.findViewById(R.id.contactNumber)
        val toggleSwitch: Switch = view.findViewById(R.id.toggleSwitch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.contactName.text = contact.name
        holder.contactNumber.text = contact.number

        // Set the toggle switch based on the selected contacts
        holder.toggleSwitch.setOnCheckedChangeListener(null)
        holder.toggleSwitch.isChecked = selectedContacts.contains(contact.number) // Update switch state

        holder.toggleSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Add contact to Firebase
                addEmergencyContactToFirebase(userId, contact)
                // Add to selected contacts
                selectedContacts.add(contact.number)
            } else {
                // Remove contact from Firebase
                removeEmergencyContactFromFirebase(userId, contact)
                // Remove from selected contacts
                selectedContacts.remove(contact.number)
            }
        }
    }

    override fun getItemCount(): Int = contacts.size

    private fun addEmergencyContactToFirebase(userId: String, contact: Contact) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
            .child(userId).child("emergency_contacts").child(contact.number)
        databaseReference.setValue(contact)
    }

    private fun removeEmergencyContactFromFirebase(userId: String, contact: Contact) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
            .child(userId).child("emergency_contacts").child(contact.number)
        databaseReference.removeValue()
    }
}