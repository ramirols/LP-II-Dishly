package com.dishly.app.service;

import java.util.List;

import com.dishly.app.model.DetallePedido;
import com.dishly.app.model.DetallePedidoId;

public interface DetallePedidoService {
	
	public List<DetallePedido> listarTodo();
	
	public DetallePedido buscarDetallePedidoPorId(DetallePedidoId id);
	
	public DetallePedido guardarDetallePedido(DetallePedido obj);
	
	public void eliminarDetallePedido(DetallePedido obj);
	
	public void eliminarDetallePedidoPorId(DetallePedidoId id);
}
