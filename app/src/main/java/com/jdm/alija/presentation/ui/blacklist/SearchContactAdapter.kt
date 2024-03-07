package com.jdm.alija.presentation.ui.blacklist

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
import com.jdm.alija.databinding.ItemBlacklistBinding
import com.jdm.alija.databinding.ItemContactBinding
import com.jdm.alija.databinding.ItemSearchContactBinding
import com.jdm.alija.domain.model.BlackContact
import com.jdm.alija.domain.model.Contact

class SearchContactAdapter(
    private val context: Context,
    private val onClickDelete: (BlackContact, Int) -> Unit,
) : RecyclerView.Adapter<SearchContactAdapter.ViewHolder>() {
    private val currentList = mutableListOf<BlackContact>()
    private val onItemDelete =
        { contact: BlackContact, pos: Int -> onClickDelete.invoke(contact, pos) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSearchContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemDelete)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = currentList[position]
        holder.bindView(item)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    fun submitList(data: List<BlackContact>) {
        currentList.clear()
        currentList.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        val binding: ItemSearchContactBinding,
        onItemSwitchClick: (BlackContact, Int) -> Unit
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        init {
            binding.ivItemSearchCheck.setOnClickListener {
                onItemSwitchClick(currentList[bindingAdapterPosition], bindingAdapterPosition)
            }
        }

        fun bindView(item: BlackContact) {
            with(binding) {
                tvItemSearchName.text = item.name
                tvItemSearchMobile.text = item.mobile
                ivItemSearchCheck.isSelected = item.isSelected
            }
        }
    }
}
