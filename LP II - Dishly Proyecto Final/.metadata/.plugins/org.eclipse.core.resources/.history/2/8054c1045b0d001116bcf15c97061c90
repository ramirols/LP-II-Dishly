package com.dishly.app.service;

import com.dishly.app.model.Plato;
import com.dishly.app.repository.PlatoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatoService {

    private final PlatoRepository platoRepository;

    public PlatoService(PlatoRepository platoRepository) {
        this.platoRepository = platoRepository;
    }

    public List<Plato> listarActivos() {
        return platoRepository.findAll()
                .stream()
                .filter(Plato::isEstado)
                .toList();
    }

    public Plato buscarPorId(Long id) {
        return platoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
    }

    public Plato guardar(Plato plato) {
        return platoRepository.save(plato);
    }
}