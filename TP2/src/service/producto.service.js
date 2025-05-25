// src/services/productoService.js
import api from '@/axios'

// Obtener todos los productos
export function getProductos() {
    return api.get('/productos')
}

// Crear un nuevo producto
export function crearProducto(producto) {
    return api.post('/productos', producto)
}

// Obtener producto por ID
export function getProductoPorId(id) {
    return api.get(`/productos/${id}`)
}

// Actualizar producto
export function actualizarProducto(id, productoActualizado) {
    return api.put(`/productos/${id}`, productoActualizado)
}

// Eliminar producto
export function eliminarProducto(id) {
    return api.delete(`/productos/${id}`)
}
