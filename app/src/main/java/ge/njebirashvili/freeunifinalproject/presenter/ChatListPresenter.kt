package ge.njebirashvili.freeunifinalproject.presenter

import ge.njebirashvili.freeunifinalproject.model.User
import ge.njebirashvili.freeunifinalproject.repository.MainRepository
import ge.njebirashvili.freeunifinalproject.views.ChatListView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatListPresenter @Inject constructor(
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

    suspend fun getAllFollowedUsers(uids : List<String>) = withContext(Dispatchers.Main) {
        chatListView.loadingState(true)
        val result = repository.getUsers(uids)
        chatListView.loadingState(false)
        result
    }

    suspend fun getUser(uid : String) = withContext(Dispatchers.Main){
        chatListView.loadingState(true)
        val user = repository.getUser(uid)
        chatListView.loadingState(false)
        user
    }
}