package com.teamsparta.myblog.infra.security.jwt

import com.teamsparta.myblog.infra.security.UserPrincipal
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.web.authentication.WebAuthenticationDetails
import java.io.Serializable

class JwtAuthenticationToken(
    private val principal: UserPrincipal,
    detail : WebAuthenticationDetails,
): AbstractAuthenticationToken(principal.authorities), Serializable {

    init {
        super.setAuthenticated(true)
        super.setDetails(detail)
    }

    override fun getCredentials()=null


    override fun getPrincipal() = principal

    override fun isAuthenticated(): Boolean {
        return true
    }

    }
