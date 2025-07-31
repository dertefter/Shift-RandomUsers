package com.dertefter.shiftrandomusers.ui.user_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dertefter.shiftrandomusers.R
import com.dertefter.shiftrandomusers.databinding.FragmentUserDetailBinding
import com.dertefter.shiftrandomusers.databinding.FragmentUserListBinding
import com.dertefter.shiftrandomusers.ui.user_list.UserListAdapter
import com.dertefter.shiftrandomusers.ui.user_list.UserListViewModel
import com.google.android.material.color.MaterialColors
import com.google.android.material.shape.MaterialShapeDrawable
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class UserDetailFragment : Fragment() {
    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!

    private val args: UserDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

        val user = args.user

        ViewCompat.setOnApplyWindowInsetsListener(binding.scrollView) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.scrollView.updatePadding(
                bottom = insets.bottom,
            )

            binding.appbar.updatePadding(bottom = 0)

            WindowInsetsCompat.CONSUMED
        }

        Picasso.get().load(user.picture.large).into(binding.picture)
        binding.name.text =  "${user.name.first} ${user.name.last}"
        binding.mail.text = user.email
        binding.phone.text = user.phone
        binding.cell.text = user.cell
        binding.location.text = "${user.location.country}, ${user.location.city}\n${user.location.street.name}, ${user.location.street.number}"
        binding.dob.text = "${user.dob.getLocalDate().format(formatter)}\nAge: ${user.dob.age}"
        binding.account.text = "Username: ${user.login.username}\nuuid: ${user.login.uuid}"

        binding.phone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData("tel:${binding.phone.text}".toUri())
            startActivity(intent)
        }

        binding.cell.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData("tel:${binding.cell.text}".toUri())
            startActivity(intent)
        }

        binding.mailCard.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.setData("mailto:${user.email}".toUri())
            intent.putExtra(Intent.EXTRA_EMAIL, user.email)
            startActivity(intent)
        }

        binding.locationCard.setOnClickListener {
            val uri = ("geo:" + user.location.coordinates.latitude + ","
                    + user.location.coordinates.longitude)
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    uri.toUri()
                )
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}