package com.trabajo_practico.gestion_comercial.service;
import com.trabajo_practico.gestion_comercial.dto.CreateUpdateFacturaDTO;
import com.trabajo_practico.gestion_comercial.dto.FacturaDTO;
import com.trabajo_practico.gestion_comercial.model.*;
import com.trabajo_practico.gestion_comercial.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final ClienteRepository clienteRepository;
    private final ProveedorRepository proveedorRepository;

    private final CompraRepository compraRepository;
    private final VentaRepository ventaRepository;

    private final ProductoRepository productoRepository;
    public FacturaService(
            FacturaRepository facturaRepository,
            ClienteRepository clienteRepository,
            ProveedorRepository proveedorRepository,
            CompraRepository compraRepository,
            VentaRepository ventaRepository,
            ProductoRepository productoRepository) {

        this.facturaRepository = facturaRepository;
        this.clienteRepository = clienteRepository;
        this.proveedorRepository = proveedorRepository;
        this.compraRepository = compraRepository;
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public FacturaDTO crearFactura(CreateUpdateFacturaDTO dto) {
        TipoFactura tipoFactura;
        try {
            tipoFactura = TipoFactura.valueOf(dto.getTipoFactura().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de factura inválido");
        }

        Long idClienteProv = dto.getIdClienteProv();
        boolean valido = switch (tipoFactura) {
            case COMPRA -> proveedorRepository.existsById(idClienteProv);
            case VENTA -> clienteRepository.existsById(idClienteProv);
        };
        if (!valido) throw new IllegalArgumentException("Cliente o proveedor no existe");

        // Crear y guardar la factura base
        final Factura factura = facturaRepository.save(generateFactura(dto));

        // Asociar compras o ventas completas
        switch (tipoFactura) {
            case COMPRA -> {
                List<Compra> compras = dto.getCompras().stream().map(c -> {
                    Producto producto = productoRepository.findById(c.getProductoId())
                            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado ID: " + c.getProductoId()));

                    producto.setStock(producto.getStock() + c.getCantidad());
                    productoRepository.save(producto);

                    Compra compra = new Compra();
                    compra.setProducto(producto);
                    compra.setCantidad(c.getCantidad());
                    compra.setPrecioUnitario(c.getPrecioUnitario());
                    compra.setTotalPorArticulo(c.getTotalPorArticulo());
                    compra.setFecha(LocalDateTime.now());
                    compra.setFactura(factura);
                    return compra;
                }).toList();

                compraRepository.saveAll(compras);
            }

            case VENTA -> {
                List<Venta> ventas = dto.getVentas().stream().map(v -> {
                    Producto producto = productoRepository.findById(v.getProductoId())
                            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado ID: " + v.getProductoId()));

                    if (producto.getStock() < v.getCantidad()) {
                        throw new IllegalArgumentException("Stock insuficiente para producto ID: " + v.getProductoId());
                    }

                    producto.setStock(producto.getStock() - v.getCantidad());
                    productoRepository.save(producto);

                    Venta venta = new Venta();
                    venta.setProducto(producto);
                    venta.setCantidad(v.getCantidad());
                    venta.setPrecioUnitario(v.getPrecioUnitario());
                    venta.setTotalPorArticulo(v.getTotalPorArticulo());
                    venta.setFecha(LocalDateTime.now());
                    venta.setFactura(factura);
                    return venta;
                }).toList();

                ventaRepository.saveAll(ventas);
            }
        }

        return new FacturaDTO(factura);
    }


    public boolean anularFactura(Long id) {
        return facturaRepository.findById(id).map(factura -> {
            factura.setEstado(EstadoFactura.ANULADA);
            facturaRepository.save(factura);
            return true;
        }).orElse(false);
    }

    public List<FacturaDTO> obtenerTodas() {
        return facturaRepository.findAll().stream().map(FacturaDTO::new).collect(Collectors.toList());
    }

    public Optional<FacturaDTO> obtenerPorId(Long id) {
        return facturaRepository.findById(id).map(FacturaDTO::new);
    }

    public FacturaDTO actualizarFactura(Long id, CreateUpdateFacturaDTO dto) {
        if (!facturaRepository.existsById(id)) return null;

        boolean valido = switch (dto.getTipoFactura().toUpperCase()) {
            case "COMPRA" -> proveedorRepository.existsById(dto.getIdClienteProv());
            case "VENTA" -> clienteRepository.existsById(dto.getIdClienteProv());
            default -> false;
        };

        if (!valido) return null;

        Factura factura = generateFactura(dto);
        factura.setId(id);
        return new FacturaDTO(facturaRepository.save(factura));
    }

    public boolean eliminarFactura(Long id) {
        if (facturaRepository.existsById(id)) {
            facturaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Factura generateFactura(CreateUpdateFacturaDTO dto) {
        var factura = new Factura();
        factura.setNumero(dto.getNumero());
        factura.setTotal(dto.getTotal());
        factura.setIdClienteProv(dto.getIdClienteProv());
        factura.setTipoFactura(dto.getTipoFactura());
        factura.setFormaPago(dto.getFormaPago());
        return factura;
    }
}
