package com.dishly.app.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="platos")
public class Plato {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_plato")
	private Integer id;
	
	@Column(name="nombre", nullable = false)
	private String nombre;
	
	@Column(name="precio", nullable = false)
    private BigDecimal precio;
    
    @ManyToOne
    @JoinColumn(name="categoria_id")
    private Categoria categoria;
    
    @Column(name="imagen")
    private String imagen;
    
    @Column(name="estado")
    private boolean estado;
    
    @Column(name = "destacado")
    private boolean destacado;
}