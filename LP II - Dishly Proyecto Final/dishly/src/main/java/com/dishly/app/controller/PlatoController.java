package com.dishly.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dishly.app.model.Categoria;
import com.dishly.app.model.Plato;
import com.dishly.app.service.CategoriaService;
import com.dishly.app.service.PlatoService;
import com.dishly.app.service.UsuarioService;
import com.dishly.app.util.AdminUtils;

import dto.CategoriaDTO;
import dto.PlatoDTO;

@Controller
@RequestMapping("/admin/platos")
public class PlatoController {

    @Autowired
    PlatoService platoService;

    @Autowired
    CategoriaService categoriaService;
    
    @Autowired
    UsuarioService usuarioService;

    // ── Platos ──────────────────────────────────────
    @GetMapping("/inicio")
    public String gestionarPlatos(Model model) {
    		model.addAttribute("adminNombre",  AdminUtils.getAdminNombre(usuarioService));
        model.addAttribute("platoDTO",    new PlatoDTO());
        model.addAttribute("categoriaDTO",new CategoriaDTO());
        model.addAttribute("platos",      platoService.listarTodo());
        model.addAttribute("categorias",  categoriaService.listarTodo());
        return "admin/platos";
    }

    @PostMapping("/guardar")
    public String guardarPlato(@ModelAttribute PlatoDTO platoDTO) {
        Plato plato = platoDTO.getId() != null
                ? platoService.buscarPlatoPorId(platoDTO.getId())
                : new Plato();

        plato.setNombre(platoDTO.getNombrePlato());
        plato.setPrecio(platoDTO.getPrecio());
        plato.setImagen(platoDTO.getImagenUrl());
        plato.setCategoria(categoriaService.buscarCategoriaPorId(platoDTO.getIdCategoria()));
        plato.setEstado(true);
        platoService.guardarPlato(plato);
        return "redirect:/admin/platos/inicio?platoGuardado";
    }

    @PostMapping("/eliminar")
    public String eliminarPlato(@ModelAttribute PlatoDTO platoDTO) {
        platoService.eliminarPlatoPorId(platoDTO.getId());
        return "redirect:/admin/platos/inicio?platoEliminado";
    }

    @PostMapping("/categoria/guardar")
    public String guardarCategoria(@ModelAttribute CategoriaDTO categoriaDTO,
                                   @RequestHeader(value = "Referer", defaultValue = "/admin/platos/inicio") String referer) {
        Categoria categoria = categoriaDTO.getId() != null
                ? categoriaService.buscarCategoriaPorId(categoriaDTO.getId())
                : new Categoria();

        categoria.setNombre(categoriaDTO.getNombre());
        categoria.setDescripcion(categoriaDTO.getDescripcion());
        categoriaService.guardarCategoria(categoria);

        // Redirige de vuelta a donde vino (platos o categorías)
        if (referer.contains("/admin/categorias")) {
            return "redirect:/admin/categorias?categoriaAgregada";
        }
        return "redirect:/admin/platos/inicio?categoriaAgregada";
    }

    @PostMapping("/categoria/eliminar")
    public String eliminarCategoria(@ModelAttribute CategoriaDTO categoriaDTO,
                                    @RequestHeader(value = "Referer", defaultValue = "/admin/platos/inicio") String referer) {
        categoriaService.eliminarCategoriaPorId(categoriaDTO.getId());

        if (referer.contains("/admin/categorias")) {
            return "redirect:/admin/categorias?categoriaEliminada";
        }
        return "redirect:/admin/platos/inicio?categoriaEliminada";
    }
}
