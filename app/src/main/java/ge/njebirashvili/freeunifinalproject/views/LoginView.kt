package ge.njebirashvili.freeunifinalproject.views

interface LoginView {
    fun showToast(message: String?)
    fun authSuccessful()
    fun loadingState(boolean: Boolean)
}