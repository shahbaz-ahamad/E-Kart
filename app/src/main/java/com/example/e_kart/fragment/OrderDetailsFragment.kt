package com.example.e_kart.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_kart.R
import com.example.e_kart.adapter.BillingProductAdapter
import com.example.e_kart.databinding.FragmentOrderDetailsBinding
import com.example.e_kart.datamodel.order.OrderStatus
import com.example.e_kart.datamodel.order.getOrderStatus
import com.example.e_kart.util.VerticalItemDecroration
import com.example.e_kart.util.hideBottomNavigation
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderDetailsFragment : Fragment() {

    private lateinit var binding: FragmentOrderDetailsBinding
    private val billingProductsAdapter by lazy { BillingProductAdapter() }
    private val args by navArgs<OrderDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val order= args.order
        hideBottomNavigation()
        setupOrderRv()

        binding.imageCloseOrder.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.apply {
            tvOrderId.text="Order #${order.orderId}"


            //for the step view
            stepView.setSteps(
                mutableListOf(
                    OrderStatus.Ordered.status,
                    OrderStatus.Confirmed.status,
                    OrderStatus.Shipped.status,
                    OrderStatus.Delivered.status
                )
            )

            val currentorderStatus = when( getOrderStatus(order.orderStatus)){

                is OrderStatus.Ordered -> 0
                is OrderStatus.Confirmed -> 1
                is OrderStatus.Shipped -> 2
                is OrderStatus.Delivered -> 3
                else -> 0
            }

            stepView.go(currentorderStatus,false)
            if (currentorderStatus==3){
                stepView.done(true)
            }

            tvFullName.text=order.address.fullname
            tvAddress.text=order.address.street
            tvPhoneNumber.text="+91-${order.address.phone}"
            tvTotalPrice.text=order.totalPrice.toString()


        }

        billingProductsAdapter.differ.submitList(order.product)
    }

    private fun setupOrderRv() {
        binding.rvProducts.apply {
            adapter = billingProductsAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(VerticalItemDecroration())
        }
    }
}