package com.dishly.app.controller;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dishly.app.model.CarritoItem;
import com.dishly.app.model.Pedido;
import com.dishly.app.model.Usuario;
import com.dishly.app.service.PedidoService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
	@Autowired
	private PedidoService pedidoService;

    @GetMapping("/carrito")
    public String carrito(HttpSession session, Model model) {

        return "cliente/carrito";
    }

    @GetMapping("/carrito/agregar/{id}")
    public String agregar(@PathVariable Long id, HttpSession session) {

        return "redirect:/menu";
    }

    // DATOS PARA EL HEADER
    @ModelAttribute
    public void header(HttpSession session, Model model) {

    }
    
    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        
        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/carrito";
        }
        
        double total = carrito.stream().mapToDouble(CarritoItem::getSubtotal).sum();
        
        model.addAttribute("cartItems", carrito);
        model.addAttribute("total", total);
        return "cliente/checkout";
    }
    
    /*@PostMapping("/checkout/confirmar")
    public String confirmarPedido(HttpSession session, 
                                  @AuthenticationPrincipal Usuario usuarioLogueado,
                                  @RequestParam("pago") String metodoPago,
                                  @RequestParam("direccion") String direccion) {
        
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        
        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/menu";
        }

        // 1. Crear la cabecera del pedido
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuarioLogueado);
        pedido.setFecha(java.time.LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        pedido.setMetodoPago(metodoPago);
        pedido.setDireccionEnvio(direccion);
        
        double total = carrito.stream().mapToDouble(CarritoItem::getSubtotal).sum();
        pedido.setTotal(java.math.BigDecimal.valueOf(total));

        // 2. Guardar pedido y detalles usando el servicio
        pedidoService.guardarPedidoCompleto(pedido, carrito);

        // 3. Limpiar el carrito de la sesión
        session.removeAttribute("carrito");
        session.setAttribute("cartCount", 0); // Si tienes un contador en el header

        return "redirect:/cliente/pedidos?success=true";
    }*/
    
    @GetMapping("/pedidos")
    public String misPedidos(Model model, @AuthenticationPrincipal Usuario usuarioLogueado) {
        // Si por alguna razón la sesión expiró o el objeto es nulo
        if (usuarioLogueado == null) {
            return "redirect:/auth/login";
        }

        try {
            List<Pedido> misPedidos = pedidoService.obtenerPedidosPorUsuario(usuarioLogueado.getIdUsuario());
            model.addAttribute("pedidos", misPedidos);
        } catch (Exception e) {
            // Esto te ayudará a ver el error real en la consola de STS
            e.printStackTrace(); 
            return "error"; 
        }
        
        return "cliente/pedidos";
    }
}