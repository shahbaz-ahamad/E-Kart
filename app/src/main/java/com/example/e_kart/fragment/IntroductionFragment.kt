package com.example.e_kart.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.e_kart.R
import com.example.e_kart.ShoppingActivity
import com.example.e_kart.databinding.FragmentIntroductionBinding
import com.example.e_kart.viewmodel.IntroductionViewmodel
import com.example.e_kart.viewmodel.IntroductionViewmodel.Companion.ACCOUNT_OPTION_FRAGMENT
import com.example.e_kart.viewmodel.IntroductionViewmodel.Companion.SHOPPING_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroductionFragment : Fragment() {

    private lateinit var binding:FragmentIntroductionBinding
    private val viewmodel:IntroductionViewmodel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentIntroductionBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.start.setOnClickListener {
            viewmodel.startButtonClicked()
            it.findNavController().navigate(R.id.action_introductionFragment_to_accountOptionFragment)
        }

        lifecycleScope.launch {
            viewmodel.navigateState.collect{
                when(it){
                    SHOPPING_ACTIVITY -> {
                        gotoShoppinActivity()
                    }
                    ACCOUNT_OPTION_FRAGMENT ->{
                        findNavController().navigate(it)
                    }else ->Unit
                }
            }
        }
    }

    fun gotoShoppinActivity(){
        val intent = Intent(requireContext(), ShoppingActivity::class.java)
        startActivity(intent)
    }


}