package com.dishly.app.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

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
@Table(name="detalle_pedido")
public class DetallePedido {

	@EmbeddedId
	private DetallePedidoId id;
	
	@Column(name="cantidad")
	private Integer cantidad;
	
	@Column(name="subtotal")
	private BigDecimal subtotal;
}