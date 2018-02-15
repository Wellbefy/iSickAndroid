package se.galvend.isick.historyfragment

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import se.galvend.isick.R
import se.galvend.isick.classes.Event
import se.galvend.isick.classes.UserViewModel

/**
 * Created by dennisgalven on 2018-02-13.
 */
class HistoryAdapter: RecyclerView.Adapter<HistoryViewHolder>() {
    companion object {
        val TAG = "HistoryAdapter"
    }
    var events : List<Event> = emptyList()

    override fun getItemCount(): Int {
        var removed = 0
        events.forEach {
            if(it.removed) removed += 1
        }

        return events.count() - removed
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.history_recycler_cell, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder?, position: Int) {
        val event = events[position]

        Log.d(TAG, event.toString())

        holder?.nameLabel?.text = event.name
        event.displayDate()
        val sickText = holder?.itemView?.context?.getString(R.string.string_sjuk, event.displayDate())
        val vabText = holder?.itemView?.context?.getString(R.string.string_vab, event.displayDate())
        when {
            event.vab!! -> holder?.dateLabel?.text = vabText
            else -> holder?.dateLabel?.text = sickText
        }

        when {
            event.reported!! -> holder?.reportedLabel?.visibility = View.VISIBLE
            else -> holder?.reportedLabel?.visibility = View.INVISIBLE
        }
    }

}

class HistoryViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val nameLabel: TextView = view.findViewById(R.id.hRCnameLabel)
    val dateLabel: TextView = view.findViewById(R.id.hRCdateLabel)
    val reportedLabel: TextView = view.findViewById(R.id.reportedLabel)
}

class ItemTouch {

    companion object {
        val TAG = "ItemTouch"
    }

    fun createTouchHelper(context: Context, recyclerView: RecyclerView, viewModel: ViewModel) {
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

//                val event = (recyclerView.adapter as HistoryAdapter).events[index]
//                (viewModel as UserViewModel).removeEvent(event.id!!)
                (viewModel as UserViewModel).events.value!![index].removed = true
                presentSnackBar(recyclerView, index, viewModel)
                recyclerView.adapter.notifyItemRemoved(index)
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

    fun presentSnackBar(recyclerView: RecyclerView, position: Int, viewModel: ViewModel) {
        val snackBar = Snackbar.make(recyclerView, "", 5000)
        val snackViewModel = viewModel as UserViewModel
        val snackEvent = snackViewModel.events.value!![position]

        snackBar.setAction("ÅNGRA?", {})

        snackBar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                when(event) {
                    DISMISS_EVENT_ACTION -> {
                        snackEvent.removed = false
                        recyclerView.adapter.notifyDataSetChanged()
                    }
                    DISMISS_EVENT_TIMEOUT -> {
                        viewModel.removeEvent(snackEvent.id)
                    }
                }
            }
        })
        snackBar.show()
    }
}