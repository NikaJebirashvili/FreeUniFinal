package ge.njebirashvili.freeunifinalproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import ge.njebirashvili.freeunifinalproject.databinding.ActivityLoginBinding
import ge.njebirashvili.freeunifinalproject.presenter.LoginPresenter
import ge.njebirashvili.freeunifinalproject.utils.goOnActivity
import ge.njebirashvili.freeunifinalproject.utils.setVisible
import ge.njebirashvili.freeunifinalproject.views.LoginView
import ge.njebirashvili.freeunifinalproject.repository.AuthRepository
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() , LoginView {
    lateinit var binding : ActivityLoginBinding
    @Inject
    lateinit var auth : FirebaseAuth
    @Inject
    lateinit var firestore : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loginPresenter = LoginPresenter(this, AuthRepository(auth,firestore))


        if(auth.currentUser != null) {
            goOnActivity(MainActivity::class.java).also {
                finish()
            }
        }
        binding.loginSignInButton.setOnClickListener {
            if(binding.nicknameEditText.text.isEmpty()){
                binding.nicknameEditText.apply {
                    error = "Please Fill"
                    requestFocus()
                }
                return@setOnClickListener
            }
            if(binding.loginPasswordEditText.text.isEmpty()){
                binding.loginPasswordEditText.apply {
                    error = "Please Fill"
                    requestFocus()
                }
                return@setOnClickListener
            }

            loginPresenter.firebaseLogin(
                binding.nicknameEditText.text.toString(),
                binding.loginPasswordEditText.text.toString()
            )
        }
        binding.loginRegisterButton.setOnClickListener {
            goOnActivity(RegisterActivity::class.java).also {
                finish()
            }
        }
    }

    override fun showToast(message: String?) {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }

    override fun authSuccessful() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    override fun loadingState(boolean: Boolean) {
        binding.loginProgressBar.setVisible(boolean)
    }


}