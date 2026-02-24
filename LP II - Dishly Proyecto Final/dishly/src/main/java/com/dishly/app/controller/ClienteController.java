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
import com.dishly.app.service.UsuarioService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private UsuarioService usuarioService;
	
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
    
    @PostMapping("/checkout/confirmar")
    public String confirmarPedido(HttpSession session, 
                                  @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails, 
                                  @RequestParam("pago") String metodoPago,
                                  @RequestParam("direccion") String direccion) {
        
        // valida que el usuario esté autenticado
        if (userDetails == null) {
            return "redirect:/auth/login";
        }

        // obtiene el carrito de la sesión
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/cliente/carrito";
        }

        // Usa el email que viene dentro de userDetails
        Usuario usuarioLogueado = usuarioService.buscarPorEmail(userDetails.getUsername())
                                                .orElse(null);

        // crea y configura el objeto Pedido
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuarioLogueado);
        pedido.setMetodoPago(metodoPago.toUpperCase());
        pedido.setDireccionEnvio(direccion);
        pedido.setEstado(true); 
        
        double total = carrito.stream().mapToDouble(CarritoItem::getSubtotal).sum();
        pedido.setTotal(java.math.BigDecimal.valueOf(total));

        // guarda pedido completo
        Pedido pedidoGuardado = pedidoService.guardarPedidoCompleto(pedido, carrito);

        // limpia sesión
        session.removeAttribute("carrito");
        session.setAttribute("cartCount", 0);

        return "redirect:/cliente/pago-exitoso?id=" + pedidoGuardado.getId();
    }

    @GetMapping("/pago-exitoso")
    public String pagoExitoso(@RequestParam("id") Integer id, Model model) {
        model.addAttribute("pedidoId", id);
        return "cliente/pago-completado";
    }
    
    @GetMapping("/pedidos")
    public String listarMisPedidos(Model model, 
                                   @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        
        if (userDetails == null) return "redirect:/auth/login";

        // obtiene el id del usuario real
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername()).orElse(null);
        
        if (usuario != null) {
            List<Pedido> misPedidos = pedidoService.obtenerPedidosPorUsuario(usuario.getIdUsuario());
            model.addAttribute("pedidos", misPedidos);
        }

        return "cliente/pedidos";
    }
    
    @GetMapping("/pedidos/{id}")
    public String verDetallePedido(@PathVariable("id") Integer id, 
                                   Model model, 
                                   @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        
        // obtiene el pedido por ID
        Pedido pedido = pedidoService.buscarPedidoPorId(id);
        
        // seguridad: Valida que el pedido exista y sea del usuario que consulta
        if (pedido == null || !pedido.getUsuario().getEmail().equals(userDetails.getUsername())) {
            return "redirect:/cliente/pedidos";
        }
        
        model.addAttribute("pedido", pedido);
        return "cliente/detalle-pedido"; 
    }
}