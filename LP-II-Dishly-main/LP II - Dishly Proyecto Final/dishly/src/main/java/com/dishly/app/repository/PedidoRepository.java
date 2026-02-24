package com.dishly.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dishly.app.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
	List<Pedido> findByUsuarioIdUsuarioOrderByIdDesc(Integer idUsuario);
}