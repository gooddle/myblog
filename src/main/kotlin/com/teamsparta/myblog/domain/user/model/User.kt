package com.teamsparta.myblog.domain.user.model

import com.teamsparta.myblog.domain.user.dto.UserResponse
import jakarta.persistence.*

@Entity
@Table(name ="users")
class User(
    @Column(name="username", nullable = false)
    var userName: String,

    @Column(name="password", nullable = false)
    var password: String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

fun User.toResponse(): UserResponse {
    return UserResponse(
        id=id!!
    )
}