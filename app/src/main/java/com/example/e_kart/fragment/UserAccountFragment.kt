package com.example.e_kart.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.e_kart.R
import com.example.e_kart.databinding.FragmentUserAccountBinding
import com.example.e_kart.datamodel.User
import com.example.e_kart.util.Resource
import com.example.e_kart.util.Resource1
import com.example.e_kart.viewmodel.UserAccountViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class UserAccountFragment : Fragment() {

    private lateinit var binding:FragmentUserAccountBinding
    private val viewmodel by viewModels<UserAccountViewmodel>()

    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentUserAccountBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewmodel.userState.collectLatest {
                when (it) {
                    is Resource1.Loading -> {
                        binding.progressbar.visibility=View.VISIBLE
                    }

                    is Resource1.Success -> {
                        binding.progressbar.visibility=View.INVISIBLE
                        showUserInformation(it.data!!)
                    }

                    is Resource1.Error -> {
                        binding.progressbar.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }


        lifecycleScope.launchWhenStarted {
            viewmodel.editInfo.collectLatest {
                when (it) {
                    is Resource1.Loading -> {
                        binding.progressbar.visibility=View.VISIBLE
                    }

                    is Resource1.Success -> {
                        binding.progressbar.visibility=View.INVISIBLE

                    }

                    is Resource1.Error -> {
                        binding.progressbar.visibility=View.INVISIBLE
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        binding.imageCloseUserAccount.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonSave.setOnClickListener {
            binding.apply {
                val firstName = edFirstName.text.toString().trim()
                val email = edEmail.text.toString().trim()
                val user = User(firstName,"" ,email)
                viewmodel.updateUserInfo(user,imageUri)
            }
        }
        binding.imageEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            imageActivityResultLauncher.launch(intent)
        }
    }

    private fun showUserInformation(data: User) {
        binding.apply {
            Glide.with(this@UserAccountFragment).load(data.imagePath).error(ColorDrawable(Color.BLACK)).into(imageUser)
            edFirstName.setText(data.firstName)
            edEmail.setText(data.email)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                imageUri = it.data?.data
                Glide.with(this).load(imageUri).into(binding.imageUser)
            }
    }
}