/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.Services;

import com.example.demo.Model.Rol;
import com.example.demo.Model.Usuario;
import com.example.demo.Repository.UsuarioRepository;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author marti
 */
//Este servicio es el encargado de cargar el usuario de la bd(autenticacion)
@Service("userDetailsService")
@Slf4j
public class UsuarioService implements UserDetailsService{

    @Autowired
    private UsuarioRepository usuarioRepo;
    
    @Override
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.findByUsername(username);
        
        if(usuario == null){
            throw new UsernameNotFoundException(username);
        }
        
        var roles = new ArrayList<GrantedAuthority>();
        
        for(Rol rol: usuario.getRoles()){
            roles.add(new SimpleGrantedAuthority(rol.getNombre_rol()));
        }
        
        return new User(usuario.getNombre_usuario(), usuario.getContrasena_usuario(), roles);
    }
    
}