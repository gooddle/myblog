package com.teamsparta.myblog.domain.user.model

import com.teamsparta.myblog.domain.user.dto.UserResponse
import jakarta.persistence.*

@Entity
@Table(name ="users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name="email", nullable = false)
    var email: String,

    @Column(name="password", nullable = false)
    var password: String,
)

fun User.toResponse(): UserResponse {
    return UserResponse(
        id=this.id!!,
        email =email
    )
}