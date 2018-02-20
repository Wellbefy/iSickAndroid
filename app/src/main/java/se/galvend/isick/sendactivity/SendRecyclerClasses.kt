package se.galvend.isick.sendactivity

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import se.galvend.isick.R
import java.text.DateFormat
import java.util.*

/**
 * Created by dennisgalven on 2018-02-20.
 * Send mail recycler viewholder and adapter
 */
class SendMailAdapter: RecyclerView.Adapter<SendMailViewHolder>() {
    var personNumber: String? = ""
    var name: String? = ""
    override fun getItemCount(): Int = 2

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SendMailViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.send_cell, parent, false)
        return SendMailViewHolder(view)
    }


    override fun onBindViewHolder(holder: SendMailViewHolder?, position: Int) {
        val date = Date()
        val formatter = DateFormat.getDateInstance(DateFormat.DEFAULT)
        val text = holder?.itemView?.context?.getString(R.string.sickmail, formatter.format(date), name, personNumber)
        holder?.mailLabel?.text = text
    }

}
class SendMailViewHolder(view: View): RecyclerView.ViewHolder(view){
    val mailLabel: TextView = view.findViewById(R.id.mailTextLabel)
}