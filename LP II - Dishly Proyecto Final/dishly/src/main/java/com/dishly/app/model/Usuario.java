package com.dishly.app.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_usuario")
    private Integer idUsuario;
    
    @Column(name="nombre", nullable = false)
    private String nombre;
    
    @Column(name="email", nullable = false)
    private String email;
    
    @Column(name="password", nullable = false)
    private String contrasenia;

    @Column(name="estado")
    private boolean estado;
    
    @OneToMany(mappedBy="usuario")
    private List<Pedido> pedidos;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="roles_usuario",
            joinColumns=@JoinColumn(name="id_usuario"),
            inverseJoinColumns=@JoinColumn(name="id_rol")
    )
    private Set<Rol> roles = new HashSet<>();
    
    
}