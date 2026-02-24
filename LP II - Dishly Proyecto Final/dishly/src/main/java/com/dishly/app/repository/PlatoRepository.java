package com.dishly.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dishly.app.model.Plato;

public interface PlatoRepository extends JpaRepository<Plato, Integer> {
	List<Plato> findByCategoriaIdAndEstadoTrue(Integer categoriaId);

    List<Plato> findByDestacadoTrueAndEstadoTrue();	
}