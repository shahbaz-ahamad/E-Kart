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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_kart.R
import com.example.e_kart.adapter.AllorderAdapter
import com.example.e_kart.databinding.FragmentAllOrderBinding
import com.example.e_kart.util.Resource1
import com.example.e_kart.viewmodel.OrderViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AllOrderFragment : Fragment() {

    private lateinit var binding : FragmentAllOrderBinding
    val viewModel by viewModels<OrderViewmodel>()
    val ordersAdapter by lazy { AllorderAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAllOrderBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOrdersRv()

        lifecycleScope.launchWhenStarted {
            viewModel.allorder.collectLatest {
                when (it) {
                    is Resource1.Loading -> {
                        binding.progressbarAllOrders.visibility = View.VISIBLE
                    }
                    is Resource1.Success -> {
                        binding.progressbarAllOrders.visibility = View.GONE
                        ordersAdapter.differ.submitList(it.data)
                        if (it.data.isNullOrEmpty()) {
                            binding.tvEmptyOrders.visibility = View.VISIBLE
                        }
                    }
                    is Resource1.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.progressbarAllOrders.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }

        ordersAdapter.onClick = {
                val action = AllOrderFragmentDirections.actionAllOrderFragmentToOrderDetailsFragment(it)
                findNavController().navigate(action)
        }

        binding.imageCloseOrders.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    private fun setupOrdersRv() {
        binding.rvAllOrders.apply {
            adapter = ordersAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

}
