package com.cartisan.system.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author colin
 */
public class JwtToken implements AuthenticationToken {
    private final String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
