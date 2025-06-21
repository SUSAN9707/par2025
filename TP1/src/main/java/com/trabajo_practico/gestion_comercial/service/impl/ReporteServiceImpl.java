package com.trabajo_practico.gestion_comercial.service.impl;

import com.trabajo_practico.gestion_comercial.dto.InventarioCompletoDTO;
import com.trabajo_practico.gestion_comercial.dto.InventarioProductoDTO;
import com.trabajo_practico.gestion_comercial.dto.MovimientoMensualDTO;
import com.trabajo_practico.gestion_comercial.model.Producto;
import com.trabajo_practico.gestion_comercial.model.Venta;
import com.trabajo_practico.gestion_comercial.model.Compra;
import com.trabajo_practico.gestion_comercial.repository.ProductoRepository;
import com.trabajo_practico.gestion_comercial.repository.VentaRepository;
import com.trabajo_practico.gestion_comercial.repository.CompraRepository;
import com.trabajo_practico.gestion_comercial.service.ReporteServiceG;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteServiceG {

    private final VentaRepository ventaRepository;
    private final CompraRepository compraRepository;

    private final ProductoRepository productoRepository;

    public ReporteServiceImpl(VentaRepository ventaRepository, CompraRepository compraRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public MovimientoMensualDTO obtenerMovimientoMensual(Integer mes, Integer anio) {
        LocalDate hoy = LocalDate.now();
        int mesConsulta = (mes != null) ? mes : hoy.getMonthValue();
        int anioConsulta = (anio != null) ? anio : hoy.getYear();

        List<Venta> ventas = ventaRepository.findByMesYAnio(mesConsulta, anioConsulta);
        List<Compra> compras = compraRepository.findByMesYAnio(mesConsulta, anioConsulta);

        long cantidadVentas = ventas.size();
        double totalVentas = ventas.stream().mapToDouble(Venta::getTotalPorArticulo).sum();

        long cantidadCompras = compras.size();
        double totalCompras = compras.stream().mapToDouble(Compra::getTotalPorArticulo).sum();

        return new MovimientoMensualDTO(
                mesConsulta,
                cantidadVentas,
                totalVentas,
                cantidadCompras,
                totalCompras
        );
    }
    @Override
    public List<InventarioProductoDTO> obtenerInventarioActual() {
        List<Producto> productos = productoRepository.findAll();

        return productos.stream()
                .map(p -> new InventarioProductoDTO(
                        p.getNombre(),
                        p.getDescripcion(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getStockMinimo()))
                .toList();
    }

    @Override
    public InventarioCompletoDTO obtenerInventarioProductos() {
        List<Producto> productos = productoRepository.findAll();

        List<InventarioProductoDTO> reporte = productos.stream()
                .map(p -> new InventarioProductoDTO(
                        p.getNombre(),
                        p.getDescripcion(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getStockMinimo()
                ))
                .toList();

        BigDecimal totalInventario = reporte.stream()
                .map(InventarioProductoDTO::getValorTotalProducto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new InventarioCompletoDTO(totalInventario, reporte);
    }

}
