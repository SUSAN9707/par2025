package com.trabajo_practico.gestion_comercial.service;

import com.trabajo_practico.gestion_comercial.dto.CreateFacturaDTO;
import com.trabajo_practico.gestion_comercial.dto.VentaConFacturaDTO;
import com.trabajo_practico.gestion_comercial.dto.VentaDTO;
import com.trabajo_practico.gestion_comercial.dto.CreateUpdateVentaDTO;
import com.trabajo_practico.gestion_comercial.model.EstadoFactura;
import com.trabajo_practico.gestion_comercial.model.Factura;
import com.trabajo_practico.gestion_comercial.model.Producto;
import com.trabajo_practico.gestion_comercial.model.Venta;
import com.trabajo_practico.gestion_comercial.repository.FacturaRepository;
import com.trabajo_practico.gestion_comercial.repository.ProductoRepository;
import com.trabajo_practico.gestion_comercial.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final FacturaRepository facturaRepository;

    public VentaService(VentaRepository ventaRepository, ProductoRepository productoRepository, FacturaRepository facturaRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.facturaRepository = facturaRepository;
    }

    @Transactional
    public VentaDTO crearVenta(VentaConFacturaDTO wrapperDto) {
        System.out.println("DEBUG - Venta recibida: " + wrapperDto);

        CreateUpdateVentaDTO ventaDto = wrapperDto.getVenta();
        CreateFacturaDTO facturaDto = wrapperDto.getFactura();

        Producto producto = productoRepository.findById(ventaDto.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // Validar stock
        if (producto.getStock() < ventaDto.getCantidad()) {
            throw new IllegalArgumentException("Stock insuficiente");
        }

        // Crear factura
        Factura factura = new Factura();

        if (facturaDto != null) {
            factura.setCliente(facturaDto.getCliente());
            factura.setNumero(facturaDto.getNumero());
            factura.setTotal(facturaDto.getTotal() != null
                    ? facturaDto.getTotal()
                    : ventaDto.getPrecioUnitario() * ventaDto.getCantidad());
            factura.setIdClienteProv(facturaDto.getIdClienteProv() != null ? facturaDto.getIdClienteProv() : 0L);
            factura.setTipoFactura(facturaDto.getTipoFactura() != null ? facturaDto.getTipoFactura() : "VENTA");
            factura.setFormaPago(facturaDto.getFormaPago() != null ? facturaDto.getFormaPago() : "EFECTIVO");
        } else {
            factura.setCliente("Cliente desconocido");
            factura.setNumero("FAC-" + System.currentTimeMillis());
            factura.setTotal(ventaDto.getPrecioUnitario() * ventaDto.getCantidad());
            factura.setIdClienteProv(0L);
            factura.setTipoFactura("VENTA");
            factura.setFormaPago("EFECTIVO");
        }

        factura.setEstado(EstadoFactura.VIGENTE);
        factura.setFecha(LocalDateTime.now());

        Factura facturaGuardada = facturaRepository.save(factura);

        // Actualizar stock del producto
        producto.setStock(producto.getStock() - ventaDto.getCantidad());
        productoRepository.save(producto);

        // Crear venta
        Venta venta = generateVenta(ventaDto);
        venta.setFactura(facturaGuardada);
        venta.setFecha(LocalDateTime.now());

        return new VentaDTO(ventaRepository.save(venta));
    }
    public List<VentaDTO> obtenerTodos() {
        return ventaRepository.findAll().stream().map(VentaDTO::new).collect(Collectors.toList());
    }

    public Optional<VentaDTO> obtenerPorId(Long id) {
        return ventaRepository.findById(id).map(VentaDTO::new);
    }

    public VentaDTO actualizarVenta(Long id, CreateUpdateVentaDTO dto) {
        Venta ventaExistente = ventaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));

        Producto productoAnterior = ventaExistente.getProducto();
        productoAnterior.setStock(productoAnterior.getStock() + ventaExistente.getCantidad());
        productoRepository.save(productoAnterior);

        Venta nuevaVenta = generateVenta(dto);
        nuevaVenta.setId(id);
        nuevaVenta.setFecha(LocalDateTime.now());

        Producto nuevoProducto = nuevaVenta.getProducto();
        if (nuevoProducto.getStock() < nuevaVenta.getCantidad()) {
            throw new IllegalArgumentException("Stock insuficiente para realizar la venta");
        }

        nuevoProducto.setStock(nuevoProducto.getStock() - nuevaVenta.getCantidad());
        productoRepository.save(nuevoProducto);

        return new VentaDTO(ventaRepository.save(nuevaVenta));
    }

    public boolean eliminarVenta(Long id) {
        Optional<Venta> ventaOpt = ventaRepository.findById(id);
        if (ventaOpt.isEmpty()) {
            return false;
        }

        Venta venta = ventaOpt.get();
        Producto producto = venta.getProducto();
        producto.setStock(producto.getStock() + venta.getCantidad());
        productoRepository.save(producto);

        ventaRepository.deleteById(id);
        return true;
    }

    private Venta generateVenta(CreateUpdateVentaDTO dto) {
        Venta venta = new Venta();

        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        venta.setProducto(producto);

        venta.setCantidad(dto.getCantidad());
        venta.setPrecioUnitario(dto.getPrecioUnitario());

        if (dto.getTotalPorArticulo() != null) {
            venta.setTotalPorArticulo(dto.getTotalPorArticulo());
        } else {
            venta.setTotalPorArticulo(dto.getPrecioUnitario() * dto.getCantidad());
        }

        return venta;
    }

}
