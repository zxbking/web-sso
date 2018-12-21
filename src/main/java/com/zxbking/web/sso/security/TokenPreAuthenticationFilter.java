package com.zxbking.web.sso.security;

import com.zxbking.web.sso.comm.utils.WebUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by zhangxibin on 2016/6/20.
 * 预授权，通过token授权
 */
public class TokenPreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {
    private static final String SSO_TOKEN = "X-API-TOKEN";
    public static final String SSO_CREDENTIALS = "N/A";

    public TokenPreAuthenticationFilter(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
    }


    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        WebUtils.logRequestParams(request);
        return request.getHeader(SSO_TOKEN);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return SSO_CREDENTIALS;
    }
}
