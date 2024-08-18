package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.Services.CustomUserDetailsService;
import com.example.demo.util.JwtUtil;
import org.springframework.security.config.Customizer;

/**
 * Configuración de seguridad de Spring.
 * 
 * Configura la seguridad de la aplicación, incluyendo la autenticación y autorización,
 * así como el manejo de tokens JWT.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Reemplaza a @EnableGlobalMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Retorna un codificador de contraseñas utilizando BCrypt.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        // Retorna el AuthenticationManager configurado.
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configura la protección CSRF utilizando la configuración predeterminada.
        http.csrf(Customizer.withDefaults())
            // Configura las reglas de autorización para las solicitudes HTTP.
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/api/register", "/api/login").permitAll() // Permite el acceso sin autenticación
                    .anyRequest().authenticated() // Requiere autenticación para todas las demás rutas
            )
            // Configura la gestión de sesiones.
            .sessionManagement(sessionManagement ->
                sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Estado de sesión sin estado
            )
            // Añade un filtro personalizado para procesar JWT antes del filtro de autenticación estándar de Spring Security.
            .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        // Construye y devuelve el SecurityFilterChain configurado.
        return http.build();
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        // Retorna una instancia del filtro personalizado para procesar JWT.
        return new JwtRequestFilter(jwtUtil, customUserDetailsService);
    }
}
