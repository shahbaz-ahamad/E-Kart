package com.example.e_kart.fragment.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_kart.R
import com.example.e_kart.adapter.BestProductAdapter
import com.example.e_kart.databinding.FragmentBaseCategoryBinding
import com.example.e_kart.util.showBottomNavigation


open class BaseCategoryFragment : Fragment() {

    private lateinit var binding:FragmentBaseCategoryBinding
    protected val offerProductAdapter: BestProductAdapter by lazy{
        BestProductAdapter()
    }
    protected val bestProductAdapter: BestProductAdapter by lazy {
        BestProductAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentBaseCategoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOfferRecyclerview()
        setupBestProductRecyclerview()

        //setting the click event
        offerProductAdapter.onClick = {
            val data = Bundle().apply {
                putParcelable("product",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,data)
        }
        //setting the click event
        bestProductAdapter.onClick = {
            val data = Bundle().apply {
                putParcelable("product",it)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,data)
        }


    }

    fun setupBestProductRecyclerview() {
        binding.rvBestProductItem.apply {
            layoutManager=GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter=bestProductAdapter
        }
    }

     fun setupOfferRecyclerview() {
        binding.recOffer.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=offerProductAdapter
        }
    }
    fun showProgressbar(){
        binding.progressBar2.visibility=View.VISIBLE
    }

    fun hideProgressBar(){
        binding.progressBar2.visibility=View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }

}