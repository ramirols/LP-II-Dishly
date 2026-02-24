package com.dishly.app.service;

import java.util.List;
import java.util.Optional;

import com.dishly.app.model.Usuario;

public interface UsuarioService {
	
	public List<Usuario> listarTodo();
	
	public Usuario buscarUsuarioPorId(Integer id);
	
	public Optional<Usuario> buscarPorEmail(String email);
	
	public Usuario guardarUsuario(Usuario user);
	
	public void eliminarUsuario(Usuario user);
	
	public void eliminarUsuarioPorId(Integer id);
}
