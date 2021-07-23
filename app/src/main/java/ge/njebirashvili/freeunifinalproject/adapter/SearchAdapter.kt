package ge.njebirashvili.freeunifinalproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ge.njebirashvili.freeunifinalproject.databinding.ListItemChatBinding
import ge.njebirashvili.freeunifinalproject.model.User

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ChatViewHolder>() {
    private var dataset : List<User> = listOf()

    inner class ChatViewHolder(private val binding: ListItemChatBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user : User){
            itemView.setOnClickListener { listener?.invoke() }
            Glide.with(itemView)
                .load(user.profilePictureUrl)
                .into(binding.circleImageView)
            binding.listItemName.text = user.username
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(ListItemChatBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item)
    }

    override fun getItemCount() = dataset.size

    fun setData(list : List<User>){
        dataset = list
        notifyDataSetChanged()
    }

    var listener : (() -> Unit)? = null

    fun setOnClickListener(listener : () -> Unit) {
        this.listener = listener
    }

}