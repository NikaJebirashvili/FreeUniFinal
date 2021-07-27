package ge.njebirashvili.freeunifinalproject.presenter

import ge.njebirashvili.freeunifinalproject.model.User
import ge.njebirashvili.freeunifinalproject.repository.MainRepository
import ge.njebirashvili.freeunifinalproject.views.ChatListView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewContactPresenter @Inject constructor(
    private val chatListView: ChatListView,
    private val repository: MainRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {

    suspend fun searchForUser(username : String) : List<User> = withContext(Dispatchers.Main){
        chatListView.loadingState(true)
        val result = repository.searchUser(username)
        chatListView.loadingState(false)
        result
    }

    suspend fun getAllUsers() : List<User> = withContext(Dispatchers.Main){
        chatListView.loadingState(true)
        val result = repository.getAllUser()
        chatListView.loadingState(false)
        result
    }
}