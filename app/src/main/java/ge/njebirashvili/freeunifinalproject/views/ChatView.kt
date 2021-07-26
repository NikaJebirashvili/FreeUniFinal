package ge.njebirashvili.freeunifinalproject.views

interface ChatView {
    fun loadingState(boolean: Boolean)
    fun sendMessage()
    fun getAllMessages()

}