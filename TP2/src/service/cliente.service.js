
import api from '@/axios'

export function getClientes() {
    return api.get('/clientes')
}

export function crearCliente(cliente) {
    return api.post('/clientes', cliente)
}

// Obtener cliente por ID
export function getClientePorId(id) {
    return api.get(`/clientes/${id}`)
}

// Actualizar cliente
export function actualizarCliente(id, clienteActualizado) {
    return api.put(`/clientes/${id}`, clienteActualizado)
}

// Eliminar cliente
export function eliminarCliente(id) {
    return api.delete(`/clientes/${id}`)
}
