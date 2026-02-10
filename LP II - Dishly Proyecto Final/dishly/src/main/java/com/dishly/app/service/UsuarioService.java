package com.dishly.app.service;

import com.dishly.app.model.Usuario;
import com.dishly.app.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registrar(String name, String email, String password) {

        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Usuario u = new Usuario();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password));
        u.setRol("CLIENTE");
        u.setEstado(true);

        usuarioRepository.save(u);
    }
}