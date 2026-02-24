package com.dishly.app.service;

import java.util.List;

import com.dishly.app.model.Plato;

public interface PlatoService {
	public List<Plato> listarTodo();
	
	public Plato buscarPlatoPorId(Integer id);
	
	List<Plato> buscarPorCategoria(Integer categoriaId);
	
	List<Plato> listarDestacados();
	
	public Plato guardarPlato(Plato plato);
	
	public void eliminarPlato(Plato plato);
	
	public void eliminarPlatoPorId(Integer id);
}
