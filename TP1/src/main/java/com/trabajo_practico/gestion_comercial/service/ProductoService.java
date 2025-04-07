package com.trabajo_practico.gestion_comercial.service;

import com.trabajo_practico.gestion_comercial.dto.CreateUpdateProductoDTO;
import com.trabajo_practico.gestion_comercial.dto.ProductoDTO;
import com.trabajo_practico.gestion_comercial.model.Producto;
import com.trabajo_practico.gestion_comercial.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public ProductoDTO crearProducto(CreateUpdateProductoDTO createUpdateProductoDTO) {
        var producto = generateProducto(createUpdateProductoDTO);
        return new ProductoDTO(productoRepository.save(producto));
    }

    public List<ProductoDTO> obtenerTodos() {
        return productoRepository.findAll().stream().map(ProductoDTO::new).collect(Collectors.toList());
    }

    public Optional<ProductoDTO> obtenerPorId(Long id) {
        var producto = productoRepository.findById(id);
        return producto.map(ProductoDTO::new);
    }

    public ProductoDTO actualizarProducto(Long id, CreateUpdateProductoDTO updateProductoDTO) {
        var producto = generateProducto(updateProductoDTO);
        if (productoRepository.existsById(id)) {
            producto.setId(id);
            return new ProductoDTO(productoRepository.save(producto));
        }
        return null;
    }

    public boolean eliminarProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
    private Producto generateProducto(CreateUpdateProductoDTO updateProductoDTO) {
        var producto = new Producto();
        producto.setNombre(updateProductoDTO.getNombre());
        producto.setPrecio(updateProductoDTO.getPrecio());
        producto.setDescripcion(updateProductoDTO.getDescripcion());
        return producto;
    }
}

