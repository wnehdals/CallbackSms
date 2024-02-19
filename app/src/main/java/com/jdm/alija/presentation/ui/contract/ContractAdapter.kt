package com.jdm.alija.presentation.ui.contract

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdm.alija.databinding.ItemContractBinding
import com.jdm.alija.domain.model.Contact

class ContractAdapter(
    private val onClickContact: (Contact) -> Unit,
    private val onClickSwitch: (Contact, Int) -> Unit,
) : RecyclerView.Adapter<ContractAdapter.ViewHolder>() {
    private val currentList = mutableListOf<Contact>()
    private val onItemClick = { contact: Contact -> onClickContact.invoke(contact) }
    private val onItemSwitchClick = { contact: Contact, pos: Int -> onClickSwitch.invoke(contact, pos) }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContractBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick, onItemSwitchClick)
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

    inner class ViewHolder(val binding: ItemContractBinding, onItemClick: (Contact) -> Unit, onItemSwitchClick: (Contact, Int) -> Unit) : RecyclerView.ViewHolder(
        binding.root
    ) {
        init {
            binding.llItemContact.setOnClickListener {
                onItemClick(currentList[bindingAdapterPosition])
            }
            binding.ivItemContractSwitch.setOnClickListener {
                onItemSwitchClick(currentList[bindingAdapterPosition], bindingAdapterPosition)
            }
        }
        fun bindView(item: Contact) {
            with(binding) {
                val mobile = if (item.numbers.isEmpty())"Unknown" else item.numbers[0]
                tvItemContractName.text = "${item.name}\n${mobile}"
                ivItemContract.isSelected = item.isSelected
                ivItemContractSwitch.isSelected = item.isSelected
            }
        }
    }
}
