package com.dishly.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CarritoItem {
	private Plato plato;
    private int cantidad;

    public double getSubtotal() {
        return plato.getPrecio().doubleValue() * cantidad;
    }
}