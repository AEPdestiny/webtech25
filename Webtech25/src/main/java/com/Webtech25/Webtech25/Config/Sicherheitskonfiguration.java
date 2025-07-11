package com.Webtech25.Webtech25.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Sicherheitskonfiguration {

    @Bean
    public PasswordEncoder passwortEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain sicherheitsFilterKette(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/anmelden")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/abmelden")
                        .logoutSuccessUrl("/auth/anmelden")
                        .permitAll()
                )
                .csrf().disable();

        return http.build();
    }
}