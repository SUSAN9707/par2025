// src/services/compraService.js
import api from '@/axios'

// Obtener todas las compras
export function getCompras() {
    return api.get('/compras')
}

// Crear una nueva compra
export function crearCompra(compra) {
    const body = {
        productoId: compra.productoId,
        cantidad: compra.cantidad,
        montoTotal:compra.montoTotal
    }
    return api.post('/compras', body)
}

// Obtener compra por ID
export function getCompraPorId(id) {
    return api.get(`/compras/${id}`)
}

// Actualizar compra
export function actualizarCompra(id, compraActualizada) {
    const body = {
        productoId: compraActualizada.productoId,
        cantidad: compraActualizada.cantidad
    }
    return api.put(`/compras/${id}`, body)
}

// Eliminar compra
export function eliminarCompra(id) {
    return api.delete(`/compras/${id}`)
}
