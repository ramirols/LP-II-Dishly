package com.dishly.app.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.dishly.app.model.Usuario;
import com.dishly.app.repository.UsuarioRepository;
import com.dishly.app.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Usuario> listarTodo() {
        return repo.findAll();
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public Usuario buscarUsuarioPorId(Integer id) {
        return repo.findById(id).get();
    }

    @Override
    public Usuario buscarPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    // Registro de cliente: valida email duplicado y cifra contrasena
    @Override
    public Usuario guardarUsuario(Usuario user) {
        if (repo.existsByEmail(user.getEmail()))
            throw new RuntimeException("El correo ya esta registrado");
        user.setEstado(true);
        user.setContrasenia(passwordEncoder.encode(user.getContrasenia()));
        return repo.save(user);
    }

    // Guardar directo sin validaciones (para edicion/creacion de staff desde admin)
    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        return repo.save(usuario);
    }

    @Override
    public void eliminarUsuario(Usuario user) {
        repo.delete(user);
    }

    @Override
    public void eliminarUsuarioPorId(Integer id) {
        repo.deleteById(id);
    }
}
