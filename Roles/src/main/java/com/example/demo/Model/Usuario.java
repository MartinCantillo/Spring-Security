/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.Set;
import lombok.Data;

/**
 *
 * @author marti
 */
@Entity
@Data
public class Usuario {

    private long id_usuario;
    private String nombre_usuario;
    private String contrasena_usuario;
    @OneToMany
    private Set<Rol> roles;
}
