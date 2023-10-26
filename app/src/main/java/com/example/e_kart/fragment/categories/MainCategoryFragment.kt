package com.example.e_kart.fragment.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_kart.R
import com.example.e_kart.adapter.BestDealAdapter
import com.example.e_kart.adapter.BestProductAdapter
import com.example.e_kart.adapter.SpecailProductAdapter
import com.example.e_kart.databinding.FragmentMainCategoryBinding
import com.example.e_kart.util.Resource
import com.example.e_kart.util.showBottomNavigation
import com.example.e_kart.viewmodel.MainCategoryViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainCategoryFragment : Fragment() {

    private lateinit var binding:FragmentMainCategoryBinding
    private lateinit var specailProductAdapter: SpecailProductAdapter
    private lateinit var bestProductAdapter: BestProductAdapter
    private lateinit var bestDealAdapter: BestDealAdapter
    private val viewmodel:MainCategoryViewmodel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentMainCategoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductRecyclerView()
        setupBestDeal()
        setUpBestProduct()

        //setting the click event
        specailProductAdapter.onclick = {
            val data = Bundle().apply {
                putParcelable("product",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,data)
        }

        bestProductAdapter.onClick = {
            val data = Bundle().apply {
                putParcelable("product",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,data)
        }

        bestDealAdapter.onClick = {
            val data = Bundle().apply {
                putParcelable("product",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,data)
        }
        //observing the special product data
        lifecycleScope.launch{
            viewmodel.specialProduct.collect{
                when(it){

                    is Resource.Loading ->{
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        specailProductAdapter.differ.submitList(it.result)
                        hideProgressBar()
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        Toast.makeText(requireContext(),"Failed to fetch the data",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


        //observing the best deal item
        lifecycleScope.launch {
            viewmodel.bestDeal.collect{
                when(it){

                    is Resource.Loading ->{
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        bestDealAdapter.differ.submitList(it.result)
                        hideProgressBar()
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        Toast.makeText(requireContext(),"Failed to fetch the data",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        //observing the best product item
        lifecycleScope.launch {
            viewmodel.bestProduct.collect{
                when(it){

                    is Resource.Loading ->{
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        bestProductAdapter.differ.submitList(it.result)
                        hideProgressBar()
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        Toast.makeText(requireContext(),"Failed to fetch the data",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setUpBestProduct() {
        bestProductAdapter= BestProductAdapter()
        binding.rvBestProductItem.apply {
            layoutManager=GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter=bestProductAdapter
        }
    }

    private fun setupBestDeal() {
        bestDealAdapter= BestDealAdapter()
        binding.rvBestDealsItem.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=bestDealAdapter
        }
    }

    private fun showProgressBar() {
        binding.progressBar2.visibility=View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar2.visibility=View.GONE
    }

    private fun setupSpecialProductRecyclerView() {
        specailProductAdapter= SpecailProductAdapter()
        binding.rvSpecialItem.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=specailProductAdapter
        }
    }

    override fun onResume() {
        super.onResume()

        showBottomNavigation()
    }

}