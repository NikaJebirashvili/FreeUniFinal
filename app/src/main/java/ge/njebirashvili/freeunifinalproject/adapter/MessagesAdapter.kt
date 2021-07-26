package ge.njebirashvili.freeunifinalproject.adapter

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import ge.njebirashvili.freeunifinalproject.R
import ge.njebirashvili.freeunifinalproject.databinding.MessageListItemBinding
import ge.njebirashvili.freeunifinalproject.model.Message
import java.util.*
import javax.inject.Inject


class MessagesAdapter @Inject constructor(
    private val auth: FirebaseAuth
) : RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>() {
    private var dataset : List<Message> = listOf()

    inner class MessagesViewHolder(private val binding: MessageListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(message: Message) {
            if(message.sender != auth.uid!!){
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.gravity = Gravity.START
                layoutParams.bottomMargin = 8
                layoutParams.marginStart = 8
                layoutParams.topMargin = 8
                val messageLinearLayout = binding.messageLinearLayout
                val v0: View = messageLinearLayout.getChildAt(0)
                val v1: View = messageLinearLayout.getChildAt(1)

                messageLinearLayout.removeAllViews()

                messageLinearLayout.addView(v1)
                messageLinearLayout.addView(v0)

                binding.messagesCardView.layoutParams = layoutParams
                binding.messageMessage.setBackgroundResource(R.drawable.sender_message_background)
                binding.messageMessage.setTextColor(Color.BLACK)
            }

            binding.messageMessage.text = message.message
            binding.messageSendDate.text = SimpleDateFormat("h:mm a", Locale.ENGLISH).format(message.sentDate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        return MessagesViewHolder(MessageListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item)
    }

    override fun getItemCount() = dataset.size

    fun setData(list : List<Message>){
        dataset = list
        notifyDataSetChanged()
    }

}