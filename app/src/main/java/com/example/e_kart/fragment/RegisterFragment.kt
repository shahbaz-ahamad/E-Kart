package com.example.e_kart.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.e_kart.R
import com.example.e_kart.databinding.FragmentRegisterBinding
import com.example.e_kart.util.RegistrationValidation
import com.example.e_kart.util.Resource
import com.example.e_kart.viewmodel.LoginRegisterViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel:LoginRegisterViewmodel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.LoginTv.setOnClickListener {
            it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.registerButton.setOnClickListener {


            val f_name=binding.etFirstName.text.toString()
            val l_name=binding.etLastName.text.toString()
            val name=f_name+" "+l_name
            val email=binding.etEmail.text.toString()
            val password=binding.etPassword.text.toString()

            viewModel.signUp(name,email,password,"")
        }


        lifecycleScope.launch {
            viewModel.signUpFlow.collect{

                when(it){
                    is Resource.Loading -> {
                        binding.progressBar.visibility=View.VISIBLE

                    }
                    is Resource.Success ->{
                        binding.progressBar.visibility=View.GONE
                        Toast.makeText(requireContext(),"Registration SuccesFull",Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

                    }
                    is Resource.Error ->{
                            binding.progressBar.visibility=View.GONE
                            Toast.makeText(requireContext(),it.error.toString(),Toast.LENGTH_LONG).show()
                    }

                    else -> {}
                }


            }
        }

        lifecycleScope.launch {

            viewModel.registrationValidationFlow.collect{
                if(it.email is RegistrationValidation.Failed){
                    binding.etEmail.apply {
                        requestFocus()
                        error=it.email.msg
                    }

                }

                if(it.password is RegistrationValidation.Failed){
                    binding.etPassword.apply {
                        requestFocus()
                        error=it.password.msg
                    }
                }
            }
        }

    }
}