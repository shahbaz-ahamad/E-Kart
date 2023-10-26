package com.example.e_kart.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.e_kart.R
import com.example.e_kart.databinding.FragmentAccountOptionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountOptionFragment : Fragment() {

    private lateinit var binding:FragmentAccountOptionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAccountOptionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_accountOptionFragment_to_registerFragment)
        }

        binding.loginButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_accountOptionFragment_to_loginFragment)
        }
    }

}