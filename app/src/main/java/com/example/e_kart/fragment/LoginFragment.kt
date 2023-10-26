package com.example.e_kart.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.e_kart.R
import com.example.e_kart.ShoppingActivity
import com.example.e_kart.databinding.FragmentLoginBinding
import com.example.e_kart.dialog.setUpBootomSheetDialog
import com.example.e_kart.util.RegistrationValidation
import com.example.e_kart.util.Resource
import com.example.e_kart.viewmodel.LoginRegisterViewmodel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding:FragmentLoginBinding
    private val viewModel:LoginRegisterViewmodel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.Register.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.LoginButton.setOnClickListener {
            val email=binding.etEmail.text.toString().trim()
            val password=binding.etPassword.text.toString().trim()
            viewModel.login(email,password)
        }

        binding.tvForgotPassword.setOnClickListener {
            setUpBootomSheetDialog {email ->
                viewModel.resetPassword(email)
            }
        }

        lifecycleScope.launch {
            viewModel.resetPassword.collect{
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        Snackbar.make(requireView(),"Reset link was send to your email",Snackbar.LENGTH_LONG).show()
                    }
                    is Resource.Error ->{
                        Snackbar.make(requireView(),"Failed to send Link",Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.loginFlow.collect{
                when(it){
                    is Resource.Loading ->{
                        binding.progressBar.visibility=View.VISIBLE
                    }
                    is Resource.Success ->{
                        binding.progressBar.visibility=View.GONE
                        gotoShoppinActivity()
//                        Toast.makeText(requireContext(),"Login SuccessFull",Toast.LENGTH_LONG).show()
                    }
                    is Resource.Error ->{
                        binding.progressBar.visibility=View.GONE
                        Toast.makeText(requireContext(),it.error.toString(), Toast.LENGTH_LONG).show()
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

    fun gotoShoppinActivity(){
        val intent = Intent(requireContext(),ShoppingActivity::class.java)
        startActivity(intent)
    }

}