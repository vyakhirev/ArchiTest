package com.mikhail.vyakhirev.data.model

import com.mikhail.vyakhirev.utils.Event

data class AuthUiModel(
    val showProgress: Boolean,
    val error: Event<Pair<AuthType, MaterialDialogContent>>?,
    val success: Boolean,
    val showAllLinkProvider: Event<Pair<List<String>, MaterialDialogContent>>?
)

enum class AuthType(var authValue: String) {
    FACEBOOK("facebook.com"),
    GOOGLE("google.com"),
    GITHUB("github.com"),
    TWITTER("twitter.com"),
    EMAIL("")
}

fun String.toAuthType(): AuthType {
    return when (this) {
        AuthType.FACEBOOK.authValue -> AuthType.FACEBOOK
        AuthType.TWITTER.authValue -> AuthType.TWITTER
        AuthType.GOOGLE.authValue -> AuthType.GOOGLE
        AuthType.GITHUB.authValue -> AuthType.GITHUB
        else -> AuthType.EMAIL
    }
}