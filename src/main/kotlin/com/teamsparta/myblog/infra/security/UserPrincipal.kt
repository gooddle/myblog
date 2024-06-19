package com.teamsparta.myblog.infra.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class UserPrincipal(
    val id: Long,
    val userName: String,
    val authorities: Collection<GrantedAuthority>
)
{
    constructor(id: Long, userName: String, roles: Set<String>) : this(
        id,
        userName,
        roles.map { SimpleGrantedAuthority("ROLE_$it") }
    )
}






