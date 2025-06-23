package com.trabajo_practico.gestion_comercial.service.impl;

import com.trabajo_practico.gestion_comercial.dto.*;
import com.trabajo_practico.gestion_comercial.model.Producto;
import com.trabajo_practico.gestion_comercial.model.Venta;
import com.trabajo_practico.gestion_comercial.model.Compra;
import com.trabajo_practico.gestion_comercial.repository.ClienteRepository;
import com.trabajo_practico.gestion_comercial.repository.ProductoRepository;
import com.trabajo_practico.gestion_comercial.repository.VentaRepository;
import com.trabajo_practico.gestion_comercial.repository.CompraRepository;
import com.trabajo_practico.gestion_comercial.service.ReporteServiceG;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteServiceG {

    private final VentaRepository ventaRepository;
    private final CompraRepository compraRepository;

    private final ProductoRepository productoRepository;

    private final ClienteRepository clienteRepository;
    private final ProductoRepository proveedorRepository;
    public ReporteServiceImpl(VentaRepository ventaRepository, CompraRepository compraRepository, ProductoRepository productoRepository, ClienteRepository clienteRepository, ProductoRepository proveedorRepository) {
        this.ventaRepository = ventaRepository;
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
        this.proveedorRepository = proveedorRepository;
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
    public List<MovimientoReporteDTO> obtenerComprasPorRango(LocalDateTime inicio, LocalDateTime fin) {
        List<Compra> compras = compraRepository.findByFechaBetween(inicio, fin);

        return compras.stream()
                .map(c -> new MovimientoReporteDTO(
                        c.getProducto().getNombre(),  // Producto debe existir en Compra
                        c.getCantidad(),
                        c.getPrecioUnitario(),
                        c.getTotalPorArticulo(),
                        c.getFecha()
                )).toList();
    }
    @Override
    public List<ProductoMasVendidoDTO> obtenerProductosMasVendidos(LocalDateTime inicio, LocalDateTime fin) {
        List<Venta> ventas = ventaRepository.findByFechaBetween(inicio, fin);

        return ventas.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getProducto().getNombre(),
                        Collectors.summingLong(Venta::getCantidad)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> new ProductoMasVendidoDTO(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Override
    public List<MovimientoReporteDTO> obtenerVentasPorRango(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByFechaBetween(inicio, fin)
                .stream()
                .map(v -> new MovimientoReporteDTO(
                        v.getProducto().getNombre(),
                        v.getCantidad(),
                        v.getPrecioUnitario(),
                        v.getTotalPorArticulo(),
                        v.getFecha()
                )).toList();
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
    @Override
    public List<TopEntidadDTO> obtenerTopClientes(LocalDateTime inicio, LocalDateTime fin) {
        List<Venta> ventas = ventaRepository.findByFechaBetween(inicio, fin);

        Map<Long, Double> acumuladoPorCliente = ventas.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getFactura().getIdClienteProv(),
                        Collectors.summingDouble(Venta::getTotalPorArticulo)
                ));

        return acumuladoPorCliente.entrySet().stream()
                .map(entry -> {
                    Long id = entry.getKey();
                    double total = entry.getValue();
                    var cliente = clienteRepository.findById(id).orElse(null);
                    String nombre = (cliente != null)
                            ? cliente.getNombre() + " " + cliente.getApellido()
                            : "Cliente desconocido";

                    return new TopEntidadDTO(nombre, id, total);
                })
                .sorted(Comparator.comparing(TopEntidadDTO::getTotal).reversed())
                .limit(15)
                .toList();
    }

    @Override
    public List<TopEntidadDTO> obtenerTopProveedores(LocalDateTime inicio, LocalDateTime fin) {
        List<Compra> compras = compraRepository.findByFechaBetween(inicio, fin);

        Map<Long, Double> acumuladoPorProveedor = compras.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getFactura().getIdClienteProv(),
                        Collectors.summingDouble(Compra::getTotalPorArticulo)
                ));

        return acumuladoPorProveedor.entrySet().stream()
                .map(entry -> {
                    Long id = entry.getKey();
                    double total = entry.getValue();
                    var proveedor = proveedorRepository.findById(id).orElse(null);
                    String nombre = (proveedor != null)
                            ? proveedor.getNombre()
                            : "Proveedor desconocido";

                    return new TopEntidadDTO(nombre, id, total);
                })
                .sorted(Comparator.comparing(TopEntidadDTO::getTotal).reversed())
                .limit(15)
                .toList();
    }
    @Override
    public UtilidadDTO obtenerReporteUtilidad(LocalDateTime inicio, LocalDateTime fin) {
        List<Venta> ventas = ventaRepository.findByFechaBetween(inicio, fin);
        List<Compra> compras = compraRepository.findByFechaBetween(inicio, fin);

        double totalVentas = ventas.stream().mapToDouble(Venta::getTotalPorArticulo).sum();
        double totalCompras = compras.stream().mapToDouble(Compra::getTotalPorArticulo).sum();

        return new UtilidadDTO(totalVentas, totalCompras);
    }


}
