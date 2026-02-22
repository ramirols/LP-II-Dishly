package com.dishly.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dishly.app.model.Categoria;
import com.dishly.app.model.Plato;
import com.dishly.app.service.CategoriaService;
import com.dishly.app.service.PlatoService;

import dto.CategoriaDTO;
import dto.PlatoDTO;

@Controller
@RequestMapping("/admin/platos")
public class PlatoController {
	@Autowired
	PlatoService platoService;
	@Autowired
	CategoriaService categoriaService;
	
	@GetMapping("/inicio")
	public String gestionarPlatos(Model model) {
		
		model.addAttribute("platoDTO", new PlatoDTO());
		model.addAttribute("categoriaDTO", new CategoriaDTO());
		model.addAttribute("platos", platoService.listarTodo());
		model.addAttribute("categorias", categoriaService.listarTodo());
		
		return "admin/platos";
	}
	
	
	@PostMapping("/guardar")
	public String guardarPlato(@ModelAttribute PlatoDTO platoDTO) {
		
		Plato plato;
		
		if (platoDTO.getId() != null) {
			plato = platoService.buscarPlatoPorId(platoDTO.getId());
		} else {
			plato = new Plato();
		}
		
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
	public String guardarCategoria(@ModelAttribute CategoriaDTO categoriaDTO) {
		
		Categoria categoria;
		
		if (categoriaDTO.getId() != null) {
			categoria = categoriaService.buscarCategoriaPorId(categoriaDTO.getId());
		} else {
			categoria = new Categoria();
		}
		
		categoria.setNombre(categoriaDTO.getNombre());
		categoria.setDescripcion(categoriaDTO.getDescripcion());
		
		categoriaService.guardarCategoria(categoria);
		
		return "redirect:/admin/platos/inicio?categoriaAgregada";
	}
	
	@PostMapping("/categoria/eliminar")
	public String eliminarCategoria(@ModelAttribute CategoriaDTO categoriaDTO) {
		categoriaService.eliminarCategoriaPorId(categoriaDTO.getId());
		return "redirect:/admin/platos/inicio?categoriaEliminada";
	}
}
