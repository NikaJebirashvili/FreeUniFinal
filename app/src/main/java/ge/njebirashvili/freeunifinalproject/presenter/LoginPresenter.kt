package ge.njebirashvili.freeunifinalproject.presenter

import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import ge.njebirashvili.freeunifinalproject.views.LoginView
import ge.njebirashvili.mysdf.repository.AuthRepository
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val loginView: LoginView,
    private val repository : AuthRepository,
    private val coroutineScope : CoroutineScope = CoroutineScope(Dispatchers.Main)
) {

    fun firebaseLogin(email: String,password: String) {
        coroutineScope.launch {
            try {
                loginView.loadingState(true)
                val result = repository.login(email,password)
                if(result.user != null) {
                    loginView.authSuccessful()
                    loginView.loadingState(false)
                } else {
                    loginView.loadingState(false)
                    loginView.showToast("Something Went Wrong")
                }
            } catch (e : Exception){
                loginView.loadingState(false)
                loginView.showToast("${e.message}")
            }

        }
    }

}