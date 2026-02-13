package com.dishly.app.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class DetallePedidoId implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name="pedido_id")
	private Pedido pedido;
	
	@ManyToOne
	@JoinColumn(name="plato_id")
	private Plato plato;
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetallePedidoId)) return false;
        DetallePedidoId that = (DetallePedidoId) o;
        return Objects.equals(pedido, that.pedido) &&
               Objects.equals(plato, that.plato);
    }

    // hashCode
    @Override
    public int hashCode() {
        return Objects.hash(pedido, plato);
    }
}
