package com.poseidon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class AppConfig {

    @Bean
    public GrantedAuthoritiesMapper authoritiesMapper() {
        return authorities -> {
            Set<GrantedAuthority> mapped = new HashSet<>();
            mapped.add(new SimpleGrantedAuthority("ROLE_USER"));
            return mapped;
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login/**", "/oauth2/**", "/css/**", "/js/**").permitAll();
                    auth.requestMatchers("/admin/**").hasRole("ADMIN");
                    auth.requestMatchers("/user/**").authenticated();
                    auth.anyRequest().authenticated();

                }).formLogin(form -> form
                        .defaultSuccessUrl("/", true)  // change ici
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("/", true)  // et ici
                        .userInfoEndpoint(userInfo -> userInfo
                                .userAuthoritiesMapper(authoritiesMapper())
                        )
                )
                .csrf(csrf -> csrf.disable())
                .build();
    }
}