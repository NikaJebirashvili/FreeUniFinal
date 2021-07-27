package ge.njebirashvili.freeunifinalproject.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ge.njebirashvili.freeunifinalproject.R
import ge.njebirashvili.freeunifinalproject.databinding.FragmentProfileBinding
import ge.njebirashvili.freeunifinalproject.model.UpdateProfile
import ge.njebirashvili.freeunifinalproject.model.User
import ge.njebirashvili.freeunifinalproject.presenter.ProfilePresenter
import ge.njebirashvili.freeunifinalproject.repository.MainRepository
import ge.njebirashvili.freeunifinalproject.ui.LoginActivity
import ge.njebirashvili.freeunifinalproject.utils.setVisible
import ge.njebirashvili.freeunifinalproject.views.ProfileView
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile), ProfileView {

    lateinit var binding: FragmentProfileBinding
    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var repository: MainRepository
    lateinit var presenter: ProfilePresenter
    private var user: User? = null
    private var profilePictureUri : Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){
            profilePictureUri = it
            Glide.with(this)
                .load(profilePictureUri)
                .centerCrop()
                .into(binding.profileImageView)
        }
        presenter = ProfilePresenter(this, repository)
        activity?.findViewById<BottomAppBar>(R.id.bottom_app_bar)?.setVisible(true)
        activity?.findViewById<FloatingActionButton>(R.id.floating_action_button)?.setVisible(true)
        activity?.findViewById<FrameLayout>(R.id.main_activity_frame).also {
            it?.setPadding(0, 0, 0, 56)
        }
        lifecycleScope.launch {
            getUser()
            initProfileScreen()
        }
        binding.profileSignOut.setOnClickListener {
            lifecycleScope.launch {
                presenter.signOut()
                val intent = Intent(activity,LoginActivity::class.java)
                activity?.startActivity(intent)
                activity?.finish()
            }
        }
        binding.profileUpdateButton.setOnClickListener {
            val updateProfile = UpdateProfile(
                binding.profileNicknameEditText.text.toString(),
                binding.profileWhatIDo.text.toString(),
                profilePictureUri
            )
            lifecycleScope.launch {
                presenter.updateProfile(updateProfile)
            }
        }

        binding.profileImageView.setOnClickListener {
            getContent.launch("image/*")
        }
    }


    private suspend fun getUser() {
        user = presenter.getUser(auth.uid!!)
    }

    override fun loadingState(boolean: Boolean) {
        binding.profileProgressBar.setVisible(boolean)
    }

    override fun signOut() {
        Toast.makeText(requireContext(),"Successfully Signed out",Toast.LENGTH_LONG).show()
    }

    private fun initProfileScreen() {
        user.let {
            Glide.with(this)
                .load(it?.profilePictureUrl)
                .centerCrop()
                .into(binding.profileImageView)
            binding.profileNicknameEditText.setText(it?.username)
            binding.profileWhatIDo.setText(it?.whatIDo)
        }
    }
}