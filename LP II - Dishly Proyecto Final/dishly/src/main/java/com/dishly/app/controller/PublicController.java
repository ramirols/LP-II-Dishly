package com.dishly.app.controller;

import com.dishly.app.service.CategoriaService;
import com.dishly.app.service.PlatoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PublicController {

    private final CategoriaService categoriaService;
    private final PlatoService platoService;

    public PublicController(CategoriaService categoriaService, PlatoService platoService) {
        this.categoriaService = categoriaService;
        this.platoService = platoService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodo());
        model.addAttribute("destacados", platoService.listarDestacados());
        return "public/home";
    }

    @GetMapping("/nosotros")
    public String nosotros() {
        return "public/nosotros";
    }
    
    @GetMapping("/categoria/{id}")
    public String verCategoria(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("platos", platoService.buscarPorCategoria(id));
        model.addAttribute("categoria", categoriaService.buscarCategoriaPorId(id)); 
        return "public/categoria_detalle";
    }

    @GetMapping("/plato/{id}")
    public String verPlato(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("plato", platoService.buscarPlatoPorId(id));
        return "public/plato_detalle";
    }
}