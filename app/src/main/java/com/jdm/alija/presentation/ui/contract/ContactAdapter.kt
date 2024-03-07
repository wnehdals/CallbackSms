package com.jdm.alija.presentation.ui.contract

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jdm.alija.R
import com.jdm.alija.databinding.ItemContactBinding
import com.jdm.alija.domain.model.Contact

class ContactAdapter(
    private val context: Context,
    private val onClickSwitch: (Contact, Int) -> Unit,
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    private val currentList = mutableListOf<Contact>()
    private val onItemSwitchClick =
        { contact: Contact, pos: Int -> onClickSwitch.invoke(contact, pos) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemSwitchClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = currentList[position]
        holder.bindView(item)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    fun submitList(data: List<Contact>) {
        currentList.clear()
        currentList.addAll(data)
        notifyDataSetChanged()
    }
    fun allSelect(value: Boolean) {
        currentList.forEach { it.isSelected = value }
    }

    inner class ViewHolder(
        val binding: ItemContactBinding,
        onItemSwitchClick: (Contact, Int) -> Unit
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        init {
            binding.ivItemContactCheck.setOnClickListener {
                Log.e("ppppp", "${bindingAdapterPosition}")
                onItemSwitchClick(currentList[bindingAdapterPosition], bindingAdapterPosition)
            }
        }

        fun bindView(item: Contact) {
            with(binding) {
                tvItemContactName.text = item.name
                tvItemContactGroupPhone.text = item.mobile
                ivItemContactCheck.isEnabled = item.isEnable
                ivItemContactCheck.isSelected = item.isSelected
                tvItemContactGroupName.text = item.groupName
            }
        }
    }
}
