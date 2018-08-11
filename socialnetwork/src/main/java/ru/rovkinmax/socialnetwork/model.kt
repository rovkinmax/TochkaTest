package ru.rovkinmax.socialnetwork

class SocialException(message: String) : RuntimeException(message)

class SocialLoginResult(val socialType: SocialType)

enum class SocialType {
    VK(), FB, PLUS(), NONE()
}

class UserInfo(val username: String, val avatar: String?)