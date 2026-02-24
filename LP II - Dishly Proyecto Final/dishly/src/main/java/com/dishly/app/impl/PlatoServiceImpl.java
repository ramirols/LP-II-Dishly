package com.dishly.app.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dishly.app.model.Plato;
import com.dishly.app.repository.PlatoRepository;
import com.dishly.app.service.PlatoService;

@Service
public class PlatoServiceImpl implements PlatoService{
	
	@Autowired
	PlatoRepository repo;
	
	public PlatoServiceImpl(PlatoRepository repo) {
		super();
		this.repo = repo;
	}

	@Override
	public List<Plato> listarTodo() {
		return repo.findAll();
	}

	@Override
	public Plato buscarPlatoPorId(Integer id) {
		return repo.findById(id).get();
	}

	@Override
	public Plato guardarPlato(Plato plato) {
		return repo.save(plato);
	}

	@Override
	public void eliminarPlato(Plato plato) {
		repo.delete(plato);
	}

	@Override
	public void eliminarPlatoPorId(Integer id) {
		repo.deleteById(id);
	}

	@Override
	public List<Plato> buscarPorCategoria(Integer categoriaId) {
	    return repo.findByCategoriaIdAndEstadoTrue(categoriaId);
	}

	@Override
	public List<Plato> listarDestacados() {
	    return repo.findByDestacadoTrueAndEstadoTrue();
	}
}
