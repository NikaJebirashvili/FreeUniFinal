package ge.njebirashvili.freeunifinalproject.presenter

import ge.njebirashvili.freeunifinalproject.model.Message
import ge.njebirashvili.freeunifinalproject.repository.MainRepository
import ge.njebirashvili.freeunifinalproject.views.ChatView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatPresenter @Inject constructor(
    private val chatView: ChatView,
    private val repository: MainRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
){

    suspend fun getAllMessages(uid : String) = withContext(Dispatchers.Main){
        chatView.loadingState(true)
        val messages = repository.getChatMessages(uid)
        chatView.getAllMessages()
        chatView.loadingState(false)
        messages
    }

    suspend fun addChatMessage(message : String, uid: String) = withContext(Dispatchers.Main){
        val messageForToggle = Message(message,uid,System.currentTimeMillis())
        chatView.loadingState(true)
        val messages = repository.addChatMessage(message,uid)
        repository.toggleFollowForUser(uid)
        repository.toggleLastSentMessage(messageForToggle)
        chatView.loadingState(false)
        chatView.sendMessage()
        messages
    }

}