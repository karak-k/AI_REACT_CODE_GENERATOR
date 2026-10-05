package com.codingshuttle.projects.lovable_clone.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public record JwtUserPrinciple(
        Long UserId,
        String Username,
        List<GrantedAuthority> authorities
) {
}
