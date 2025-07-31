package com.dertefter.shiftrandomusers.ui.user_list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dertefter.shiftrandomusers.databinding.FragmentUserListBinding
import com.google.android.material.shape.MaterialShapeDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserListFragment : Fragment() {
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: UserListAdapter

    private val viewModel: UserListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            binding.recyclerView.updatePadding(
                bottom = insets.bottom
            )
            WindowInsetsCompat.CONSUMED
        }
        binding.appbar.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(context)


        adapter = UserListAdapter(
            onItemClick = {
                val action = UserListFragmentDirections
                    .actionUserListFragmentToUserDetailFragment(it)
                findNavController().navigate(action)
            },
            onLocationButton = {
                val uri = ("geo:" + it.coordinates.latitude + ","
                        + it.coordinates.longitude)
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        uri.toUri()
                    )
                )

            },




            onEmailClick = {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.setData("mailto:$it".toUri())
                intent.putExtra(Intent.EXTRA_EMAIL, it)
                startActivity(intent)
            },
            onPhoneClick = {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.setData("tel:$it".toUri())
                startActivity(intent)
            },
        )

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.done.setOnClickListener {
            viewModel.refreshUsers()
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.users.collect { users ->
                        users?.let {
                            adapter.submitUsers(it)
                            if (it.isEmpty()) {
                                viewModel.refreshUsers()
                            }
                        }
                    }
                }

                launch {
                    viewModel.uiStatus.collect { status ->
                        binding.loading.isVisible = status == UiStatus.LOADING
                        binding.done.isVisible = status != UiStatus.LOADING
                        binding.error.isVisible = status == UiStatus.ERROR
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}