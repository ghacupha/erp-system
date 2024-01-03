package io.github.erp.internal.service.applicationUser;

import io.github.erp.security.jwt.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * This filter clears the application-user context after each request to ensure the same information remains
 * current
 */
@Component("currentUserClearFilter")
public class CurrentUserClearFilter extends GenericFilterBean  implements Filter {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String AUTHORIZATION_TOKEN = "access_token";

    private static final Logger log = LoggerFactory.getLogger(CurrentUserClearFilter.class);

    private final TokenProvider tokenProvider;

    private final InternalApplicationUserDetailService internalApplicationUserDetailService;

    public CurrentUserClearFilter(TokenProvider tokenProvider, InternalApplicationUserDetailService internalApplicationUserDetailService) {
        this.tokenProvider = tokenProvider;
        this.internalApplicationUserDetailService = internalApplicationUserDetailService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        try {

            HttpServletRequest httpRequest = (HttpServletRequest) request;

            String jwtToken = resolveToken(httpRequest);

            if (StringUtils.hasText(jwtToken) && this.tokenProvider.validateToken(jwtToken)) {
                Authentication authentication = this.tokenProvider.getAuthentication(jwtToken);

                internalApplicationUserDetailService
                    .getCorrespondingApplicationUser((UserDetails) authentication.getPrincipal())
                    .ifPresent(CurrentUserContext::setCurrentUser);
            }

            chain.doFilter(request, response);

        } finally {

            CurrentUserContext.clearCurrentUser();
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        String jwt = request.getParameter(AUTHORIZATION_TOKEN);
        if (StringUtils.hasText(jwt)) {
            return jwt;
        }
        return null;
    }
}

