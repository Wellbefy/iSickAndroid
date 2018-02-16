package se.galvend.isick.settingsfragment

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import se.galvend.isick.R
import se.galvend.isick.classes.Kid

/**
 * Created by dennisgalven on 2018-02-16.
 */
class EditKidRecyclerAdapter: RecyclerView.Adapter<EditKidViewHolder>() {
    companion object {
        const val TAG = "EditKidRecyclerAdapter"
    }
    var kids : List<Kid> = emptyList()

    override fun getItemCount(): Int = kids.count()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EditKidViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.edit_kid_cell, parent, false)
        return EditKidViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EditKidViewHolder?, position: Int) {
        val kid = kids[position]
        holder?.nameLabel?.text = "${kid.name}: ${kid.personNumber}"
        holder?.emailLabel?.text = kid.email
        holder?.editButton?.setOnClickListener {
            Log.d(TAG, kid.name)
        }
    }

}

class EditKidViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val nameLabel: TextView = view.findViewById(R.id.editKidNameLabel)
    val emailLabel: TextView = view.findViewById(R.id.editKidEmailLabel)
    val editButton: ImageButton = view.findViewById(R.id.editKidCellButton)
}