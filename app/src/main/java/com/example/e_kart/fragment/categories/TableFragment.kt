package com.example.e_kart.fragment.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.e_kart.datamodel.Category
import com.example.e_kart.factory.BaseCategoryViewmodelFactory
import com.example.e_kart.util.Resource
import com.example.e_kart.viewmodel.CategoryViewmodel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@AndroidEntryPoint
class TableFragment:BaseCategoryFragment() {
    @Inject
    lateinit var firestore: FirebaseFirestore
    val viewModel by viewModels<CategoryViewmodel> {
        BaseCategoryViewmodelFactory(firestore, Category.Table)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launchWhenStarted {
            viewModel.offerProduct.collectLatest {
                when(it){

                    is Resource.Loading -> {
                        showProgressbar()
                    }
                    is Resource.Success ->{
                        offerProductAdapter.differ.submitList(it.result)
                        hideProgressBar()
                    }
                    is Resource.Error ->{
                        hideProgressBar()
                        Snackbar.make(
                            requireView(),
                            it.error.toString(),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProduct.collectLatest {it ->
                when(it){

                    is Resource.Loading -> {

                    }
                    is Resource.Success ->{
                        bestProductAdapter.differ.submitList(it.result)
                    }
                    is Resource.Error ->{


                        Snackbar.make(
                            requireView(),
                            it.error.toString(),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    else -> Unit
                }
            }
        }

    }
}