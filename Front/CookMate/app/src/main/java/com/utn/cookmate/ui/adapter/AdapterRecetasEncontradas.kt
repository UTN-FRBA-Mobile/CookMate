package com.utn.cookmate.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.utn.cookmate.R

class AdapterRecetasEncontradas(context: Context, private val mData: List<String>) :
    RecyclerView.Adapter<AdapterRecetasEncontradas.ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData[position]
        holder.myTextView.text = item
        holder.myButton.setOnClickListener {
            mClickListener?.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val myTextView: TextView = itemView.findViewById(R.id.nombre_receta)
        val myButton: ImageButton = itemView.findViewById(R.id.boton_guardar_receta)
    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(item: String)
    }
}
