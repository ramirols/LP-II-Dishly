package com.dishly.app.service;

import com.dishly.app.model.CarritoItem;
import com.dishly.app.model.Plato;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarritoService {

    private static final String CARRITO = "CARRITO";

    @SuppressWarnings("unchecked")
    public List<CarritoItem> obtener(HttpSession session) {
        List<CarritoItem> carrito =
                (List<CarritoItem>) session.getAttribute(CARRITO);

        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute(CARRITO, carrito);
        }
        return carrito;
    }

    public void agregar(HttpSession session, Plato plato) {
        List<CarritoItem> carrito = obtener(session);

        for (CarritoItem item : carrito) {
            if (item.getPlato().getId().equals(plato.getId())) {
                item.setCantidad(item.getCantidad() + 1);
                return;
            }
        }

        CarritoItem nuevo = new CarritoItem();
        nuevo.setPlato(plato);
        nuevo.setCantidad(1);
        carrito.add(nuevo);
    }

    public int cantidad(HttpSession session) {
        return obtener(session).stream()
                .mapToInt(CarritoItem::getCantidad)
                .sum();
    }

    public double total(HttpSession session) {
        return obtener(session).stream()
                .mapToDouble(CarritoItem::getSubtotal)
                .sum();
    }
}