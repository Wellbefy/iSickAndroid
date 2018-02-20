package se.galvend.isick.reportfragment

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import se.galvend.isick.R
import se.galvend.isick.classes.Kid

/**
 * Created by dennisgalven on 2018-02-13.
 */
class KidAdapter: RecyclerView.Adapter<KidViewHolder>() {
    companion object {
        val TAG = "KidAdapter"
    }
    var kids : List<Kid> = emptyList()

    override fun getItemCount(): Int = kids.count()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): KidViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.kidrecyclercell, parent, false)
        return KidViewHolder(view)
    }

    override fun onBindViewHolder(holder: KidViewHolder?, position: Int) {
        val kid = kids[position]
        holder?.nameLabel?.text = kid.name
        holder?.prsnNrLabel?.text = kid.personNumber
        holder?.isSickSwitch?.isChecked = kid.isSick

        holder?.isSickSwitch?.setOnCheckedChangeListener { _, checked ->
            kid.isSick = checked
        }
    }

}
class KidViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val nameLabel: TextView = view.findViewById(R.id.kidNameLabel)
    val prsnNrLabel: TextView = view.findViewById(R.id.kidPrsnnrLabel)
    val isSickSwitch: Switch = view.findViewById(R.id.sickSwitch)
}