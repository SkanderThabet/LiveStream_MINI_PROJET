package com.projet.miniprojet.androidVox.viewHolder

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.projet.miniprojet.androidVox.R
import com.projet.miniprojet.androidVox.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(user: User) = with(itemView) {
        countTV.isVisible = false
        timeTV.isVisible = false

        titleTV.text = user.name
        subtitleTV.text = user.status

        Picasso.get()
            .load(user.picture)
            .placeholder(R.drawable.ic_baseline_account_circle_24)
            .error(R.drawable.ic_baseline_account_circle_24)
            .into(userImgView)
    }
}