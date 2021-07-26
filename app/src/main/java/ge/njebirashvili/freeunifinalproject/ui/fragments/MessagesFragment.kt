package ge.njebirashvili.freeunifinalproject.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import ge.njebirashvili.freeunifinalproject.R
import ge.njebirashvili.freeunifinalproject.adapter.MessagesAdapter
import ge.njebirashvili.freeunifinalproject.databinding.FragmentMessagesBinding
import ge.njebirashvili.freeunifinalproject.model.Message
import ge.njebirashvili.freeunifinalproject.model.User
import ge.njebirashvili.freeunifinalproject.presenter.ChatListPresenter
import ge.njebirashvili.freeunifinalproject.presenter.ChatPresenter
import ge.njebirashvili.freeunifinalproject.repository.MainRepository
import ge.njebirashvili.freeunifinalproject.utils.setVisible
import ge.njebirashvili.freeunifinalproject.views.ChatView
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class MessagesFragment : Fragment(R.layout.fragment_messages), ChatView {
    lateinit var binding: FragmentMessagesBinding
    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var firestore: FirebaseFirestore
    @Inject
    lateinit var storage: FirebaseStorage
    @Inject
    lateinit var realtime: FirebaseDatabase
    lateinit var chatPresenter: ChatPresenter
    var chatList = listOf<Message>()
    @Inject
    lateinit var adapter: MessagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user: User = arguments?.getSerializable("user") as User
        initRecyclerView()

        chatPresenter = ChatPresenter(this, MainRepository(auth, firestore, storage, realtime))

        lifecycleScope.launch {
            user.let {
                try {
                    chatList = chatPresenter.getAllMessages(it.uid)
                    adapter.setData(chatList)
                } catch (e: Exception) {
                    binding.messagesProgressBar.setVisible(false)
                }
            }
        }

        binding.messagesSendButton.setOnClickListener {
            lifecycleScope.launch {
                val list = chatPresenter.addChatMessage(
                    binding.messageEditText.text.toString(),
                    user.uid
                )
                adapter.setData(list)
            }

        }




        binding.chatBackButton.setOnClickListener { activity?.onBackPressed() }

        activity?.findViewById<BottomAppBar>(R.id.bottom_app_bar)?.setVisible(false)
        activity?.findViewById<FloatingActionButton>(R.id.floating_action_button)?.setVisible(false)
        activity?.findViewById<FrameLayout>(R.id.main_activity_frame).also {
            it?.setPadding(0, 0, 0, 0)
        }

        binding.apply {
            Glide.with(this@MessagesFragment)
                .load(user.profilePictureUrl)
                .into(binding.messagesProfileImage)
            messagesNameTextView.text = user.username
            messagesWhatIDo.text = user.whatIDo
        }


    }

    private fun initRecyclerView() {
        binding.messagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MessagesFragment.adapter
        }
    }

    override fun loadingState(boolean: Boolean) {
        binding.messagesProgressBar.setVisible(boolean)
    }

    override fun sendMessage() {

    }

    override fun getAllMessages() {

    }
}