import AppLayout from '@/layout/AppLayout.vue'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            redirect: '/auth/login'
        },
        {
            path: '/auth/login',
            name: 'login',
            component: () => import('@/views/pages/auth/Login.vue')
        },
        {
            path: '/',
            component: AppLayout,
            children: [
                {
                    path: '/dashboard',
                    name: 'dashboard',
                    component: () => import('@/views/Dashboard.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: '/pages/productos',
                    name: 'Productos',
                    component: () => import('@/views/pages/Productos.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: '/pages/clientes',
                    name: 'Clientes',
                    component: () => import('@/views/pages/Clientes.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: '/pages/facturas',
                    name: 'Facturas',
                    component: () => import('@/views/pages/Facturas.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: '/pages/proveedores',
                    name: 'Proveedores',
                    component: () => import('@/views/pages/Proveedores.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: '/pages/reportes',
                    name: 'Reportes',
                    component: () => import('@/views/pages/Reportes.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: '/pages/ventas',
                    name: 'Ventas',
                    component: () => import('@/views/pages/Ventas.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: '/pages/compras',
                    name: 'Compras',
                    component: () => import('@/views/pages/Compras.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: '/reportes/inventario-productos',
                    name: 'Inventario de Productos',
                    component: () => import('@/views/reportes/ReporteInventario.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: 'reportes/movimientos-financieros',
                    name: 'Movimientos Financieros',
                    component: () => import('@/views/reportes/ReporteFinanciero.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: 'reportes/compras',
                    name: 'Reportes Compras',
                    component: () => import('@/views/reportes/ReporteCompras.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: 'reportes/ventas',
                    name: 'Reportes Ventas',
                    component: () => import('@/views/reportes/ReporteVentas.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: 'reportes/productos-mas-vendidos',
                    name: 'Productos mas vendidos',
                    component: () => import('@/views/reportes/ProductosMasVendidos.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: 'reportes/top-proveedores',
                    name: 'Top 15 proveedores',
                    component: () => import('@/views/reportes/TopProveedores.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: 'reportes/top-clientes',
                    name: 'Top 15 clientes',
                    component: () => import('@/views/reportes/TopClientes.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: 'reportes/utilidades',
                    name: 'Reporte de utilidades',
                    component: () => import('@/views/reportes/ReporteUtilidades.vue'),
                    meta: { requiresAuth: true }
                }
            ]
        },
        {
            path: '/:pathMatch(.*)*',
            name: 'NotFound',
            component: () => import('@/views/pages/NotFound.vue')
        }

    ]
})

// GUARD DE AUTENTICACIÓN
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (to.meta.requiresAuth && !token) {
        next('/auth/login')
    } else {
        next()
    }
})

export default router
