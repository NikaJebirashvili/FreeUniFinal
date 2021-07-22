package ge.njebirashvili.freeunifinalproject.views

interface RegisterView {
    fun showToast(message: String?)
    fun registerSuccessful()
    fun loadingState(boolean: Boolean)
}