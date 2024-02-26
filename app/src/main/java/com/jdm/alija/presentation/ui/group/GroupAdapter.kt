package com.jdm.alija.presentation.ui.group

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jdm.alija.R
import com.jdm.alija.databinding.ItemContactBinding
import com.jdm.alija.databinding.ItemGroupBinding
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.Group

class GroupAdapter(
    private val onClickGroup: (Group) -> Unit,
    private val onClickDelete: (Group) -> Unit
) : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {
    private val currentList = mutableListOf<Group>()
    private val onItemSwitchClick =
        { group: Group -> onClickGroup.invoke(group) }
    private val onItemDeleteClick =
        { group: Group -> onClickDelete.invoke(group) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemSwitchClick, onItemDeleteClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = currentList[position]
        holder.bindView(item)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    fun submitList(data: List<Group>) {
        currentList.clear()
        currentList.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        val binding: ItemGroupBinding,
        onItemSwitchClick: (Group) -> Unit,
        onClickDelete: (Group) -> Unit
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        init {
            binding.llItemGroup.setOnClickListener {
                onItemSwitchClick(currentList[bindingAdapterPosition])
            }
            binding.ivItemGroupDelete.setOnClickListener {
                onClickDelete(currentList[bindingAdapterPosition])
            }
        }

        fun bindView(item: Group) {
            with(binding) {
                tvItemGroupName.text = item.name
            }
        }
    }
}
