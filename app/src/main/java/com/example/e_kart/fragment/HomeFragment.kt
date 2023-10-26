package com.example.e_kart.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_kart.R
import com.example.e_kart.adapter.HomeViewpgaerAdapter
import com.example.e_kart.databinding.FragmentHomeBinding
import com.example.e_kart.fragment.categories.AccessoryFragment
import com.example.e_kart.fragment.categories.ChairFragment
import com.example.e_kart.fragment.categories.CupbaoardFragment
import com.example.e_kart.fragment.categories.FurnitureFragment
import com.example.e_kart.fragment.categories.MainCategoryFragment
import com.example.e_kart.fragment.categories.TableFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryFragment = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CupbaoardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment()
        )

        //TO CANCEL SWIPEABLE BEHAVOUR OF VIEWPAGER
        binding.viewPager.isUserInputEnabled=false

        val viewPager2Adapter=
            HomeViewpgaerAdapter(categoryFragment,childFragmentManager,lifecycle)
        binding.viewPager.adapter=viewPager2Adapter
        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,position ->

            when(position){
                0 -> tab.text="Home"
                1-> tab.text="Chair"
                2-> tab.text="Cupboard"
                3-> tab.text="Table"
                4-> tab.text="Accessory"
                5-> tab.text="Furniture"

            }
        }.attach()
    }

}