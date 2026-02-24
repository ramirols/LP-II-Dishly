package com.dishly.app.impl;

import com.dishly.app.model.Pedido;
import com.dishly.app.service.IPdfServicio;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PdfServicioImpl implements IPdfServicio {

    // Colores del tema Dishly (igual que el dashboard)
    private static final Color COLOR_PRIMARIO   = new Color(30,  41,  59);   // slate-800
    private static final Color COLOR_ACENTO     = new Color(99,  102, 241);  // indigo-500
    private static final Color COLOR_VERDE      = new Color(22,  163, 74);   // green-600
    private static final Color COLOR_FONDO_FILA = new Color(248, 250, 252);  // slate-50
    private static final Color COLOR_BORDE      = new Color(226, 232, 240);  // slate-200
    private static final Color COLOR_TEXTO_GRIS = new Color(100, 116, 139);  // slate-500
    private static final Color COLOR_BLANCO     = Color.WHITE;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public byte[] generarReportePedidos(List<Pedido> pedidos, long totalPedidos, BigDecimal ventasTotales) {

        System.out.println("=== GENERANDO REPORTE DISHLY ===");
        System.out.println("Total pedidos: " + totalPedidos);
        System.out.println("Ventas totales: S/ " + ventasTotales);
        System.out.println("================================");

        try {
            // ─── DISEÑO BASE ───
            JasperDesign design = new JasperDesign();
            design.setName("ReportePedidosDishly");
            design.setPageWidth(595);
            design.setPageHeight(842);
            design.setLeftMargin(30);
            design.setRightMargin(30);
            design.setTopMargin(20);
            design.setBottomMargin(20);
            design.setColumnWidth(535);
            design.setColumnSpacing(0);

            // ─── PARÁMETROS ───
            String[] nombreParams = {"P_TITULO", "P_TOTAL_PEDIDOS", "P_VENTAS_TOTALES", "P_FECHA_GENERACION"};
            for (String nombre : nombreParams) {
                JRDesignParameter param = new JRDesignParameter();
                param.setName(nombre);
                param.setValueClass(String.class);
                design.addParameter(param);
            }

            // Sin query (usamos JRBeanCollectionDataSource)
            design.setQuery(new JRDesignQuery());

            // ─── FIELDS del bean Pedido ───
            agregarField(design, "id",            Integer.class);
            agregarField(design, "usuario",       com.dishly.app.model.Usuario.class);
            agregarField(design, "fechaCreacion", java.time.LocalDateTime.class);
            agregarField(design, "total",         BigDecimal.class);
            agregarField(design, "metodoPago",    String.class);
            agregarField(design, "direccionEnvio",String.class);
            agregarField(design, "estado",        Boolean.class);

            // ══════════════════════════════════════
            //  TITLE BAND — Header con logo y título
            // ══════════════════════════════════════
            JRDesignBand titleBand = new JRDesignBand();
            titleBand.setHeight(90);

            // Fondo oscuro
            JRDesignRectangle fondoHeader = new JRDesignRectangle();
            fondoHeader.setX(0); fondoHeader.setY(0);
            fondoHeader.setWidth(535); fondoHeader.setHeight(80);
            fondoHeader.setMode(ModeEnum.OPAQUE);
            fondoHeader.setBackcolor(COLOR_PRIMARIO);
            fondoHeader.setForecolor(COLOR_PRIMARIO);
            titleBand.addElement(fondoHeader);

            // Barra de acento izquierda
            JRDesignRectangle barraAccento = new JRDesignRectangle();
            barraAccento.setX(0); barraAccento.setY(0);
            barraAccento.setWidth(5); barraAccento.setHeight(80);
            barraAccento.setMode(ModeEnum.OPAQUE);
            barraAccento.setBackcolor(COLOR_ACENTO);
            barraAccento.setForecolor(COLOR_ACENTO);
            titleBand.addElement(barraAccento);

            titleBand.addElement(textoFijo("Dishly",
                    15, 12, 200, 28, 20, true, COLOR_BLANCO, HorizontalTextAlignEnum.LEFT));

            titleBand.addElement(textoFijo("Panel Administrador",
                    15, 40, 250, 16, 9, false, new Color(148, 163, 184), HorizontalTextAlignEnum.LEFT));

            // Título reporte (derecha)
            titleBand.addElement(campoDinamico("$P{P_TITULO}",
                    250, 12, 280, 20, 12, true, COLOR_BLANCO, HorizontalTextAlignEnum.RIGHT));

            // Fecha generación
            titleBand.addElement(campoDinamico("$P{P_FECHA_GENERACION}",
                    250, 38, 280, 14, 8, false, new Color(148, 163, 184), HorizontalTextAlignEnum.RIGHT));

            design.setTitle(titleBand);

            // ══════════════════════════════════════
            //  PAGE HEADER — KPIs resumen
            // ══════════════════════════════════════
            JRDesignBand pageHeader = new JRDesignBand();
            pageHeader.setHeight(60);

            // Card total pedidos
            agregarCard(pageHeader, 0, 5, 160, 48,
                    "TOTAL PEDIDOS", "$P{P_TOTAL_PEDIDOS}",
                    new Color(239, 246, 255), COLOR_ACENTO);

            // Card ventas totales
            agregarCard(pageHeader, 170, 5, 160, 48,
                    "VENTAS TOTALES (S/)", "$P{P_VENTAS_TOTALES}",
                    new Color(240, 253, 244), COLOR_VERDE);

            design.setPageHeader(pageHeader);

            // ══════════════════════════════════════
            //  COLUMN HEADER — Cabecera de la tabla
            // ══════════════════════════════════════
            JRDesignBand colHeader = new JRDesignBand();
            colHeader.setHeight(26);

            JRDesignRectangle fondoCol = new JRDesignRectangle();
            fondoCol.setX(0); fondoCol.setY(0);
            fondoCol.setWidth(535); fondoCol.setHeight(26);
            fondoCol.setMode(ModeEnum.OPAQUE);
            fondoCol.setBackcolor(COLOR_ACENTO);
            fondoCol.setForecolor(COLOR_ACENTO);
            colHeader.addElement(fondoCol);

            // Columnas: #ID | Cliente | Fecha | Pago | Total | Estado
            colHeader.addElement(textoFijo("#ID",      5,  6, 35,  14, 8, true, COLOR_BLANCO, HorizontalTextAlignEnum.LEFT));
            colHeader.addElement(textoFijo("CLIENTE",  45, 6, 110, 14, 8, true, COLOR_BLANCO, HorizontalTextAlignEnum.LEFT));
            colHeader.addElement(textoFijo("FECHA",    160, 6, 90, 14, 8, true, COLOR_BLANCO, HorizontalTextAlignEnum.LEFT));
            colHeader.addElement(textoFijo("PAGO",     255, 6, 65, 14, 8, true, COLOR_BLANCO, HorizontalTextAlignEnum.LEFT));
            colHeader.addElement(textoFijo("TOTAL S/", 325, 6, 70, 14, 8, true, COLOR_BLANCO, HorizontalTextAlignEnum.RIGHT));
            colHeader.addElement(textoFijo("ESTADO",   400, 6, 130,14, 8, true, COLOR_BLANCO, HorizontalTextAlignEnum.LEFT));

            design.setColumnHeader(colHeader);

            // ══════════════════════════════════════
            //  DETAIL BAND — Filas de la tabla
            // ══════════════════════════════════════
            JRDesignBand detailBand = new JRDesignBand();
            detailBand.setHeight(22);

            // Fondo alternado (siempre slate-50, se ve limpio)
            JRDesignRectangle fondoFila = new JRDesignRectangle();
            fondoFila.setX(0); fondoFila.setY(0);
            fondoFila.setWidth(535); fondoFila.setHeight(22);
            fondoFila.setMode(ModeEnum.OPAQUE);
            fondoFila.setBackcolor(COLOR_FONDO_FILA);
            fondoFila.setForecolor(COLOR_BORDE);
            detailBand.addElement(fondoFila);

            // ID
            detailBand.addElement(campoDinamico(
                    "\"#\" + $F{id}",
                    5, 5, 35, 14, 8, false, COLOR_TEXTO_GRIS, HorizontalTextAlignEnum.LEFT));

            // Cliente (nombre del usuario)
            detailBand.addElement(campoDinamico(
                    "$F{usuario} != null ? $F{usuario}.getNombre() : \"—\"",
                    45, 5, 110, 14, 8, true, COLOR_PRIMARIO, HorizontalTextAlignEnum.LEFT));

            // Fecha
            detailBand.addElement(campoDinamico(
                    "$F{fechaCreacion} != null ? $F{fechaCreacion}.format(java.time.format.DateTimeFormatter.ofPattern(\"dd/MM/yy HH:mm\")) : \"—\"",
                    160, 5, 90, 14, 8, false, COLOR_TEXTO_GRIS, HorizontalTextAlignEnum.LEFT));

            // Método pago
            detailBand.addElement(campoDinamico(
                    "$F{metodoPago} != null ? $F{metodoPago} : \"—\"",
                    255, 5, 65, 14, 8, false, COLOR_TEXTO_GRIS, HorizontalTextAlignEnum.LEFT));

            // Total
            detailBand.addElement(campoDinamico(
                    "$F{total} != null ? String.format(\"%.2f\", $F{total}) : \"0.00\"",
                    325, 5, 70, 14, 8, true, COLOR_PRIMARIO, HorizontalTextAlignEnum.RIGHT));

            // Estado
            detailBand.addElement(campoDinamico(
                    "$F{estado} ? \"✓ Completado\" : \"⏳ Pendiente\"",
                    400, 5, 130, 14, 8, false, COLOR_TEXTO_GRIS, HorizontalTextAlignEnum.LEFT));

            // Línea separadora
            JRDesignLine lineaFila = new JRDesignLine();
            lineaFila.setX(0); lineaFila.setY(21);
            lineaFila.setWidth(535); lineaFila.setHeight(1);
            lineaFila.setForecolor(COLOR_BORDE);
            detailBand.addElement(lineaFila);

            ((JRDesignSection) design.getDetailSection()).addBand(detailBand);

            // ══════════════════════════════════════
            //  PAGE FOOTER
            // ══════════════════════════════════════
            JRDesignBand footer = new JRDesignBand();
            footer.setHeight(25);

            JRDesignLine lineaPie = new JRDesignLine();
            lineaPie.setX(0); lineaPie.setY(2);
            lineaPie.setWidth(535); lineaPie.setHeight(1);
            lineaPie.setForecolor(COLOR_BORDE);
            footer.addElement(lineaPie);

            footer.addElement(textoFijo("© 2026 Dishly · Proyecto Académico",
                    0, 8, 300, 14, 8, false, COLOR_TEXTO_GRIS, HorizontalTextAlignEnum.LEFT));

            // Número de página
            JRDesignTextField pagina = new JRDesignTextField();
            JRDesignExpression expPagina = new JRDesignExpression();
            expPagina.setText("\"Pág. \" + $V{PAGE_NUMBER}");
            pagina.setExpression(expPagina);
            pagina.setX(400); pagina.setY(8);
            pagina.setWidth(135); pagina.setHeight(14);
            pagina.setFontSize(8f);
            pagina.setForecolor(COLOR_TEXTO_GRIS);
            pagina.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
            pagina.setMode(ModeEnum.TRANSPARENT);
            footer.addElement(pagina);

            design.setPageFooter(footer);

            // ─── COMPILAR Y LLENAR ───
            JasperReport jasperReport = JasperCompileManager.compileReport(design);

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("P_TITULO",          "Reporte de Pedidos");
            parametros.put("P_TOTAL_PEDIDOS",   String.valueOf(totalPedidos));
            parametros.put("P_VENTAS_TOTALES",  "S/ " + String.format("%.2f", ventasTotales));
            parametros.put("P_FECHA_GENERACION","Generado: " + java.time.LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

            System.out.println("=== PARÁMETROS JASPER ===");
            parametros.forEach((k, v) -> System.out.println(k + " = " + v));
            System.out.println("=========================");

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(pedidos);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            System.out.println("=== ERROR EN JASPER ===");
            e.printStackTrace();
            System.out.println("=======================");
            return new byte[0];
        }
    }

    // ─── HELPERS (igual que tu otro proyecto) ───

    private void agregarField(JasperDesign design, String nombre, Class<?> clase) throws JRException {
        JRDesignField field = new JRDesignField();
        field.setName(nombre);
        field.setValueClass(clase);
        design.addField(field);
    }

    private JRDesignStaticText textoFijo(
            String texto, int x, int y, int ancho, int alto,
            int tamFuente, boolean negrita,
            Color color, HorizontalTextAlignEnum alineacion) {

        JRDesignStaticText st = new JRDesignStaticText();
        st.setText(texto);
        st.setX(x); st.setY(y);
        st.setWidth(ancho); st.setHeight(alto);
        st.setFontSize((float) tamFuente);
        st.setBold(negrita);
        st.setForecolor(color);
        st.setHorizontalTextAlign(alineacion);
        st.setMode(ModeEnum.TRANSPARENT);
        return st;
    }

    private JRDesignTextField campoDinamico(
            String expresion, int x, int y, int ancho, int alto,
            int tamFuente, boolean negrita,
            Color color, HorizontalTextAlignEnum alineacion) {

        JRDesignTextField tf = new JRDesignTextField();
        JRDesignExpression exp = new JRDesignExpression();
        exp.setText(expresion);
        tf.setExpression(exp);
        tf.setX(x); tf.setY(y);
        tf.setWidth(ancho); tf.setHeight(alto);
        tf.setFontSize((float) tamFuente);
        tf.setBold(negrita);
        tf.setForecolor(color);
        tf.setHorizontalTextAlign(alineacion);
        tf.setMode(ModeEnum.TRANSPARENT);
        tf.setBlankWhenNull(true);
        return tf;
    }

    private void agregarCard(JRDesignBand band,
                              int x, int y, int ancho, int alto,
                              String label, String expresionValor,
                              Color colorFondo, Color colorValor) throws JRException {

        JRDesignRectangle bg = new JRDesignRectangle();
        bg.setX(x); bg.setY(y);
        bg.setWidth(ancho); bg.setHeight(alto);
        bg.setMode(ModeEnum.OPAQUE);
        bg.setBackcolor(colorFondo);
        bg.setForecolor(colorFondo);
        band.addElement(bg);

        band.addElement(textoFijo(label,
                x + 8, y + 6, ancho - 16, 14,
                7, false, COLOR_TEXTO_GRIS, HorizontalTextAlignEnum.LEFT));

        band.addElement(campoDinamico(expresionValor,
                x + 8, y + 22, ancho - 16, 20,
                13, true, colorValor, HorizontalTextAlignEnum.LEFT));
    }
}
