package ge.njebirashvili.freeunifinalproject.presenter

import ge.njebirashvili.freeunifinalproject.views.RegisterView
import ge.njebirashvili.freeunifinalproject.repository.AuthRepository

import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

class RegisterPresenter @Inject constructor(
    private val registerView: RegisterView,
    private val repository : AuthRepository,
    private val coroutineScope : CoroutineScope = CoroutineScope(Dispatchers.Main)
) {

     fun firebaseRegister(email :String,username : String, password : String) {
         coroutineScope.launch {
             try {
                 registerView.loadingState(true)
                 val result = repository.register(email,username,password)
                 if(result.user != null) {
                     registerView.showToast("You have Successfully registered")
                     registerView.loadingState(false)
                     registerView.registerSuccessful()

                 } else {
                     registerView.showToast("Check your Internet Connection and Try again")
                     registerView.loadingState(false)
                 }
             }catch (e : Exception){
                 registerView.loadingState(false)
                 registerView.showToast(e.message)
             }

         }
    }

}