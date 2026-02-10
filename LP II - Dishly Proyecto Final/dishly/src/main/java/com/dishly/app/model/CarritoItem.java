package com.dishly.app.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CarritoItem {
	private Plato plato;
    private int cantidad;

    public double getSubtotal() {
        return plato.getPrecio() * cantidad;
    }
}