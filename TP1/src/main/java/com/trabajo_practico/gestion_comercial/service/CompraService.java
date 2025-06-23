package com.trabajo_practico.gestion_comercial.service;

import com.trabajo_practico.gestion_comercial.dto.CompraConFacturaDTO;
import com.trabajo_practico.gestion_comercial.dto.CompraDTO;
import com.trabajo_practico.gestion_comercial.dto.CreateFacturaDTO;
import com.trabajo_practico.gestion_comercial.dto.CreateUpdateCompraDTO;
import com.trabajo_practico.gestion_comercial.model.Compra;
import com.trabajo_practico.gestion_comercial.model.EstadoFactura;
import com.trabajo_practico.gestion_comercial.model.Factura;
import com.trabajo_practico.gestion_comercial.model.Producto;
import com.trabajo_practico.gestion_comercial.repository.CompraRepository;
import com.trabajo_practico.gestion_comercial.repository.FacturaRepository;
import com.trabajo_practico.gestion_comercial.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;
    private final FacturaRepository facturaRepository;

    public CompraService(CompraRepository compraRepository, ProductoRepository productoRepository, FacturaRepository facturaRepository) {
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
        this.facturaRepository = facturaRepository;
    }

    @Transactional
    public CompraDTO crearCompra(CompraConFacturaDTO wrapperDto) {
        CreateUpdateCompraDTO compraDto = wrapperDto.getCompra();
        CreateFacturaDTO facturaDto = wrapperDto.getFactura();

        Producto producto = productoRepository.findById(compraDto.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // --- Lógica para construir factura ---
        Factura factura = new Factura();

        if (facturaDto != null) {

            factura.setTotal(facturaDto.getTotal() != null
                    ? facturaDto.getTotal()
                    : compraDto.getPrecioUnitario() * compraDto.getCantidad());
            factura.setIdClienteProv(facturaDto.getIdClienteProv() != null ? facturaDto.getIdClienteProv() : 0L);
            factura.setTipoFactura(facturaDto.getTipoFactura() != null ? facturaDto.getTipoFactura() : "COMPRA");
            factura.setFormaPago(facturaDto.getFormaPago() != null ? facturaDto.getFormaPago() : "EFECTIVO");
        } else {
            factura.setTotal(compraDto.getPrecioUnitario() * compraDto.getCantidad());
            factura.setIdClienteProv(0L);
            factura.setTipoFactura("COMPRA");
            factura.setFormaPago("EFECTIVO");
        }

        factura.setEstado(EstadoFactura.VIGENTE);
        factura.setFecha(LocalDateTime.now());

        Factura facturaGuardada = facturaRepository.save(factura);

        // --- Stock ---
        producto.setStock(producto.getStock() + compraDto.getCantidad());
        productoRepository.save(producto);

        // --- Crear compra ---
        Compra compra = generateCompra(compraDto);
        compra.setFactura(facturaGuardada);
        compra.setFecha(LocalDateTime.now());

        return new CompraDTO(compraRepository.save(compra));
    }



    public List<CompraDTO> obtenerTodos() {
        return compraRepository.findAll().stream().map(CompraDTO::new).collect(Collectors.toList());
    }

    public Optional<CompraDTO> obtenerPorId(Long id) {
        return compraRepository.findById(id).map(CompraDTO::new);
    }

    public CompraDTO actualizarCompra(Long id, CreateUpdateCompraDTO dto) {
        Compra compraExistente = compraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Compra no encontrada"));

        Producto productoAnterior = compraExistente.getProducto();
        int cantidadAnterior = compraExistente.getCantidad();

        // Revertir stock del producto anterior
        productoAnterior.setStock(productoAnterior.getStock() - cantidadAnterior);
        productoRepository.save(productoAnterior);

        // Generar nueva compra con generateCompra
        Compra compraActualizada = generateCompra(dto);
        compraActualizada.setId(id);

        Producto nuevoProducto = compraActualizada.getProducto();

        // Sumar stock con la nueva cantidad
        nuevoProducto.setStock(nuevoProducto.getStock() + compraActualizada.getCantidad());
        productoRepository.save(nuevoProducto);

        compraActualizada.setFecha(LocalDateTime.now());

        return new CompraDTO(compraRepository.save(compraActualizada));
    }

    public boolean eliminarCompra(Long id) {
        var compraOpt = compraRepository.findById(id);
        if (compraOpt.isEmpty()) {
            return false;
        }

        Compra compra = compraOpt.get();
        Producto producto = compra.getProducto();

        // Restar la cantidad comprada al stock del producto
        producto.setStock(producto.getStock() - compra.getCantidad());
        productoRepository.save(producto);

        compraRepository.deleteById(id);
        return true;
    }

    private Compra generateCompra(CreateUpdateCompraDTO dto) {
        Compra compra = new Compra();

        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        compra.setProducto(producto);

        compra.setCantidad(dto.getCantidad());
        compra.setPrecioUnitario(dto.getPrecioUnitario());

        if (dto.getTotalPorArticulo() != null) {
            compra.setTotalPorArticulo(dto.getTotalPorArticulo());
        } else {
            compra.setTotalPorArticulo(dto.getPrecioUnitario() * dto.getCantidad());
        }

        return compra;
    }

}
