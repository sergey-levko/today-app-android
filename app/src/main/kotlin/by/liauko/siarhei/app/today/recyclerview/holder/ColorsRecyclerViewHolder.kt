package by.liauko.siarhei.app.today.recyclerview.holder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import by.liauko.siarhei.app.today.R
import by.liauko.siarhei.app.today.recyclerview.adapter.ColorsRecyclerViewAdapter

class ColorsRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val colorItem : ImageView = itemView.findViewById(R.id.color_item)

    fun bind(item: Int, listener: ColorsRecyclerViewAdapter.RecyclerViewOnItemClickListener) {
        itemView.setOnClickListener { listener.onItemClick(item) }
    }
}
