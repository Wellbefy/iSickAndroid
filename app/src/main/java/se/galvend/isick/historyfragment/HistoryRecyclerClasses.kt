package se.galvend.isick.historyfragment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import se.galvend.isick.R

/**
 * Created by dennisgalven on 2018-02-13.
 */
class HistoryAdapter: RecyclerView.Adapter<HistoryViewHolder>() {
    override fun getItemCount(): Int = 2

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.history_recycler_cell, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder?, position: Int) {
        holder?.nameLabel?.text = "Alice Galvén"
        holder?.dateLabel?.text = "28 feb. 2018 - VAB"
        holder?.reportedLabel?.text = "Anmält till FK"
    }

}
class HistoryViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val nameLabel: TextView = view.findViewById(R.id.hRCnameLabel)
    val dateLabel: TextView = view.findViewById(R.id.hRCdateLabel)
    val reportedLabel: TextView = view.findViewById(R.id.reportedLabel)
}