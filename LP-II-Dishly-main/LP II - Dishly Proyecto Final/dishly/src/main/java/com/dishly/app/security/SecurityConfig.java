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

    public SecurityConfig() {
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // RUTAS PÚBLICAS
                .requestMatchers(
                    "/",
                    "/categoria/**",
                    "/plato/**",
                    "/carrito/**",
                    "/nosotros",
                    "/auth/**",
                    "/js/**",
                    "/css/**",
                    "/img/**"
                ).permitAll()

                // CHECKOUT
                .requestMatchers("/cliente/checkout")
                .authenticated()

                // RUTAS DE CLIENTE
                // .requestMatchers("/cliente/pedidos/**").hasRole("CLIENTE")
                // .requestMatchers("/cliente/**").authenticated()
                
                .requestMatchers("/cliente/**").hasRole("CLIENTE")

                // ADMIN exclusivo (platos, categorias, usuarios)
                .requestMatchers("/admin/platos/**").hasRole("ADMIN")
                .requestMatchers("/admin/categorias/**").hasRole("ADMIN")
                .requestMatchers("/admin/usuarios/**").hasRole("ADMIN")

                // ADMIN y STAFF (dashboard, pedidos, reportes)
                .requestMatchers("/admin/**").hasAnyRole("ADMIN", "STAFF")

                // CUALQUIER OTRA RUTA
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .usernameParameter("email")
                .passwordParameter("contrasenia")
                .successHandler((request, response, authentication) -> {
                    boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                    boolean isStaff = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"));
                    if (isAdmin || isStaff) {
                        response.sendRedirect("/admin/dashboard");
                    } else {
                        response.sendRedirect("/");
                    }
                })
                .failureUrl("/?error=true")
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
