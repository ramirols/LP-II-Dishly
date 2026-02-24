package com.dishly.app.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dishly.app.model.Usuario;
import com.dishly.app.repository.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService{
	
	@Autowired
	private UsuarioRepository repo;
	
	public UsuarioDetailsService(UsuarioRepository repo) {
		this.repo = repo;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    Usuario usuario = repo.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("Correo no encontrado"));
	    
	    List<GrantedAuthority> authorities = usuario.getRoles()
	            .stream().map(rol -> {
	                // el rol debe empezar con ROLE_ segun BD
	                String nombreRol = rol.getNombre().toUpperCase();
	                if (!nombreRol.startsWith("ROLE_")) {
	                    nombreRol = "ROLE_" + nombreRol;
	                }
	                return new SimpleGrantedAuthority(nombreRol);
	            }).collect(Collectors.toList());
	    
	    return new User(usuario.getEmail(), usuario.getContrasenia(), authorities);
	}
}
