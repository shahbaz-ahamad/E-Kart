package com.example.e_kart.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_kart.Firebase.FirebaseDB
import com.example.e_kart.R
import com.example.e_kart.adapter.CartAdapter
import com.example.e_kart.databinding.FragmentCartBinding
import com.example.e_kart.util.Resource1
import com.example.e_kart.util.VerticalItemDecroration
import com.example.e_kart.viewmodel.CartViewodel
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CartFragment : Fragment() {

    private lateinit var binding:FragmentCartBinding
    val cartAdapter by lazy { CartAdapter() }
    val viewModel by activityViewModels<CartViewodel>()
    private var priceItem : Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCartBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecView()
        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when(it){
                    is Resource1.Success ->{
                        binding.progressCart.visibility=View.INVISIBLE
                        if(it.data!!.isEmpty()){
                            showEmptyCart()
                            hideOtherView()
                        }else{
                            hideEmptyCart()
                            showOtherView()
                            cartAdapter.differ.submitList(it.data)

                        }
                    }
                    is Resource1.Loading -> {
                        binding.progressCart.visibility=View.VISIBLE
                    }
                    is Resource1.Error -> {
                        binding.progressCart.visibility=View.INVISIBLE
                    }else -> Unit
                }
            }
        }


        //soserve the total price
        lifecycleScope.launch {
            viewModel.productPrize.collectLatest {price ->
                price?.let {
                    priceItem=it
                    binding.priceTv.text="Rs:${price}"
                }
            }
        }


        //click event on the cart item
        cartAdapter.onProductclick = {
            val b = Bundle().apply {
                putParcelable("product",it.product)
            }
            findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment,b)
        }


        //to increase the cartitem quamtity
        cartAdapter.onPlusclick ={
            viewModel.changeQuantity(it,FirebaseDB.quantityChanging.INCREASAE)
        }


        //to decrease
        cartAdapter.onMinusclick ={
            viewModel.changeQuantity(it,FirebaseDB.quantityChanging.DECREASE)
        }

        lifecycleScope.launch {
            viewModel.deletedailog.collectLatest {
                val alertDialog =AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete Item From Cart")
                        .setMessage("Do you want to delete this item form cart.")
                        .setNegativeButton("Cancel"){dialogue,_ ->
                            dialogue.dismiss()
                        }
                        .setPositiveButton("Delete"){dialogue,_ ->

                            viewModel.deleteCartItem(it)
                            dialogue.dismiss()
                        }
                }
                alertDialog.create()
                alertDialog.show()
            }
        }

        cartAdapter.onLongClick ={
            val alertDialog =AlertDialog.Builder(requireContext()).apply {
                setTitle("Delete Item From Cart")
                    .setMessage("Do you want to delete this item form cart.")
                    .setNegativeButton("Cancel"){dialogue,_ ->
                        dialogue.dismiss()
                    }
                    .setPositiveButton("Delete"){dialogue,_ ->

                        viewModel.deleteCartItem(it)
                        dialogue.dismiss()
                    }
            }
            alertDialog.create()
            alertDialog.show()
            
        }


        binding.buttonCheckOut.setOnClickListener {

            val action = CartFragmentDirections.actionCartFragmentToBillingFragment(priceItem,cartAdapter.differ.currentList.toTypedArray())
            findNavController().navigate(action)
        }
        binding.imgCloseCart.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showOtherView() {
        binding.cartRecyclerView.visibility=View.VISIBLE
        binding.totalBoxContainer.visibility=View.VISIBLE
        binding.buttonCheckOut.visibility=View.VISIBLE
    }

    private fun hideOtherView() {
        binding.totalBoxContainer.visibility=View.INVISIBLE
        binding.buttonCheckOut.visibility=View.INVISIBLE
        binding.cartRecyclerView.visibility=View.INVISIBLE
    }


    private fun hideEmptyCart() {
        binding.emptyCart.visibility=View.INVISIBLE

    }

    private fun showEmptyCart() {
        binding.emptyCart.visibility=View.VISIBLE

    }
private fun setupRecView() {
    binding.cartRecyclerView.apply {
        layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        adapter=cartAdapter
        addItemDecoration(VerticalItemDecroration())
    }
}


}
