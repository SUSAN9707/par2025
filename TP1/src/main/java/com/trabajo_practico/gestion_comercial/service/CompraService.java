package com.trabajo_practico.gestion_comercial.service;

import com.trabajo_practico.gestion_comercial.dto.CompraDTO;
import com.trabajo_practico.gestion_comercial.dto.CreateUpdateCompraDTO;
import com.trabajo_practico.gestion_comercial.model.Compra;
import com.trabajo_practico.gestion_comercial.model.Producto;
import com.trabajo_practico.gestion_comercial.repository.CompraRepository;
import com.trabajo_practico.gestion_comercial.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;

    public CompraService(CompraRepository compraRepository, ProductoRepository productoRepository) {
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
    }

    public CompraDTO crearCompra(CreateUpdateCompraDTO dto) {
        // Buscar producto desde la base de datos
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // Actualizar stock del producto (sumar cantidad comprada)
        producto.setStock(producto.getStock() + dto.getCantidad());
        productoRepository.save(producto);

        // Crear objeto Compra usando generateCompra
        Compra compra = generateCompra(dto);
        compra.setFecha(LocalDateTime.now()); //

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

        // Generar nueva compra con generateCompra (ya busca y setea el producto)
        Compra compraActualizada = generateCompra(dto);
        compraActualizada.setId(id);

        Producto nuevoProducto = compraActualizada.getProducto();

        // Sumar stock con la nueva cantidad
        nuevoProducto.setStock(nuevoProducto.getStock() + compraActualizada.getCantidad());
        productoRepository.save(nuevoProducto);

        // Opcional: actualizar fecha
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
        compra.setCantidad(dto.getCantidad());

        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        compra.setProducto(producto);

        return compra;
    }
}
