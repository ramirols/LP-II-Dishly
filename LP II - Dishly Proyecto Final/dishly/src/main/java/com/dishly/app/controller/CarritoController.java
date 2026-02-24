package com.dishly.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dishly.app.model.CarritoItem;
import com.dishly.app.model.Plato;
import com.dishly.app.service.PlatoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/carrito")
public class CarritoController {
	@Autowired
    private PlatoService platoService;
	
	@GetMapping("/agregar/{id}")
	public String agregar(@PathVariable Integer id, 
	                      @RequestParam(defaultValue = "1") int cantidad,
	                      HttpSession session, 
	                      HttpServletRequest request) {
	    List<CarritoItem> carrito = obtenerCarrito(session);
	    
	    Optional<CarritoItem> itemOpt = carrito.stream()
	        .filter(i -> i.getPlato().getId().equals(id))
	        .findFirst();

	    if (itemOpt.isPresent()) {
	        itemOpt.get().setCantidad(itemOpt.get().getCantidad() + cantidad);
	    } else {
	        Plato p = platoService.buscarPlatoPorId(id);
	        carrito.add(new CarritoItem(p, cantidad));
	    }
	    
	    session.setAttribute("carrito", carrito);
	    String referer = request.getHeader("Referer");
	    return "redirect:" + (referer != null ? referer : "/");
	}

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, HttpSession session) {
        List<CarritoItem> carrito = obtenerCarrito(session);
        carrito.removeIf(i -> i.getPlato().getId().equals(id));
        session.setAttribute("carrito", carrito);
        return "redirect:/";
    }

    @SuppressWarnings("unchecked")
    private List<CarritoItem> obtenerCarrito(HttpSession session) {
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        return (carrito == null) ? new ArrayList<>() : carrito;
    }
    
    @GetMapping("/restar/{id}")
    public String restar(@PathVariable Integer id, HttpSession session, HttpServletRequest request) {
        List<CarritoItem> carrito = obtenerCarrito(session);
        
        carrito.stream()
            .filter(i -> i.getPlato().getId().equals(id))
            .findFirst()
            .ifPresent(item -> {
                if (item.getCantidad() > 1) {
                    item.setCantidad(item.getCantidad() - 1);
                } else {
                    carrito.remove(item);
                }
            });
            
        session.setAttribute("carrito", carrito);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
