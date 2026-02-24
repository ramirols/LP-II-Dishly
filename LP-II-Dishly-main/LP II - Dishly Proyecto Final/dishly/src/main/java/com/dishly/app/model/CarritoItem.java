package com.dishly.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoItem {
    private Plato plato;
    private int cantidad;

    public double getSubtotal() {
        return plato.getPrecio().doubleValue() * cantidad;
    }
}