package fr.delcey.fizzbuzz.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.delcey.fizzbuzz.databinding.FizzbuzzListItemBinding

class FizzbuzzAdapter : RecyclerView.Adapter<FizzbuzzAdapter.FizzbuzzViewHolder>() {

    var items = emptyList<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FizzbuzzViewHolder(
        FizzbuzzListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: FizzbuzzViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class FizzbuzzViewHolder(private val binding: FizzbuzzListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            binding.fizzbuzzItemTextView.text = text
        }
    }
}
