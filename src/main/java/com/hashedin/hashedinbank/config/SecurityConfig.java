package com.hashedin.hashedinbank.config;

import com.hashedin.hashedinbank.constants.SecurityConstants;
import com.hashedin.hashedinbank.exception.AuthEntryPointJwtException;
import com.hashedin.hashedinbank.filter.JWTTokenValidatorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthEntryPointJwtException unauthorizedHandler;

    private final UserDetailsService bankUserDetailsService;

    @Autowired
    public SecurityConfig(AuthEntryPointJwtException unauthorizedHandler,
                          UserDetailsService bankUserDetailsService) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.bankUserDetailsService = bankUserDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(bankUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors().configurationSource(request -> getCorsConfiguration())
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(new JWTTokenValidatorFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests()
                .requestMatchers("/company/**", "/user/**", "/card/**", "/expense/**").authenticated()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/swagger-ui.html", "/swagger-resources/**", "/v3/api-docs/**", "/webjars/**",
                        "/actuator/**", "/swagger-ui/**","/swagger-ui/index.html","/configuration/ui","/configuration/**").permitAll();
        return http.build();
    }

    private CorsConfiguration getCorsConfiguration() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setExposedHeaders(List.of(SecurityConstants.JWT_HEADER));
        config.setMaxAge(3600L);
        return config;
    }
}
