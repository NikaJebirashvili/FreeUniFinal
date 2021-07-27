package ge.njebirashvili.freeunifinalproject.presenter

import com.google.firebase.auth.FirebaseAuth
import ge.njebirashvili.freeunifinalproject.model.UpdateProfile
import ge.njebirashvili.freeunifinalproject.repository.MainRepository
import ge.njebirashvili.freeunifinalproject.views.ProfileView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfilePresenter @Inject constructor(
    private val profileView: ProfileView,
    private val repository: MainRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
){
    suspend fun getUser(uid : String) = withContext(Dispatchers.Main){
        profileView.loadingState(true)
        val result = repository.getUser(uid)
        profileView.loadingState(false)
        result
    }

    suspend fun signOut() {
        profileView.loadingState(true)
        repository.userLogOut()
        profileView.loadingState(false)
        profileView.signOut()
    }

    suspend fun updateProfile(updateProfile: UpdateProfile){
        profileView.loadingState(true)
        repository.updateProfile(updateProfile)
        profileView.loadingState(false)
    }
}