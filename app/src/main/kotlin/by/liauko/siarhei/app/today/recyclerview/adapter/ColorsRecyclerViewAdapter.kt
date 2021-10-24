package by.liauko.siarhei.app.today.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.recyclerview.holder.ColorsRecyclerViewHolder

class ColorsRecyclerViewAdapter(
    private val items: IntArray,
    private val listener: RecyclerViewOnItemClickListener
) : RecyclerView.Adapter<ColorsRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsRecyclerViewHolder =
        ColorsRecyclerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.colors_rv_item, parent, false)
        )

    override fun onBindViewHolder(holder: ColorsRecyclerViewHolder, position: Int) {
        val color = items[position]
        holder.colorItem.setColorFilter(color)
        holder.colorItem.tag = color
        holder.colorItem.contentDescription = color.toString()
        holder.bind(color, listener)
    }

    override fun getItemCount() = items.size

    interface RecyclerViewOnItemClickListener {
        fun onItemClick(item: Int)
    }
}
