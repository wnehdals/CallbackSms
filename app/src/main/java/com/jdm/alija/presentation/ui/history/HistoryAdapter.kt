package com.jdm.alija.presentation.ui.history

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
import com.jdm.alija.databinding.ItemCallbackHistoryBinding
import com.jdm.alija.databinding.ItemContactBinding
import com.jdm.alija.domain.model.BlackContact
import com.jdm.alija.domain.model.CallbackResult
import com.jdm.alija.domain.model.Contact

class HistoryAdapter(
    private val context: Context,
    private val onClickHistory: (CallbackResult) -> Unit,
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private val currentList = mutableListOf<CallbackResult>()
    private val onItemDelete =
        { contact: CallbackResult -> onClickHistory.invoke(contact) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCallbackHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemDelete)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = currentList[position]
        holder.bindView(item)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    fun submitList(data: List<CallbackResult>) {
        currentList.clear()
        currentList.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        val binding: ItemCallbackHistoryBinding,
        onItemSwitchClick: (CallbackResult) -> Unit
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        init {
            binding.llItemCallbackHistory.setOnClickListener {
                onItemSwitchClick(currentList[bindingAdapterPosition])
            }
        }

        fun bindView(item: CallbackResult) {
            with(binding) {
                tvItemCallbackHistoryName.text = item.name
                tvItemCallbackHistoryMobile.text = item.mobile
                tvItemCallbackHistoryDate.text = "${item.year}-${String.format("%02d", item.month)}-${String.format("%02d", item.day)} ${String.format("%02d", item.hour)}시 ${String.format("%02d", item.minute)}분"
            }
        }
    }
}
