package com.jdm.alija.presentation.ui.photoselect

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jdm.alija.R
import com.jdm.alija.databinding.ItemContractBinding
import com.jdm.alija.databinding.ItemPhotoBinding
import com.jdm.alija.domain.model.Contact
import com.jdm.alija.domain.model.Photo

class PhotoSelectAdapter(
    private val context: Context,
    private val onClickPhoto: (Int?, Int) -> Unit,
) : RecyclerView.Adapter<PhotoSelectAdapter.ViewHolder>() {
    private val currentList = mutableListOf<Photo>()
    private val onItemClick = { previous: Int?, now: Int -> onClickPhoto.invoke(previous, now) }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = currentList[position]
        holder.bindView(item)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
    fun submitList(data: List<Photo>) {
        currentList.clear()
        currentList.addAll(data)
        notifyDataSetChanged()
    }
    fun getSelectedPhoto(): Photo? {
        return currentList.filter { it.isSelected }.firstOrNull()
    }

    inner class ViewHolder(val binding: ItemPhotoBinding, onItemClick: (Int?, Int) -> Unit) : RecyclerView.ViewHolder(
        binding.root
    ) {
        init {
            binding.clItemPhoto.setOnClickListener {
                var previousIdx: Int? = null
                for(i in currentList.indices) {
                    if (currentList[i].isSelected) {
                        previousIdx = i
                        break
                    }
                }
                if (previousIdx != null) {
                    currentList[previousIdx].isSelected = !currentList[previousIdx].isSelected
                }
                currentList[bindingAdapterPosition].isSelected = !currentList[bindingAdapterPosition].isSelected
                onItemClick(previousIdx, bindingAdapterPosition)
            }
        }
        fun bindView(item: Photo) {
            with(binding) {
                clItemPhoto.isSelected = item.isSelected
                Glide.with(context)
                    .load(item.uri)
                    .error(R.drawable.ic_img_fail_black)
                    .placeholder(R.drawable.ic_img_fail_black)
                    .into(binding.ivItemPhoto)
            }
        }
    }
}
