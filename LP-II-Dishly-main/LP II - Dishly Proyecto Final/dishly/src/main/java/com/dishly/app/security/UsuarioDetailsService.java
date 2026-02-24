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
        Usuario usuario = repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Correo no encontrado"));
		
        List<GrantedAuthority> authorities = usuario.getRoles()
                .stream().map(rol ->
                        new SimpleGrantedAuthority(rol.getNombre())
                ).collect(Collectors.toList());
        
		return new User(usuario.getEmail(), usuario.getContrasenia(), authorities);
	}

}
