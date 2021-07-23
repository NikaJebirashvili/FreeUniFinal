package ge.njebirashvili.freeunifinalproject.views

import ge.njebirashvili.freeunifinalproject.model.User

interface ChatListView {
    fun loadingState(boolean: Boolean)
    fun searchingUser(username : String) : List<User>
}