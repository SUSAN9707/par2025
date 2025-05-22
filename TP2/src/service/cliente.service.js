
import api from '@/axios'

export function getClientes() {
    return api.get('/clientes')
}
