package com.dishly.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // RUTAS PÚBLICAS (Cualquiera puede entrar)
                .requestMatchers(
                    "/",
                    "/categoria/**",
                    "/plato/**",
                    "/carrito/**",
                    "/nosotros",
                    "/programas",
                    "/soporte",
                    "/auth/**",
                    "/js/**",
                    "/css/**",
                    "/img/**"
                ).permitAll()

                // CHECKOUT (Solo usuarios con rol específico)
                .requestMatchers("/cliente/checkout")
                .hasAnyRole("CLIENTE", "ADMIN")

                // RUTAS DE CLIENTE (Cualquier usuario logueado)
                .requestMatchers("/cliente/pedidos/**").hasRole("CLIENTE")
                .requestMatchers("/cliente/**")
                .authenticated()

                // ADMIN
                .requestMatchers("/admin/**")
                .hasRole("ADMIN")

                // CUALQUIER OTRA RUTA
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/auth/login")
                .usernameParameter("email")
                .passwordParameter("contrasenia")
                // true para que siempre vaya al inicio, false para que respete a donde quería ir (SavedRequest)
                .defaultSuccessUrl("/", false) 
                .permitAll()
            )

            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(
                    new LoginUrlAuthenticationEntryPoint("/auth/login")
                )
            )

            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            )

            .build();
    }
}