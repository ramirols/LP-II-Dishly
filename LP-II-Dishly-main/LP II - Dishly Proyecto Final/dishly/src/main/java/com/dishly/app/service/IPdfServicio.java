package com.dishly.app.service;

import com.dishly.app.model.Pedido;
import java.util.List;

public interface IPdfServicio {

    byte[] generarReportePedidos(List<Pedido> pedidos, long totalPedidos, java.math.BigDecimal ventasTotales);
}
