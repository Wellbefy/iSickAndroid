package se.galvend.isick.reportfragment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import se.galvend.isick.R

/**
 * Created by dennisgalven on 2018-02-13.
 */
class KidAdapter: RecyclerView.Adapter<KidViewHolder>() {
    override fun getItemCount(): Int = 2

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): KidViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.kidrecyclercell, parent, false)
        return KidViewHolder(view)
    }

    override fun onBindViewHolder(holder: KidViewHolder?, position: Int) {
        holder?.nameLabel?.text = "Alice Galvén"
        holder?.prsnnrLabel?.text = "081110-8281"
        holder?.isSickSwitch?.isChecked = false
    }

}
class KidViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val nameLabel: TextView = view.findViewById(R.id.kidNameLabel)
    val prsnnrLabel: TextView = view.findViewById(R.id.kidPrsnnrLabel)
    val isSickSwitch: Switch = view.findViewById(R.id.sickSwitch)
}