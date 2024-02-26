package com.jdm.alija.presentation.ui.contract

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

    inner class ViewHolder(
        val binding: ItemContactBinding,
        onItemSwitchClick: (Contact, Int) -> Unit
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        init {
            binding.ivItemContactCheck.setOnClickListener {
                currentList[bindingAdapterPosition].isSelected = !currentList[bindingAdapterPosition].isSelected
                notifyItemChanged(bindingAdapterPosition)
                onItemSwitchClick(currentList[bindingAdapterPosition], bindingAdapterPosition)
            }
        }

        fun bindView(item: Contact) {
            with(binding) {
                tvItemContactIncall.visibility = if (item.isIncall) View.VISIBLE else View.GONE
                tvItemContactOutcall.visibility = if (item.isOutCall) View.VISIBLE else View.GONE
                tvItemContactReleasecall.visibility = if (item.isReleaseCall) View.VISIBLE else View.GONE
                tvItemContactName.text = item.name
                tvItemContactGroupName.text = item.groupName
                if (item.numbers.size > 1) {
                    var mobile = ""
                    var idx = 0
                    var end = 0
                    for(i in item.numbers.indices) {
                        if (item.numbers[i] == item.mobile) {
                            idx = i
                            end = item.numbers[i].length
                        }
                        if (i < item.numbers.size-1)
                            mobile += "${item.numbers[i]}, "
                        else
                            mobile += "${item.numbers[i]}"
                    }
                    val span = SpannableStringBuilder(mobile)
                    span.setSpan(ForegroundColorSpan(Color.BLACK), idx, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    tvItemContactGroupPhone.setText(span, TextView.BufferType.SPANNABLE)
                } else if (item.numbers.size == 1){
                    tvItemContactGroupPhone.setTextColor(ContextCompat.getColor(context, R.color.black))
                    tvItemContactGroupPhone.text = item.numbers[0]
                }
                ivItemContactCheck.isEnabled = item.isEnable
                ivItemContactCheck.isSelected = item.isSelected
            }
        }
    }
}
