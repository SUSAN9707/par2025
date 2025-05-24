// src/services/ventaService.js
import api from '@/axios'

// Obtener todas las ventas
export function getVentas() {
    return api.get('/ventas')
}

// Crear una nueva venta
export function crearVenta(venta) {
    return api.post('/ventas', venta)
}

// Obtener venta por ID
export function getVentaPorId(id) {
    return api.get(`/ventas/${id}`)
}

// Actualizar venta
export function actualizarVenta(id, ventaActualizada) {
    return api.put(`/ventas/${id}`, ventaActualizada)
}

// Eliminar venta
export function eliminarVenta(id) {
    return api.delete(`/ventas/${id}`)
}
