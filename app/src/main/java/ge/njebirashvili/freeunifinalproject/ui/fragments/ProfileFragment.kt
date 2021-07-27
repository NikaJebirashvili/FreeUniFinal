package ge.njebirashvili.freeunifinalproject.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ge.njebirashvili.freeunifinalproject.R
import ge.njebirashvili.freeunifinalproject.databinding.FragmentProfileBinding
import ge.njebirashvili.freeunifinalproject.utils.setVisible

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    lateinit var binding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<BottomAppBar>(R.id.bottom_app_bar)?.setVisible(true)
        activity?.findViewById<FloatingActionButton>(R.id.floating_action_button)?.setVisible(true)
        activity?.findViewById<FrameLayout>(R.id.main_activity_frame).also {
            it?.setPadding(0, 0, 0, 56)
        }
    }
}