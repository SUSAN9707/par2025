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
export function getReporteCompras(fechaInicio, fechaFin) {
    return api.get(`/reportes/compras`, {
        params: {
            inicio: fechaInicio.toISOString(),
            fin: fechaFin.toISOString()
        }
    });
}

export function getReporteVentas(fechaInicio, fechaFin) {
    return api.get(`/reportes/ventas`, {
        params: {
            inicio: fechaInicio.toISOString(),
            fin: fechaFin.toISOString()
        }
    });
}

export function getProductosMasVendidos(fechaInicio, fechaFin) {
    return api.get(`/reportes/productos-mas-vendidos`, {
        params: {
            inicio: fechaInicio.toISOString(),
            fin: fechaFin.toISOString()
        }
    });
}
