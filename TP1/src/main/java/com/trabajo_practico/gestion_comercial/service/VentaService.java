package com.trabajo_practico.gestion_comercial.service;

import com.trabajo_practico.gestion_comercial.dto.VentaDTO;
import com.trabajo_practico.gestion_comercial.dto.CreateUpdateVentaDTO;
import com.trabajo_practico.gestion_comercial.model.Producto;
import com.trabajo_practico.gestion_comercial.model.Venta;
import com.trabajo_practico.gestion_comercial.repository.ProductoRepository;
import com.trabajo_practico.gestion_comercial.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public VentaService(VentaRepository ventaRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    public VentaDTO crearVenta(CreateUpdateVentaDTO dto) {
        // Crear la entidad Venta con el producto cargado desde el repo
        Venta venta = generateVenta(dto);
        venta.setFecha(LocalDateTime.now());

        // Validar stock
        Producto producto = venta.getProducto();
        if (producto.getStock() < venta.getCantidad()) {
            throw new IllegalArgumentException("Stock insuficiente");
        }

        // Actualizar stock
        producto.setStock(producto.getStock() - venta.getCantidad());
        productoRepository.save(producto);

        return new VentaDTO(ventaRepository.save(venta));
    }


    public List<VentaDTO> obtenerTodos() {
        return ventaRepository.findAll().stream().map(VentaDTO::new).collect(Collectors.toList());
    }

    public Optional<VentaDTO> obtenerPorId(Long id) {
        return ventaRepository.findById(id).map(VentaDTO::new);
    }

    public VentaDTO actualizarVenta(Long id, CreateUpdateVentaDTO dto) {
        Optional<Venta> ventaOptional = ventaRepository.findById(id);
        if (ventaOptional.isEmpty()) {
            return null;
        }

        Venta ventaExistente = ventaOptional.get();

        // Revertir stock del producto anterior
        Producto productoAnterior = ventaExistente.getProducto();
        productoAnterior.setStock(productoAnterior.getStock() + ventaExistente.getCantidad());
        productoRepository.save(productoAnterior);

        // Generar nueva venta desde DTO
        Venta nuevaVenta = generateVenta(dto);

        // Validar stock del nuevo producto
        Producto nuevoProducto = nuevaVenta.getProducto();
        if (nuevoProducto.getStock() < nuevaVenta.getCantidad()) {
            throw new IllegalArgumentException("Stock insuficiente para realizar la venta");
        }

        // Disminuir stock del nuevo producto
        nuevoProducto.setStock(nuevoProducto.getStock() - nuevaVenta.getCantidad());
        productoRepository.save(nuevoProducto);

        // Reutilizar ID de la venta existente
        nuevaVenta.setId(id);

        return new VentaDTO(ventaRepository.save(nuevaVenta));
    }


    public boolean eliminarVenta(Long id) {
        Optional<Venta> ventaOpt = ventaRepository.findById(id);
        if (ventaOpt.isEmpty()) {
            return false;
        }

        Venta venta = ventaOpt.get();
        Producto producto = venta.getProducto();

        // Revertir stock: sumar la cantidad de la venta al stock actual
        producto.setStock(producto.getStock() + venta.getCantidad());
        productoRepository.save(producto);

        // Borrar la venta
        ventaRepository.deleteById(id);

        return true;
    }


    private Venta generateVenta(CreateUpdateVentaDTO dto) {
        Venta venta = new Venta();
        venta.setCantidad(dto.getCantidad());

        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        venta.setProducto(producto);

        return venta;
    }
}
