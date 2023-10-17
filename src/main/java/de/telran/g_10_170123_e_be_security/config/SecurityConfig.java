package de.telran.g_10_170123_e_be_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(x -> x.disable())
                .authorizeHttpRequests(
                        x -> x
                                .requestMatchers(HttpMethod.GET, "/user/all")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/user/name")
                                .hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.POST, "/user/add")
                                .hasRole("ADMIN")
                                .anyRequest().authenticated()
                                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}