package com.dishly.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dishly.app.model.DetallePedido;
import com.dishly.app.model.DetallePedidoId;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, DetallePedidoId>{

}
