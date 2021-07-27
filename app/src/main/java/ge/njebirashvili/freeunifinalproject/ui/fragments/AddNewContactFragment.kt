package ge.njebirashvili.freeunifinalproject.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ge.njebirashvili.freeunifinalproject.R
import ge.njebirashvili.freeunifinalproject.adapter.SearchAdapter
import ge.njebirashvili.freeunifinalproject.databinding.FragmentAddNewContactBinding
import ge.njebirashvili.freeunifinalproject.model.User
import ge.njebirashvili.freeunifinalproject.presenter.NewContactPresenter
import ge.njebirashvili.freeunifinalproject.repository.MainRepository
import ge.njebirashvili.freeunifinalproject.utils.Constants
import ge.njebirashvili.freeunifinalproject.utils.setVisible
import ge.njebirashvili.freeunifinalproject.views.ChatListView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddNewContactFragment : Fragment(R.layout.fragment_add_new_contact) , ChatListView {
    lateinit var binding: FragmentAddNewContactBinding
    val adapter = SearchAdapter()
    lateinit var presenter : NewContactPresenter
    @Inject lateinit var repository: MainRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewContactBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        presenter = NewContactPresenter(this,repository)

        binding.addNewBackButton.setOnClickListener { activity?.onBackPressed() }

        lifecycleScope.launch {
            val allUser = presenter.getAllUsers()
            adapter.setData(allUser)
        }

        binding.addNewSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            var job : Job? = null
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                job?.cancel()
                job = lifecycleScope.launch {
                    delay(Constants.SEARCH_TIME_DELAY)
                    newText?.let {
                        adapter.setData(presenter.searchForUser(it))
                    }
                }
                return true
            }

        })

    }


    private fun initRecyclerView() {
        binding.addNewContactRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AddNewContactFragment.adapter.apply {
                setOnClickListener {
                    findNavController().navigate(AddNewContactFragmentDirections.actionAddNewContactFragmentToMessagesFragment(it))
                }
            }
        }
    }

    override fun loadingState(boolean: Boolean) {
        binding.chatListProgressBar.setVisible(boolean)
    }

    override fun searchingUser(username: String): List<User> {
        var resultList = listOf<User>()
        lifecycleScope.launch {
            resultList = presenter.searchForUser(username)
        }
        return resultList
    }
}