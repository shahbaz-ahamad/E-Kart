package com.example.e_kart.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_kart.R
import com.example.e_kart.databinding.FragmentAddressBinding
import com.example.e_kart.datamodel.Address
import com.example.e_kart.util.Resource1
import com.example.e_kart.viewmodel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class AddressFragment : Fragment() {

    private lateinit var binding : FragmentAddressBinding
    private  val viewmodel by viewModels<AddressViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //onserve the lifecycle
        lifecycleScope.launchWhenStarted {

            viewmodel.addnewAddress.collectLatest {
                when(it){

                    is Resource1.Loading ->{
                        binding.progressbarAddress.visibility=View.VISIBLE
                    }
                    is Resource1.Success ->{
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        findNavController().navigateUp()
                    }
                    is Resource1.Error ->{
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }else ->Unit
                }
            }
        }


        //for thr validation
        lifecycleScope.launchWhenStarted {
            viewmodel.error.collectLatest {
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddressBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.buttonSave.setOnClickListener {
            binding.apply {
                val addresstitle = edAddressTitle.text.toString()
                val full_name = edFullName.text.toString()
                val street = edStreet.text.toString()
                val phone = edPhone.text.toString()
                val city = edCity.text.toString()
                val state =edState.text.toString()

                val address = Address(addresstitle,full_name,street,phone,city,state)
                viewmodel.addAddress(address)
            }
        }

        binding.imageAddressClose.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}