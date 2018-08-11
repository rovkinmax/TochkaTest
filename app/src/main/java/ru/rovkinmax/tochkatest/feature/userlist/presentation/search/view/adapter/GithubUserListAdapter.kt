package ru.rovkinmax.tochkatest.feature.userlist.presentation.search.view.adapter

import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_github_user_info.view.*
import ru.rovkinmax.fieldsadapter.matcher.FieldViewHolder
import ru.rovkinmax.tochkatest.R
import ru.rovkinmax.tochkatest.feature.global.presentation.view.adapter.LoadingAdapter
import ru.rovkinmax.tochkatest.feature.userlist.domain.GithubUserEntity

class GithubUserListAdapter(private val listener: ((GithubUserEntity) -> Unit)) : LoadingAdapter<GithubUserEntity>() {

    init {
        addXmlLayouter(R.layout.item_github_user_info) {
            matcher { it is GithubUserEntity }
            creator { view, viewGroup -> UserViewHolder(view, viewGroup) }
        }
    }

    private inner class UserViewHolder(itemView: View, root: ViewGroup) : FieldViewHolder<GithubUserEntity>(itemView, root) {

        init {
            itemView.setOnClickListener { data?.let { listener.invoke(it) } }
        }

        override fun performBind(data: GithubUserEntity) {
            Picasso.get()
                    .load(data.avatarUrl)
                    .into(itemView.ivAvatar)

            itemView.tvUsername.text = data.login
        }
    }
}