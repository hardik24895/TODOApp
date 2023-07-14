package com.example.hardiknoteapp.ui.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hardiknoteapp.data.modal.NoteModal
import com.example.hardiknoteapp.databinding.RowNoteBinding


class NoteAdapter(
    private val mContext: Context,
    private var list: MutableList<NoteModal> = mutableListOf(),

    private val listener: OnItemSelected,
) : RecyclerView.Adapter<NoteAdapter.ItemHolder>() {

    lateinit var binding: RowNoteBinding

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        binding = RowNoteBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false
        )
        return ItemHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]
        holder.bindData(mContext, data, listener)
    }


    interface OnItemSelected {
        fun onItemSelect(position: Int, data: NoteModal, action: String)
    }

    class ItemHolder(containerView: RowNoteBinding) : RecyclerView.ViewHolder(containerView.root) {
        private val binding = containerView

        fun bindData(
            context: Context,
            data: NoteModal,
            listener: OnItemSelected
        ) {
            binding.txtTitle.text = data.title
            binding.txtDesc.text = data.description
            binding.maiView.setOnClickListener { listener.onItemSelect(absoluteAdapterPosition,data,"Click Event") }
        }
    }


}