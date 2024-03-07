package com.jdm.alija.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jdm.alija.R
import com.jdm.alija.databinding.ItemListSelectBinding
import com.jdm.alija.domain.model.SelectData

class SelectAdapter(
    private val context: Context,
    private val onClickSelectData: (SelectData) -> Unit,
) : RecyclerView.Adapter<SelectAdapter.ViewHolder>() {
    private val currentList = mutableListOf<SelectData>()
    private val onItemClick =
        { item: SelectData -> onClickSelectData.invoke(item) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListSelectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = currentList[position]
        holder.bindView(item)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    fun submitList(data: List<SelectData>) {
        currentList.clear()
        currentList.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        val binding: ItemListSelectBinding,
        onItemClick: (SelectData) -> Unit,
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        init {
            binding.llDialogSelectItem.setOnClickListener {
                onItemClick(currentList[bindingAdapterPosition])
            }
        }

        fun bindView(item: SelectData) {
            with(binding) {
                tvDialogSelectItem.text = item.text
                llDialogSelectItem.setBackgroundColor(if (item.isSelected) ContextCompat.getColor(context, R.color.gray_200) else ContextCompat.getColor(context, R.color.white))

            }
        }
    }
}
