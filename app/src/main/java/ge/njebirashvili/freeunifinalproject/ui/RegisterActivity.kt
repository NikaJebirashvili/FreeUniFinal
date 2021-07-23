package ge.njebirashvili.freeunifinalproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import ge.njebirashvili.freeunifinalproject.databinding.ActivityRegisterBinding
import ge.njebirashvili.freeunifinalproject.presenter.RegisterPresenter
import ge.njebirashvili.freeunifinalproject.utils.Constants.MAX_USERNAME_LENGTH
import ge.njebirashvili.freeunifinalproject.utils.Constants.MIN_USERNAME_LENGTH
import ge.njebirashvili.freeunifinalproject.utils.goOnActivity
import ge.njebirashvili.freeunifinalproject.utils.setVisible
import ge.njebirashvili.freeunifinalproject.views.RegisterView
import ge.njebirashvili.freeunifinalproject.repository.AuthRepository
import javax.inject.Inject


@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() , RegisterView{
    lateinit var binding: ActivityRegisterBinding
    @Inject
    lateinit var repository: AuthRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val registerPresenter = RegisterPresenter(this, repository)

        binding.registerRegisterButton.setOnClickListener {
            if (binding.nicknameEditText.text.length < MIN_USERNAME_LENGTH ||
                binding.nicknameEditText.text.isEmpty() ||
                binding.nicknameEditText.text.length > MAX_USERNAME_LENGTH) {

                binding.nicknameEditText.apply {
                    error = "Enter Valid Username"
                    requestFocus()
                    return@setOnClickListener
                }
            }

            if(binding.emailEditText.text.isEmpty()) {
                binding.emailEditText.apply {
                    error = "Please Fill"
                    requestFocus()
                    return@setOnClickListener
                }
            }
            if(binding.passwordEditText.text.isEmpty()){
                binding.passwordEditText.apply {
                    error = "Please Fill"
                    requestFocus()
                    return@setOnClickListener
                }
            }
            registerPresenter.firebaseRegister(
                binding.emailEditText.text.toString(),
                binding.nicknameEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }
        binding.registerBackButton.setOnClickListener { onBackPressed() }
    }

    override fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun registerSuccessful() {
        goOnActivity(MainActivity::class.java).also {
            finish()
        }
    }

    override fun loadingState(boolean: Boolean) {
        binding.registerProgressBar.setVisible(boolean)
    }
}