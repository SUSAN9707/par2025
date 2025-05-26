import api from '@/axios'

// Obtener reporte de movimientos financieros (por mes y año)
export function getReporteMovimientos(mes, anio) {
    return api.get('/reportes/movimientos', {
        params: { mes, anio }
    })
}

// Obtener reporte de inventario de productos
export function getReporteInventario() {
    return api.get('/reportes/inventario')
}
