package com.example.demo.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.Services.CustomUserDetailsService;
import com.example.demo.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


//El JwtRequestFilter se encarga de interceptar las solicitudes HTTP entrantes y 
//verificar si contienen un token JWT válido en el encabezado de autorización
//con OncePerRequestFilter Esto significa que el filtro solo se aplica a las solicitudes entrantes y no a las respuestas salientes.




// Marca esta clase como un componente de Spring que será detectado automáticamente
@Component
@AllArgsConstructor
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwUtil;  // Utilidad para manejar tokens JWT

    @Autowired
    private CustomUserDetailsService customUserDetailsService;  // Servicio para cargar detalles del usuario

    // Método que se ejecuta una vez por cada solicitud
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Obtiene el encabezado de autorización de la solicitud
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // Verifica si el encabezado de autorización está presente y comienza con "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);  // Extrae el JWT del encabezado
            username = jwUtil.extractUsername(jwt);  // Extrae el nombre de usuario del JWT
        }
        
        // Verifica si el nombre de usuario no es nulo y si no hay autenticación en el contexto de seguridad
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carga los detalles del usuario desde el servicio de detalles del usuario
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
            
            // Verifica si el token JWT es válido
            if (jwUtil.validateToken(jwt, username)) {
                // Crea un objeto de autenticación con los detalles del usuario y los roles
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                // Establece los detalles de autenticación en el objeto de autenticación
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Establece el objeto de autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        
        // Pasa la solicitud y la respuesta al siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }
}
