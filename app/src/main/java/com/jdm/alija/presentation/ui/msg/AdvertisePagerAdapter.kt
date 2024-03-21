package com.jdm.alija.presentation.ui.msg

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jdm.alija.databinding.ItemAdvertiseBinding
import com.jdm.alija.databinding.ItemGroupBinding
import com.jdm.alija.domain.model.Advertise

class AdvertisePagerAdapte (
    private val context: Context,
    private val listData: List<Advertise>,
    private val onClickItem: (Advertise) -> Unit
) : RecyclerView.Adapter<AdvertisePagerAdapte.ViewHolderPage>() {
    lateinit var binding: ItemAdvertiseBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPage {
        val binding = ItemAdvertiseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderPage(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderPage, position: Int) {
        val viewHolder: ViewHolderPage = holder
        viewHolder.onBind(listData[position % listData.size])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    inner class ViewHolderPage(val binding: ItemAdvertiseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Advertise) {
            binding.clItemAdvertise.setOnClickListener { onClickItem(data) }
            Glide.with(context)
                .load(data.title)
                .into(binding.tvItemAdvertise)
        }
    }
}
