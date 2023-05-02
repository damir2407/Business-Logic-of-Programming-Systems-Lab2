package com.example.blps_lab1.security;

import com.example.blps_lab1.model.basic.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    private final CookUserDetailsService cookUserDetailsService;

    public AuthTokenFilter(JwtUtils jwtUtils, CookUserDetailsService cookUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.cookUserDetailsService = cookUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                String login = jwtUtils.getLoginFromJwtToken(jwt);


                UserDetails userDetails = cookUserDetailsService.loadUserByUsernameAndRoles(login,
                         jwtUtils.getAuthoritiesFromToken(jwt));
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails.getAuthorities());
                System.out.println(userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            log.error("Cannot authenticate user: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);

    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
