package com.june0122.sunflower.model.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Plant(
    val imageUrl: String,
    val name: String,
    val description: String,
) : Parcelable

data class Users(
    @field:SerializedName("") val incomplete_results: Boolean,
    val items: List<User>,
    val total_count: Int
)

data class User(
    @field:SerializedName("avatar_url") val avatarUrl: String,
    @field:SerializedName("events_url") val eventsUrl: String,
    @field:SerializedName("followers_url") val followersUrl: String,
    @field:SerializedName("following_url") val followingUrl: String,
    @field:SerializedName("gists_url") val gistsUrl: String,
    @field:SerializedName("gravatar_id") val gravatarId: String,
    @field:SerializedName("html_url") val htmlUrl: String,
    val id: Int,
    val login: String,
    @field:SerializedName("node_id") val nodeId: String,
    @field:SerializedName("organizations_url") val organizationsUrl: String,
    @field:SerializedName("received_events_url") val receivedEventsUrl: String,
    @field:SerializedName("repos_url") val reposUrl: String,
    val score: Double,
    @field:SerializedName("site_admin") val siteAdmin: Boolean,
    @field:SerializedName("starred_url") val starredUrl: String,
    @field:SerializedName("subscriptions_url") val subscriptionsUrl: String,
    val type: String,
    val url: String
)

data class UserDetail(
    @field:SerializedName("avatar_url") val avatarUrl: String,
    val bio: String,
    val blog: String,
    val company: String,
    @field:SerializedName("created_at") val createdAt: String,
    val email: String,
    @field:SerializedName("events_url") val eventsUrl: String,
    val followers: Int,
    @field:SerializedName("followers_url") val followersUrl: String,
    val following: Int,
    @field:SerializedName("following_url") val followingUrl: String,
    @field:SerializedName("gists_url") val gistsUrl: String,
    @field:SerializedName("gravatar_id") val gravatarId: String,
    val hireable: Boolean,
    @field:SerializedName("html_url") val htmlUrl: String,
    val id: Int,
    val location: String,
    val login: String,
    val name: String,
    @field:SerializedName("node_id") val nodeId: String,
    @field:SerializedName("organizations_url") val organizationsUrl: String,
    @field:SerializedName("public_gists") val publicGists: Int,
    @field:SerializedName("public_repos") val publicRepos: Int,
    @field:SerializedName("received_events_url") val receivedEventsUrl: String,
    @field:SerializedName("repos_url") val reposUrl: String,
    @field:SerializedName("site_admin") val siteAdmin: Boolean,
    @field:SerializedName("starred_url") val starredUrl: String,
    @field:SerializedName("subscriptions_url") val subscriptionsUrl: String,
    @field:SerializedName("twitter_username") val twitterUsername: String,
    val type: String,
    @field:SerializedName("updated_at") val updatedAt: String,
    val url: String
)