package ge.njebirashvili.freeunifinalproject.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import ge.njebirashvili.freeunifinalproject.R
import ge.njebirashvili.freeunifinalproject.adapter.ChatListAdapter
import ge.njebirashvili.freeunifinalproject.adapter.SearchAdapter
import ge.njebirashvili.freeunifinalproject.databinding.FragmentChatListBinding
import ge.njebirashvili.freeunifinalproject.model.User
import ge.njebirashvili.freeunifinalproject.presenter.ChatListPresenter
import ge.njebirashvili.freeunifinalproject.repository.MainRepository
import ge.njebirashvili.freeunifinalproject.ui.MainActivity
import ge.njebirashvili.freeunifinalproject.utils.Constants.SEARCH_TIME_DELAY
import ge.njebirashvili.freeunifinalproject.utils.setVisible
import ge.njebirashvili.freeunifinalproject.views.ChatListView
import ge.njebirashvili.freeunifinalproject.views.MainHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.fragment_chat_list), ChatListView  {
    lateinit var binding: FragmentChatListBinding
    @Inject lateinit var auth: FirebaseAuth
    @Inject lateinit var firestore: FirebaseFirestore
    @Inject lateinit var storage: FirebaseStorage
    @Inject lateinit var realtime : FirebaseDatabase
    lateinit var chatListPresenter: ChatListPresenter
    val helper = MainActivity()
    val chatListAdapter = ChatListAdapter()
    val searchListAdapter = SearchAdapter()
    lateinit var currentUser : User


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.findViewById<BottomAppBar>(R.id.bottom_app_bar)?.setVisible(true)
        activity?.findViewById<FloatingActionButton>(R.id.floating_action_button)?.setVisible(true)
        activity?.findViewById<FrameLayout>(R.id.main_activity_frame).also {
            it?.setPadding(0, 0, 0, 56)
        }

        chatListPresenter = ChatListPresenter(this, MainRepository(auth, firestore, storage,realtime))
        initUserRecyclerView()
        initSearchRecyclerView()
        binding.usersRecyclerView.setVisible(true)
        binding.searchRecyclerView.setVisible(false)

        lifecycleScope.launch {
            currentUser = chatListPresenter.getUser(auth.uid!!)!!
            if(currentUser.follows.isNotEmpty()){
                val list = chatListPresenter.getAllFollowedUsers(currentUser.follows)
                chatListAdapter.setData(list)
            }

        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            var job : Job? = null
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                job?.cancel()
                job = lifecycleScope.launch {
                    delay(SEARCH_TIME_DELAY)
                    newText?.let {
                        if(it == ""){
                            binding.usersRecyclerView.setVisible(true)
                            binding.searchRecyclerView.setVisible(false)
                        } else {
                            binding.usersRecyclerView.setVisible(false)
                            binding.searchRecyclerView.setVisible(true)
                        }
                        searchListAdapter.setData(chatListPresenter.searchForUser(it))
                    }
                }
                return true
            }

        })

    }

    override fun loadingState(boolean: Boolean) {
        binding.chatListProgressBar.setVisible(boolean)
    }

    override fun searchingUser(username: String): List<User> {
        var resultList = listOf<User>()
        lifecycleScope.launch {
            resultList = chatListPresenter.searchForUser(username)
        }
        return resultList
    }

    private fun initUserRecyclerView() {
        binding.usersRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatListAdapter.apply {
                setOnClickListener {
                    findNavController().navigate(ChatFragmentDirections.actionChatFragmentToMessagesFragment(it))
                }
            }
        }
    }

    private fun initSearchRecyclerView() {
        binding.searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchListAdapter.apply {
                setOnClickListener {
                    findNavController().navigate(ChatFragmentDirections.actionChatFragmentToMessagesFragment(it))
                }
            }
        }
    }
}