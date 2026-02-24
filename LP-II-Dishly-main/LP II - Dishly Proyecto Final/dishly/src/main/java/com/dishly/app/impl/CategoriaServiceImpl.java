package com.dishly.app.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dishly.app.model.Categoria;
import com.dishly.app.repository.CategoriaRepository;
import com.dishly.app.service.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService{
	
	@Autowired
	CategoriaRepository repo;
	
	public CategoriaServiceImpl(CategoriaRepository repo) {
		super();
		this.repo = repo;
	}

	@Override
	public List<Categoria> listarTodo() {
		return repo.findAll();
	}

	@Override
	public Categoria buscarCategoriaPorId(Integer id) {
		return repo.findById(id).get();
	}

	@Override
	public Categoria guardarCategoria(Categoria categoria) {
		return repo.save(categoria);
	}

	@Override
	public void eliminarCategoria(Categoria categoria) {
		repo.delete(categoria);
	}

	@Override
	public void eliminarCategoriaPorId(Integer id) {
		repo.deleteById(id);
	}

}
