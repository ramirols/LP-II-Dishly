package com.dishly.app.service;

import java.util.List;
import java.util.Optional;
import com.dishly.app.model.Usuario;

public interface UsuarioService {

    List<Usuario> listarTodo();

    Usuario buscarUsuarioPorId(Integer id);

    Optional<Usuario> buscarPorEmail(String email);

    // Para registro de cliente (valida email duplicado y cifra contrasena)
    Usuario guardarUsuario(Usuario user);

    void eliminarUsuario(Usuario user);

    void eliminarUsuarioPorId(Integer id);

    Usuario buscarPorId(Integer id);

    // Para guardar cambios directos sin validaciones extra (edicion de staff)
    Usuario actualizarUsuario(Usuario usuario);
}
