package com.dishly.app.service;

import java.util.List;

import com.dishly.app.model.Pedido;

public interface PedidoService {
	public List<Pedido> listarTodo();
	
	public Pedido buscarPedidoPorId(Integer id);
	
	public Pedido guardarPedido(Pedido pedido);
	
	public void eliminarPedido(Pedido pedido);
	
	public void eliminarPedidoPorId(Integer id);
}
