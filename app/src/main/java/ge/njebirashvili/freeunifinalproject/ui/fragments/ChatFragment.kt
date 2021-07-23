package ge.njebirashvili.freeunifinalproject.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ge.njebirashvili.freeunifinalproject.R
import ge.njebirashvili.freeunifinalproject.databinding.FragmentChatListBinding

class ChatFragment : Fragment(R.layout.fragment_chat_list) {
    lateinit var binding : FragmentChatListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.setOnClickListener {
            binding.collapsingLayout.setExpanded(false)
        }
    }
}