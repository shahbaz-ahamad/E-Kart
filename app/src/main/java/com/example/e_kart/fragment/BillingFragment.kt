package com.example.e_kart.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
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
import com.example.e_kart.adapter.AddressAdapter
import com.example.e_kart.adapter.BillingProductAdapter
import com.example.e_kart.databinding.FragmentBillingBinding
import com.example.e_kart.datamodel.Address
import com.example.e_kart.datamodel.CartProduct
import com.example.e_kart.datamodel.order.Order
import com.example.e_kart.datamodel.order.OrderStatus
import com.example.e_kart.util.HorizontalItemDecoration
import com.example.e_kart.util.Resource1
import com.example.e_kart.viewmodel.BillingViewmodel
import com.example.e_kart.viewmodel.OrderViewmodel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest



@AndroidEntryPoint
class BillingFragment : Fragment() {

    private lateinit var binding : FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingAdapter by lazy { BillingProductAdapter() }
    private val viewmodel by viewModels<BillingViewmodel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var product = emptyList<CartProduct>()
    private var totalPrice = 0f
    private var selectedAddress :Address? = null
    private val orderviewmodel by viewModels<OrderViewmodel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        product = args.products.toList()
        totalPrice=args.price
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentBillingBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupBillingProductRV()
        setupAddressRV()

        lifecycleScope.launchWhenStarted {
            viewmodel.address.collectLatest {
                when(it){

                    is Resource1.Loading ->{
                        binding.progressbarAddress.visibility=View.VISIBLE
                    }
                    is Resource1.Success ->{
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        addressAdapter.differ.submitList(it.data)
                        billingAdapter.differ.submitList(product)
                        binding.tvTotalPrice.text = "${totalPrice}"
                    }
                    is Resource1.Error ->{
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else ->Unit
                }
            }
        }


        //placing the order
        lifecycleScope.launchWhenStarted {
            orderviewmodel.order.collectLatest {
                when(it){

                    is Resource1.Loading ->{
                        binding.progressbarAddress.visibility=View.VISIBLE
                    }
                    is Resource1.Success ->{
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        findNavController().navigateUp()
                        Snackbar.make(requireView(), "Order Placed", Snackbar.LENGTH_LONG).show()

                    }
                    is Resource1.Error ->{
                        binding.progressbarAddress.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else ->Unit
                }
            }
        }


        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }
        binding.imageCloseBilling.setOnClickListener {
            findNavController().navigateUp()
        }


        //to get the selected address
        addressAdapter.onclick ={
            selectedAddress=it
        }
        binding.buttonPlaceOrder.setOnClickListener {

            if(selectedAddress != null){

                showOrderConfirmationDialogue()
            }else{
                Toast.makeText(requireContext(),"Select Address",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showOrderConfirmationDialogue() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Order Item")
                .setMessage("Do you want to Order Your Cart Item.")
                .setNegativeButton("Cancel"){dialogue,_ ->
                    dialogue.dismiss()
                }
                .setPositiveButton("Yes"){dialogue,_ ->

                    var order = Order(OrderStatus.Ordered.status,
                        totalPrice,
                        product,
                        selectedAddress!!)

                    orderviewmodel.placeOrder(order)
                    dialogue.dismiss()
                }
        }
        alertDialog.create()
        alertDialog.show()
    }

    private fun setupAddressRV() {
        binding.rvAddress.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=addressAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun setupBillingProductRV() {
        binding.rvProducts.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=billingAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }
}