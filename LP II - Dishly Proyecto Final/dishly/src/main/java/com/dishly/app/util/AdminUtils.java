package com.dishly.app.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AdminUtils {

    public static String getAdminNombre(
            com.dishly.app.service.UsuarioService usuarioService) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth != null ? auth.getName() : "";
        return usuarioService.buscarPorEmail(email)
                .map(u -> u.getNombre())
                .orElse("Administrador");
    }
}