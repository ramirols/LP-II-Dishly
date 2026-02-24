package com.dishly.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dishly.app.model.DetallePedido;
import com.dishly.app.model.DetallePedidoId;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, DetallePedidoId> {

    // Retorna [nombre del plato, total cantidad, total ventas]
    // ordenado por cantidad descendente, limitado a 5
    @Query("SELECT d.id.plato.nombre, SUM(d.cantidad), SUM(d.subtotal) " +
           "FROM DetallePedido d " +
           "GROUP BY d.id.plato.nombre " +
           "ORDER BY SUM(d.cantidad) DESC")
    List<Object[]> findTopPlatos();
}
