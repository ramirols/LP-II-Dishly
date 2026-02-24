package com.dishly.app.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dishly.app.model.Pedido;
import com.dishly.app.model.Rol;
import com.dishly.app.model.Usuario;
import com.dishly.app.repository.DetallePedidoRepository;
import com.dishly.app.repository.RolRepository;
import com.dishly.app.service.IPdfServicio;
import com.dishly.app.service.PedidoService;
import com.dishly.app.service.PlatoService;
import com.dishly.app.service.UsuarioService;
import com.dishly.app.service.CategoriaService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private PedidoService           pedidoService;
    @Autowired private PlatoService            platoService;
    @Autowired private UsuarioService          usuarioService;
    @Autowired private IPdfServicio            pdfServicio;
    @Autowired private DetallePedidoRepository detallePedidoRepository;
    @Autowired private CategoriaService        categoriaService;
    @Autowired private PasswordEncoder         passwordEncoder;
    @Autowired private RolRepository           rolRepository;

    // ─── Nombre del admin logueado ────────────────────────────────
    private String getAdminNombre() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth != null ? auth.getName() : "";
        return usuarioService.buscarPorEmail(email)
                .map(Usuario::getNombre)
                .orElse("Administrador");
    }

    // ─── Top 5 platos mas pedidos ─────────────────────────────────
    private List<Map<String, Object>> buildTopPlatos() {
        List<Object[]> raw = detallePedidoRepository.findTopPlatos();

        long totalCantidad = raw.stream()
                .mapToLong(r -> ((Number) r[1]).longValue())
                .sum();

        List<Map<String, Object>> topPlatos = new ArrayList<>();
        int limite = Math.min(raw.size(), 5);

        for (int i = 0; i < limite; i++) {
            Object[] row = raw.get(i);
            String    nombre    = (String) row[0];
            long      cantidad  = ((Number) row[1]).longValue();
            BigDecimal total    = new BigDecimal(row[2].toString()).setScale(2, RoundingMode.HALF_UP);
            double porcentaje   = totalCantidad > 0
                    ? Math.round((cantidad * 100.0 / totalCantidad) * 10.0) / 10.0
                    : 0.0;

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("nombre",     nombre);
            item.put("cantidad",   cantidad);
            item.put("total",      total);
            item.put("porcentaje", porcentaje);
            topPlatos.add(item);
        }
        return topPlatos;
    }

    // ══════════════════════════════════════════════════════════════
    //  DASHBOARD
    // ══════════════════════════════════════════════════════════════
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Pedido> todos = pedidoService.listarTodo();

        long totalPedidos = todos.size();
        BigDecimal ventasTotales = todos.stream()
                .map(p -> p.getTotal() != null ? p.getTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalPlatos   = platoService.listarTodo().size();
        long totalUsuarios = usuarioService.listarTodo().size();
        long completados   = todos.stream().filter(Pedido::isEstado).count();
        long pendientes    = totalPedidos - completados;

        List<Pedido> ultimosPedidos = todos.stream()
                .sorted((a, b) -> {
                    if (a.getFechaCreacion() == null) return 1;
                    if (b.getFechaCreacion() == null) return -1;
                    return b.getFechaCreacion().compareTo(a.getFechaCreacion());
                })
                .limit(10).collect(Collectors.toList());

        // Ventas por mes (ultimos 6)
        List<String> mesesLabels  = new ArrayList<>();
        List<Double> ventasPorMes = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();
        for (int i = 5; i >= 0; i--) {
            LocalDateTime mes = ahora.minusMonths(i);
            String label = mes.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "PE"));
            mesesLabels.add(label.substring(0, 1).toUpperCase() + label.substring(1));
            double suma = todos.stream()
                    .filter(p -> p.getFechaCreacion() != null
                            && p.getFechaCreacion().getYear()       == mes.getYear()
                            && p.getFechaCreacion().getMonthValue() == mes.getMonthValue())
                    .mapToDouble(p -> p.getTotal() != null ? p.getTotal().doubleValue() : 0).sum();
            ventasPorMes.add(Math.round(suma * 100.0) / 100.0);
        }

        model.addAttribute("adminNombre",        getAdminNombre());
        model.addAttribute("totalPedidos",       totalPedidos);
        model.addAttribute("ventasTotales",      ventasTotales);
        model.addAttribute("totalPlatos",        totalPlatos);
        model.addAttribute("totalUsuarios",      totalUsuarios);
        model.addAttribute("pedidosCompletados", completados);
        model.addAttribute("pedidosPendientes",  pendientes);
        model.addAttribute("ultimosPedidos",     ultimosPedidos);
        model.addAttribute("mesesLabels",        mesesLabels);
        model.addAttribute("ventasPorMes",       ventasPorMes);
        model.addAttribute("topPlatos",          buildTopPlatos());
        return "admin/dashboard";
    }

    // ══════════════════════════════════════════════════════════════
    //  PEDIDOS — Listar con filtro
    // ══════════════════════════════════════════════════════════════
    @GetMapping("/pedidos")
    public String pedidos(@RequestParam(required = false) String filtro, Model model) {
        List<Pedido> todos = pedidoService.listarTodo();

        List<Pedido> pedidos;
        if ("completados".equals(filtro)) {
            pedidos = todos.stream().filter(Pedido::isEstado).collect(Collectors.toList());
        } else if ("pendientes".equals(filtro)) {
            pedidos = todos.stream().filter(p -> !p.isEstado()).collect(Collectors.toList());
        } else {
            pedidos = todos;
        }

        pedidos = pedidos.stream()
                .sorted((a, b) -> {
                    if (a.getFechaCreacion() == null) return 1;
                    if (b.getFechaCreacion() == null) return -1;
                    return b.getFechaCreacion().compareTo(a.getFechaCreacion());
                }).collect(Collectors.toList());

        model.addAttribute("adminNombre", getAdminNombre());
        model.addAttribute("pedidos",     pedidos);
        model.addAttribute("filtro",      filtro);
        return "admin/pedidos";
    }

    // ══════════════════════════════════════════════════════════════
    //  PEDIDOS — Cambiar estado
    // ══════════════════════════════════════════════════════════════
    @PostMapping("/pedidos/estado")
    public String cambiarEstado(@RequestParam Integer id, @RequestParam boolean estado) {
        Pedido pedido = pedidoService.buscarPedidoPorId(id);
        if (pedido != null) {
            pedido.setEstado(estado);
            pedidoService.guardarPedido(pedido);
        }
        return "redirect:/admin/pedidos?success";
    }

    // ══════════════════════════════════════════════════════════════
    //  STAFF — Listar (solo ROLE_STAFF)
    // ══════════════════════════════════════════════════════════════
    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        List<Usuario> todos = usuarioService.listarTodo();

        List<Usuario> staff = todos.stream()
                .filter(u -> u.getRoles().stream()
                        .anyMatch(r -> r.getNombre().equals("ROLE_STAFF")))
                .collect(Collectors.toList());

        long staffActivo   = staff.stream().filter(Usuario::isEstado).count();
        long staffInactivo = staff.stream().filter(u -> !u.isEstado()).count();

        model.addAttribute("adminNombre",   getAdminNombre());
        model.addAttribute("usuarios",      staff);
        model.addAttribute("totalStaff",    staff.size());
        model.addAttribute("staffActivo",   staffActivo);
        model.addAttribute("staffInactivo", staffInactivo);
        return "admin/usuarios";
    }

    // ══════════════════════════════════════════════════════════════
    //  STAFF — Agregar
    // ══════════════════════════════════════════════════════════════
    @PostMapping("/usuarios/staff/agregar")
    public String agregarStaff(@RequestParam String nombre,
                                @RequestParam String email,
                                @RequestParam String contrasena) {
        if (usuarioService.buscarPorEmail(email).isPresent()) {
            return "redirect:/admin/usuarios?emailExistente";
        }

        Rol rolStaff = rolRepository.findByNombre("ROLE_STAFF")
                .orElseThrow(() -> new RuntimeException("Rol STAFF no encontrado en BD"));

        Usuario nuevo = new Usuario();
        nuevo.setNombre(nombre);
        nuevo.setEmail(email);
        nuevo.setContrasenia(passwordEncoder.encode(contrasena));
        nuevo.setEstado(true);
        Set<Rol> roles = new HashSet<>();
        roles.add(rolStaff);
        nuevo.setRoles(roles);

        usuarioService.actualizarUsuario(nuevo);
        return "redirect:/admin/usuarios?staffAgregado";
    }

    // ══════════════════════════════════════════════════════════════
    //  STAFF — Editar
    // ══════════════════════════════════════════════════════════════
    @PostMapping("/usuarios/staff/editar")
    public String editarStaff(@RequestParam Integer id,
                               @RequestParam String nombre,
                               @RequestParam String email,
                               @RequestParam(required = false) String contrasena) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) return "redirect:/admin/usuarios";

        usuario.setNombre(nombre);
        usuario.setEmail(email);

        if (contrasena != null && !contrasena.isBlank()) {
            usuario.setContrasenia(passwordEncoder.encode(contrasena));
        }

        usuarioService.actualizarUsuario(usuario);
        return "redirect:/admin/usuarios?staffActualizado";
    }

    // ══════════════════════════════════════════════════════════════
    //  STAFF — Habilitar / Deshabilitar
    // ══════════════════════════════════════════════════════════════
    @PostMapping("/usuarios/estado")
    public String cambiarEstadoUsuario(@RequestParam Integer id,
                                        @RequestParam boolean estado) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario != null) {
            usuario.setEstado(estado);
            usuarioService.actualizarUsuario(usuario);
        }
        return "redirect:/admin/usuarios?staffActualizado";
    }

    // ══════════════════════════════════════════════════════════════
    //  CATEGORIAS
    // ══════════════════════════════════════════════════════════════
    @GetMapping("/categorias")
    public String categorias(Model model) {
        model.addAttribute("adminNombre",  getAdminNombre());
        model.addAttribute("categorias",   categoriaService.listarTodo());
        model.addAttribute("categoriaDTO", new dto.CategoriaDTO());
        return "admin/categorias";
    }

    // ══════════════════════════════════════════════════════════════
    //  REPORTES — Vista
    // ══════════════════════════════════════════════════════════════
    @GetMapping("/reportes")
    public String reportes(Model model) {
        List<Pedido> todos = pedidoService.listarTodo();

        long totalPedidos = todos.size();
        BigDecimal ventasTotales = todos.stream()
                .map(p -> p.getTotal() != null ? p.getTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal ticketPromedio = totalPedidos > 0
                ? ventasTotales.divide(BigDecimal.valueOf(totalPedidos), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        long completados = todos.stream().filter(Pedido::isEstado).count();
        double tasaCompletados = totalPedidos > 0
                ? Math.round((completados * 100.0 / totalPedidos) * 10.0) / 10.0
                : 0.0;

        List<String> mesesLabels  = new ArrayList<>();
        List<Double> ventasPorMes = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();
        for (int i = 5; i >= 0; i--) {
            LocalDateTime mes = ahora.minusMonths(i);
            String label = mes.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "PE"));
            mesesLabels.add(label.substring(0, 1).toUpperCase() + label.substring(1));
            double suma = todos.stream()
                    .filter(p -> p.getFechaCreacion() != null
                            && p.getFechaCreacion().getYear()       == mes.getYear()
                            && p.getFechaCreacion().getMonthValue() == mes.getMonthValue())
                    .mapToDouble(p -> p.getTotal() != null ? p.getTotal().doubleValue() : 0).sum();
            ventasPorMes.add(Math.round(suma * 100.0) / 100.0);
        }

        Map<String, Long> pagoMap = todos.stream()
                .filter(p -> p.getMetodoPago() != null)
                .collect(Collectors.groupingBy(Pedido::getMetodoPago, Collectors.counting()));
        List<String> pagoLabels = new ArrayList<>(pagoMap.keySet());
        List<Long>   pagoCounts = pagoLabels.stream().map(pagoMap::get).collect(Collectors.toList());

        model.addAttribute("adminNombre",     getAdminNombre());
        model.addAttribute("ventasTotales",   ventasTotales);
        model.addAttribute("totalPedidos",    totalPedidos);
        model.addAttribute("ticketPromedio",  ticketPromedio);
        model.addAttribute("tasaCompletados", tasaCompletados);
        model.addAttribute("mesesLabels",     mesesLabels);
        model.addAttribute("ventasPorMes",    ventasPorMes);
        model.addAttribute("pagoLabels",      pagoLabels);
        model.addAttribute("pagoCounts",      pagoCounts);
        model.addAttribute("topPlatos",       buildTopPlatos());
        return "admin/reportes";
    }

    // ══════════════════════════════════════════════════════════════
    //  REPORTES — Exportar PDF
    // ══════════════════════════════════════════════════════════════
    @GetMapping("/reportes/exportar/pdf")
    public ResponseEntity<byte[]> exportarPdf() {
        List<Pedido> pedidos = pedidoService.listarTodo();

        long totalPedidos = pedidos.size();
        BigDecimal ventasTotales = pedidos.stream()
                .map(p -> p.getTotal() != null ? p.getTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        byte[] pdf = pdfServicio.generarReportePedidos(pedidos, totalPedidos, ventasTotales);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_dishly.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
