package com.example.blps_lab1.config;

import com.example.blps_lab1.config.jwt.AuthEntryPointJwt;
import com.example.blps_lab1.config.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableWebSecurity
@EnableTransactionManagement

public class WebSecurityConfig {
    private final AuthEntryPointJwt unauthorizedHandler;

    private final AuthTokenFilter authenticationJwtTokenFilter;



    @Autowired
    public WebSecurityConfig(AuthEntryPointJwt unauthorizedHandler, AuthTokenFilter authenticationJwtTokenFilter) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.authenticationJwtTokenFilter = authenticationJwtTokenFilter;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .antMatchers("/cuisine/**").hasRole("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/cuisine/**").hasRole("ROLE_USER")

                .antMatchers(HttpMethod.POST, "/culinary-news/**").hasAnyRole("ROLE_USER", "ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/culinary-news/**").permitAll()

                .antMatchers("/dish/**").hasRole("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/dish/**").hasRole("ROLE_USER")

                .antMatchers("/ingredient/**").hasRole("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/ingredient/**").hasRole("ROLE_USER")

                .antMatchers("/taste/**").hasRole("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/taste/**").hasRole("ROLE_USER")

                .antMatchers(HttpMethod.GET, "/recipe").hasAnyRole("ROLE_ADMIN", "ROLE_USER")
                .antMatchers(HttpMethod.POST, "/recipe").hasAnyRole("ROLE_ADMIN", "ROLE_USER")
                .antMatchers(HttpMethod.DELETE, "/recipe").hasAnyRole("ROLE_ADMIN", "ROLE_USER")
                .antMatchers(HttpMethod.PUT, "/recipe").hasAnyRole("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/recipe/review").hasRole("ROLE_ADMIN")
                .antMatchers(HttpMethod.PUT, "/recipe/accept/**").hasRole("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/recipe/decline/**").hasRole("ROLE_ADMIN")

                .antMatchers(HttpMethod.POST, "/user/admin-create").hasRole("ROLE_ADMIN")
                .anyRequest().permitAll();

        http.addFilterBefore(authenticationJwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
