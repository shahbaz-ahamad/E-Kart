package com.example.e_kart.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.e_kart.MainActivity
import com.example.e_kart.R
import com.example.e_kart.databinding.FragmentProfileBinding
import com.example.e_kart.util.Resource1
import com.example.e_kart.viewmodel.ProfileViewmodel
import com.google.firebase.BuildConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding
    private val viewmodel by viewModels<ProfileViewmodel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.constraintProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_userAccountFragment)
        }

        binding.linearAllOrders.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_allOrderFragment)
        }

        binding.linearLogOut.setOnClickListener {
            viewmodel.logout()
            val intent = Intent(requireActivity(),MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.tvVersion.text = "Version: ${BuildConfig.VERSION_NAME}"

        lifecycleScope.launchWhenStarted {
            viewmodel.user.collectLatest {
                when (it) {
                    is Resource1.Loading -> {
                        binding.progressbarSettings.visibility = View.VISIBLE
                    }
                    is Resource1.Success -> {
                        binding.progressbarSettings.visibility = View.GONE
                        Glide.with(requireView()).load(it.data!!.imagePath).error(
                            ColorDrawable(
                            Color.BLACK)
                        ).into(binding.imageUser)
                        binding.tvUserName.text = "${it.data.firstName}"
                    }
                    is Resource1.Error -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                        binding.progressbarSettings.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }
    }

}