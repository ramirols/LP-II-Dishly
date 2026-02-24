package com.dishly.app.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dishly.app.model.CarritoItem;
import com.dishly.app.model.DetallePedido;
import com.dishly.app.model.DetallePedidoId;
import com.dishly.app.model.Pedido;
import com.dishly.app.repository.DetallePedidoRepository;
import com.dishly.app.repository.PedidoRepository;
import com.dishly.app.service.PedidoService;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService{
	
	@Autowired
    private PedidoRepository repo;

    @Autowired
    private DetallePedidoRepository detalleRepo;

    @Override
    public List<Pedido> obtenerPedidosPorUsuario(Integer idUsuario) {
        // Asumiendo que en PedidoRepository tienes: findByUsuarioIdUsuarioOrderByIdDesc
        return repo.findByUsuarioIdUsuarioOrderByIdDesc(idUsuario);
    }
    
    @Override
    @Transactional
    public Pedido guardarPedidoCompleto(Pedido pedido, List<CarritoItem> carrito) {
        Pedido nuevoPedido = repo.save(pedido);

        for (CarritoItem item : carrito) {
            DetallePedido detalle = new DetallePedido();
            
            DetallePedidoId idCompuesto = new DetallePedidoId();
            idCompuesto.setPedido(nuevoPedido);
            idCompuesto.setPlato(item.getPlato());
            
            detalle.setId(idCompuesto);
            detalle.setCantidad(item.getCantidad());
            
            BigDecimal subtotal = item.getPlato().getPrecio().multiply(new BigDecimal(item.getCantidad()));
            detalle.setSubtotal(subtotal);
            
            detalleRepo.save(detalle); 
        }
        return nuevoPedido;
    }

    @Override public List<Pedido> listarTodo() { return repo.findAll(); }
    @Override public Pedido buscarPedidoPorId(Integer id) { return repo.findById(id).orElse(null); }
    @Override public Pedido guardarPedido(Pedido pedido) { return repo.save(pedido); }
    @Override public void eliminarPedido(Pedido pedido) { repo.delete(pedido); }
    @Override public void eliminarPedidoPorId(Integer id) { repo.deleteById(id); }
}
