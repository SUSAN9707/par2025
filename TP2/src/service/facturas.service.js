// src/services/facturaService.js
import api from '@/axios'

// Obtener todas las facturas
export function getFacturas() {
    return api.get('/facturas')
}

// Crear una nueva factura
export function crearFactura(factura) {
    return api.post('/facturas', factura)
}

// Obtener factura por ID
export function getFacturaPorId(id) {
    return api.get(`/facturas/${id}`)
}

// Actualizar factura
export function actualizarFactura(id, facturaActualizada) {
    return api.put(`/facturas/${id}`, facturaActualizada)
}

// Eliminar factura
export function eliminarFactura(id) {
    return api.delete(`/facturas/${id}`)
}

export function anularFactura(id) {
    return api.put(`/facturas/${id}/anular`)
}
