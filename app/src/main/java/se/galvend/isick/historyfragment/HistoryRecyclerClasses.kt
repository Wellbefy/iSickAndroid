package se.galvend.isick.historyfragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
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
        event.displayDate()
        val sickText = holder?.itemView?.context?.getString(R.string.string_sjuk, event.displayDate())
        val vabText = holder?.itemView?.context?.getString(R.string.string_vab, event.displayDate())
        when {
            event.vab!! -> holder?.dateLabel?.text = sickText
            else -> holder?.dateLabel?.text = vabText
        }

        if(!event.reported!!) holder?.reportedLabel?.visibility = View.INVISIBLE
    }

}

class HistoryViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val nameLabel: TextView = view.findViewById(R.id.hRCnameLabel)
    val dateLabel: TextView = view.findViewById(R.id.hRCdateLabel)
    val reportedLabel: TextView = view.findViewById(R.id.reportedLabel)
}

class ItemTouch {

    fun createTouchHelper(context: Context, recyclerView: RecyclerView) {
        val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_forever_black_24dp)
        val deleteColor = ColorDrawable(ContextCompat.getColor(context, R.color.purple))
        val iconMargin = 8

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START){
            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                val index = viewHolder?.adapterPosition ?: -1
                if(index == -1) return

            }

            override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                if(c == null || viewHolder == null) return

                val itemView = viewHolder.itemView

                deleteColor.setBounds(itemView.left, itemView.top, itemView.right, itemView.bottom)
                deleteColor.draw(c)

                val iconWidth = deleteIcon.intrinsicWidth
                val iconHeight = deleteIcon.intrinsicHeight
                val left = itemView.right - iconMargin -  iconWidth
                val top = itemView.top + itemView.height/2 - iconHeight/2
                val right = left + iconWidth
                val bottom = top + iconHeight
                deleteIcon.setBounds(left, top, right, bottom)
                deleteIcon.draw(c)

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}