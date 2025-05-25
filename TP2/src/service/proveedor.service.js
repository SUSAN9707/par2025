import api from '@/axios'

// Obtener todos los proveedores
export function getProveedores() {
    return api.get('/proveedores')
}

// Crear un nuevo proveedor
export function crearProveedor(proveedor) {
    return api.post('/proveedores', proveedor)
}

// Obtener proveedor por ID
export function getProveedorPorId(id) {
    return api.get(`/proveedores/${id}`)
}

// Actualizar proveedor
export function actualizarProveedor(id, proveedorActualizado) {
    return api.put(`/proveedores/${id}`, proveedorActualizado)
}

// Eliminar proveedor
export function eliminarProveedor(id) {
    return api.delete(`/proveedores/${id}`)
}
