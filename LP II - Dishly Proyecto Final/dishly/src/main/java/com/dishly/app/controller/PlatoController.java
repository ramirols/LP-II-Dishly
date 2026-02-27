package com.dishly.app.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired PlatoService     platoService;
    @Autowired CategoriaService categoriaService;
    @Autowired UsuarioService   usuarioService;

    // Carpeta destino dentro del proyecto
    private static final String IMG_DIR = "src/main/resources/static/img/";

    // ── Listar ───────────────────────────────────────────
    @GetMapping("/inicio")
    public String gestionarPlatos(Model model) {
        model.addAttribute("adminNombre",  AdminUtils.getAdminNombre(usuarioService));
        model.addAttribute("platoDTO",     new PlatoDTO());
        model.addAttribute("categoriaDTO", new CategoriaDTO());
        model.addAttribute("platos",       platoService.listarTodo());
        model.addAttribute("categorias",   categoriaService.listarTodo());
        return "admin/platos";
    }

    // ── Guardar / Editar ─────────────────────────────────
    @PostMapping("/guardar")
    public String guardarPlato(@ModelAttribute PlatoDTO platoDTO) {

        Plato plato = platoDTO.getId() != null
                ? platoService.buscarPlatoPorId(platoDTO.getId())
                : new Plato();

        plato.setNombre(platoDTO.getNombrePlato());
        plato.setPrecio(platoDTO.getPrecio());
        plato.setCategoria(categoriaService.buscarCategoriaPorId(platoDTO.getIdCategoria()));
        plato.setEstado(true);

        MultipartFile imagenFile = platoDTO.getImagenFile();

        if (imagenFile != null && !imagenFile.isEmpty()) {
            // Subieron imagen nueva — guardar en disco
            String nombreArchivo = guardarImagen(imagenFile);
            if (nombreArchivo != null) {
            	plato.setImagen(nombreArchivo);
            }
        } else if (platoDTO.getImagenUrl() != null && !platoDTO.getImagenUrl().isBlank()) {
            // Sin imagen nueva — conservar la existente
            plato.setImagen(platoDTO.getImagenUrl());
        }

        platoService.guardarPlato(plato);
        return "redirect:/admin/platos/inicio?platoGuardado";
    }

    // ── "Eliminar" (Borrado Lógico) ─────────────────────────
    @PostMapping("/eliminar")
    public String eliminarPlato(@ModelAttribute PlatoDTO platoDTO) {
    Plato plato = platoService.buscarPlatoPorId(platoDTO.getId());
    if (plato != null) {
        plato.setEstado(false); // Cambiamos a inactivo en lugar de borrar
        platoService.guardarPlato(plato);
    }
        return "redirect:/admin/platos/inicio?platoEliminado";
    }

    // ── Guardar categoria ────────────────────────────────
    @PostMapping("/categoria/guardar")
    public String guardarCategoria(@ModelAttribute CategoriaDTO categoriaDTO,
                                   @RequestHeader(value = "Referer", defaultValue = "/admin/platos/inicio") String referer) {
        Categoria categoria = categoriaDTO.getId() != null
                ? categoriaService.buscarCategoriaPorId(categoriaDTO.getId())
                : new Categoria();

        categoria.setNombre(categoriaDTO.getNombre());
        categoria.setDescripcion(categoriaDTO.getDescripcion());
        categoriaService.guardarCategoria(categoria);

        if (referer.contains("/admin/categorias")) {
            return "redirect:/admin/categorias?categoriaAgregada";
        }
        return "redirect:/admin/platos/inicio?categoriaAgregada";
    }

    // ── Eliminar categoria ───────────────────────────────
    @PostMapping("/categoria/eliminar")
    public String eliminarCategoria(@ModelAttribute CategoriaDTO categoriaDTO,
                                    @RequestHeader(value = "Referer", defaultValue = "/admin/platos/inicio") String referer) {
        categoriaService.eliminarCategoriaPorId(categoriaDTO.getId());

        if (referer.contains("/admin/categorias")) {
            return "redirect:/admin/categorias?categoriaEliminada";
        }
        return "redirect:/admin/platos/inicio?categoriaEliminada";
    }

    // ── Helper: guardar imagen en disco ──────────────────
    private String guardarImagen(MultipartFile file) {
        try {
            Path carpeta = Paths.get(IMG_DIR);
            if (!Files.exists(carpeta)) {
                Files.createDirectories(carpeta);
            }
            String extension   = obtenerExtension(file.getOriginalFilename());
            String nombreUnico = UUID.randomUUID().toString() + "." + extension;
            Files.copy(file.getInputStream(), carpeta.resolve(nombreUnico),
                       StandardCopyOption.REPLACE_EXISTING);
            return nombreUnico;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String obtenerExtension(String nombre) {
        if (nombre == null || !nombre.contains(".")) return "jpg";
        return nombre.substring(nombre.lastIndexOf('.') + 1).toLowerCase();
    }
}
