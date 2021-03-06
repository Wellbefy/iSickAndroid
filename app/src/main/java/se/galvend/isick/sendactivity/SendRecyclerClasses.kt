package se.galvend.isick.sendactivity

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import se.galvend.isick.R
import se.galvend.isick.classes.MailAndMessage

/**
 * Created by dennisgalven on 2018-02-20.
 * Send mail recycler viewholder and adapter
 */
class SendMailAdapter: RecyclerView.Adapter<SendMailViewHolder>() {
    var messages = listOf<MailAndMessage>()

    override fun getItemCount(): Int = messages.count()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SendMailViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.send_cell, parent, false)
        return SendMailViewHolder(view)
    }

    override fun onBindViewHolder(holder: SendMailViewHolder?, position: Int) {
        val message = (messages[position] as MailAndMessage).message
        holder?.mailLabel?.text = message
    }

}

class SendMailViewHolder(view: View): RecyclerView.ViewHolder(view){
    val mailLabel: TextView = view.findViewById(R.id.mailTextLabel)
}