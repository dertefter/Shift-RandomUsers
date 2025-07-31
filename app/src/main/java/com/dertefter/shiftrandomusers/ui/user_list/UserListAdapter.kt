package com.dertefter.shiftrandomusers.ui.user_list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.dertefter.shiftrandomusers.R
import com.dertefter.shiftrandomusers.data.model.Location
import com.dertefter.shiftrandomusers.data.model.User
import com.dertefter.shiftrandomusers.data.model.UserItem
import com.dertefter.shiftrandomusers.databinding.ItemUserBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.squareup.picasso.Picasso

class UserListAdapter(
    private val onItemClick: (User) -> Unit,
    private val onPhoneClick: (String) -> Unit,
    private val onEmailClick: (String) -> Unit,
    private val onLocationButton: (Location) -> Unit
): RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    private var items: List<UserItem> = emptyList()

    fun submitUsers(users: List<User>) {
        items = users.map { UserItem(it) }
        notifyDataSetChanged()
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) = with(binding) {
            val item = items[position]
            val user = item.user

            root.setOnClickListener {
                onItemClick(user)
            }

            moreButton.setOnClickListener {
                onItemClick(user)
            }

            phoneButton.setOnClickListener {
                onPhoneClick(user.phone)
            }

            locationButton.setOnClickListener {
                onLocationButton(user.location)
            }

            mailButton.setOnClickListener {
                onEmailClick(user.email)
            }

            name.text = "${user.name.first} ${user.name.last}"
            mailButton.text = user.email

            nameExp.text = "${user.name.first} ${user.name.last}"
            phoneButton.text = user.phone
            locationButton.text =  "${user.location.country} ${user.location.city}"

            Picasso.get().load(user.picture.medium).into(picture)
            Picasso.get().load(user.picture.large).into(pictureExp)

            expanded.isGone = !item.isExpanded
            collapsed.isGone = item.isExpanded

            val isFirst = position == 0
            val isLast = position == itemCount - 1
            root.setCustomCornerRadii(isFirst, isLast, itemView.context)

            expandButton.setOnClickListener {
                item.isExpanded = true
                notifyItemChanged(position)
            }

            collapse.setOnClickListener {
                item.isExpanded = false
                notifyItemChanged(position)
            }
        }

        fun MaterialCardView.setCustomCornerRadii(
            isFirst: Boolean,
            isLast: Boolean,
            context: Context
        ) {
            val radiusMax = context.resources.getDimension(R.dimen.radius)
            val radiusMicro = context.resources.getDimension(R.dimen.radius_micro)

            val shapeBuilder = ShapeAppearanceModel.Builder()

            if (isFirst && isLast) {
                shapeBuilder.setAllCornerSizes(radiusMax)
            } else if (isFirst) {
                shapeBuilder
                    .setTopLeftCorner(CornerFamily.ROUNDED, radiusMax)
                    .setTopRightCorner(CornerFamily.ROUNDED, radiusMax)
                    .setBottomLeftCorner(CornerFamily.ROUNDED, radiusMicro)
                    .setBottomRightCorner(CornerFamily.ROUNDED, radiusMicro)
            } else if (isLast) {
                shapeBuilder
                    .setTopLeftCorner(CornerFamily.ROUNDED, radiusMicro)
                    .setTopRightCorner(CornerFamily.ROUNDED, radiusMicro)
                    .setBottomLeftCorner(CornerFamily.ROUNDED, radiusMax)
                    .setBottomRightCorner(CornerFamily.ROUNDED, radiusMax)
            } else {
                shapeBuilder.setAllCornerSizes(radiusMicro)
            }

            shapeAppearanceModel = shapeBuilder.build()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = items.size
}

