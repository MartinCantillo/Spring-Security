/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.Model;

import jakarta.persistence.*;
import java.util.Set;
import lombok.*;

/**
 *
 * @author marti
 */
@Entity
@Data
@AllConstructor
@NoAllConstructor
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_usuario;
    private String nombre_usuario;
    private String contrasena_usuario;
}
