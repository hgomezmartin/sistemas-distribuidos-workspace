package com.ubu.p3.twentyonepilots.config;

import com.ubu.p3.twentyonepilots.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity        // si usas @PreAuthorize, etc.
public class WebSecurityConfig {

    // beans “independientes”

    @Bean
    public PasswordEncoder passwordEncoder() {
        // el mismo que usas en UserService.register()
        return new BCryptPasswordEncoder();
    }

    // autenticación

    @Bean
    public DaoAuthenticationProvider authProvider(UserService userService,
                                                  PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    //reglas HTTP y login

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           DaoAuthenticationProvider authProvider)
            throws Exception {

        http.authenticationProvider(authProvider)           // lo añadimos
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/", "/login", "/register", "/merch/**", "/tickets/**", "/css/**", "/images/**").permitAll()

                        // xonas solo para usuarios logueados
                        .requestMatchers("/profile/**", "/cart/**").authenticated()

                        // zona de administracion
                        .requestMatchers("/users/**").hasRole("ADMIN")

                        // cualquier otra cosa va a requerir iniciar sesion
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/logout-success")
                        .invalidateHttpSession(true)
                        .permitAll()
                );

        return http.build();
    }
}
