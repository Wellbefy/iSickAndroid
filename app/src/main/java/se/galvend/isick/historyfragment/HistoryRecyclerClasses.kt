package se.galvend.isick.historyfragment

import android.app.usage.UsageEvents
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import se.galvend.isick.R
import se.galvend.isick.classes.Event

/**
 * Created by dennisgalven on 2018-02-13.
 */
class HistoryAdapter: RecyclerView.Adapter<HistoryViewHolder>() {
    var events : List<Event> = emptyList()

    override fun getItemCount(): Int = events.count()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.history_recycler_cell, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder?, position: Int) {
        val event = events[position]
        holder?.nameLabel?.text = event.name

        when {
            event.vab!! -> holder?.dateLabel?.text = "${event.date.toString()} - VAB"
            else -> holder?.dateLabel?.text = "${event.date.toString()} - SJUK"
        }

        if(!event.reported!!) holder?.reportedLabel?.visibility = View.INVISIBLE
    }

}
class HistoryViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val nameLabel: TextView = view.findViewById(R.id.hRCnameLabel)
    val dateLabel: TextView = view.findViewById(R.id.hRCdateLabel)
    val reportedLabel: TextView = view.findViewById(R.id.reportedLabel)
}