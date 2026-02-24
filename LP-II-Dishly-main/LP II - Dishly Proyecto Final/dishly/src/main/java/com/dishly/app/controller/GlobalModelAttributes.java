package com.dishly.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.dishly.app.model.CarritoItem;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalModelAttributes {

	@SuppressWarnings("unchecked")
	@ModelAttribute
    public void addCartAttributes(HttpSession session, Model model) {
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        double total = carrito.stream()
                              .mapToDouble(item -> item.getPlato().getPrecio().doubleValue() * item.getCantidad())
                              .sum();
        
        int count = carrito.stream()
                           .mapToInt(CarritoItem::getCantidad)
                           .sum();

        model.addAttribute("cartItems", carrito);
        model.addAttribute("cartTotal", total);
        model.addAttribute("cartCount", count);
    }
}
