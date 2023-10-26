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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_kart.R
import com.example.e_kart.adapter.Color
import com.example.e_kart.adapter.Size
import com.example.e_kart.adapter.ViewPage2Images
import com.example.e_kart.databinding.FragmentProductDetailsBinding
import com.example.e_kart.datamodel.CartProduct
import com.example.e_kart.util.Resource
import com.example.e_kart.util.hideBottomNavigation
import com.example.e_kart.viewmodel.ProductDetailsViewmodel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private val args by navArgs<ProductDetailsFragmentArgs>()//to get the percelize data
    private lateinit var binding:FragmentProductDetailsBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    //intializing the adapter
    protected  val viewPagerAdapter by lazy { ViewPage2Images() }
    protected val colorAdapter by lazy { Color() }
    protected val sizeAdapter by lazy { Size() }

    private val viewModel by viewModels<ProductDetailsViewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        hideBottomNavigation()
        // Inflate the layout for this fragment
        binding=FragmentProductDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore=FirebaseFirestore.getInstance()
        auth=Firebase.auth

        //to fetch the product
        val product = args.product
        setupSizeRV()
        setupColorRV()
        setupViewpagerRV()

        //selecting the size and color
        onColorClick()
        onSizeClick()

        //setting the data
        binding.apply {
            tvProductName.text=product.name
            tvProductDescription.text=product.description
            tvProductPrice.text="$${product.price.toString()}"
        }

        viewPagerAdapter.differ.submitList(product.images)
        product.colors?.let {
            colorAdapter.differ.submitList(it)
        }
        product.sizes?.let {
            sizeAdapter.differ.submitList(it)
        }


        //to close the detail fragment
        binding.closeDetailFragment.setOnClickListener{
            findNavController().navigateUp()
        }

        if(product.colors.isNullOrEmpty()){
            binding.tvProductColor.visibility=View.GONE
        }
        if(product.sizes.isNullOrEmpty()){
            binding.tvProductSize.visibility=View.GONE
        }

        binding.buttonAddToCart.setOnClickListener {
            binding.progressbar.visibility=View.VISIBLE
            viewModel.addUpdateProductInCart(CartProduct(product,1,selectedColor,selectedSize))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                    }

                    is Resource.Success -> {
                        binding.progressbar.visibility=View.INVISIBLE
                        binding.buttonAddToCart.setBackgroundColor(resources.getColor(R.color.black))
                    }

                    is Resource.Error -> {
                        binding.progressbar.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(), it.error.toString(), Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }


    private fun setupViewpagerRV() {
        binding.apply {
            viewpageProductImage.adapter=viewPagerAdapter
        }
    }

    private fun setupColorRV() {
        binding.apply {
            rvColor.adapter=colorAdapter
            rvColor.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setupSizeRV() {
        binding.apply {
            rvSize.adapter=sizeAdapter
            rvSize.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private var selectedSize: String? =null
    private fun onSizeClick() {
        sizeAdapter.onItemClick = { size ->
            selectedSize = size
        }
    }

    private var selectedColor: Int? = null
    private fun onColorClick() {
        colorAdapter.onItemClick = { color ->
            selectedColor = color
        }
    }
}
