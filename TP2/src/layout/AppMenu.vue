<script setup>
import { ref } from 'vue';

import AppMenuItem from './AppMenuItem.vue';

const model = ref([
    /*{
        label: 'Inicio',
        items: [{ label: 'Dashboard', icon: 'pi pi-fw pi-home', to: '/' }]
    },*/
    {
        label: 'Administración',
        icon: 'pi pi-fw pi-briefcase',
        to: '/pages',
        items: [
            {
                label: 'Productos',
                icon: 'pi pi-fw pi-box',
                to: '/pages/productos'
            },
            {
                label: 'Clientes',
                icon: 'pi pi-fw pi-users',
                to: '/pages/clientes'
            },
            {
                label: 'Proveedores',
                icon: 'pi pi-truck',
                to: '/pages/proveedores'
            },
           {
                label: 'Facturas',
                icon: 'pi pi-dollar',
                to: '/pages/facturas'
            },
            {
                label: 'Ventas',
                icon: 'pi pi-wallet',
                to: '/pages/ventas'
            },
            {
                label: 'Compras',
                icon: 'pi pi-shopping-cart',
                to: '/pages/compras'
            }
        ]
    }, {
        label: 'Reportes',
        to: '/reportes',
        items: [
            { label: 'Inventario de productos', icon: 'pi pi-tags', to: '/reportes/inventario-productos' },
            { label: 'Movimientos financieros', icon: 'pi pi-wallet', to: '/reportes/movimientos-financieros' },
            { label: 'Reporte Compras', icon: 'pi pi-shopping-cart', to: '/reportes/compras' },
            { label: 'Reporte Ventas', icon: 'pi pi-dollar', to: '/reportes/ventas' },
            { label: 'Productos más Vendidos', icon: 'pi pi-star', to: '/reportes/productos-mas-vendidos' },
            { label: 'Top 15 Proveedores', icon: 'pi pi-truck', to: '/reportes/top-proveedores' },
            { label: 'Top 15 Clientes', icon: 'pi pi-users', to: '/reportes/top-clientes' },
            { label: 'Reporte de utilidades', icon: 'pi pi-chart-bar', to: '/reportes/utilidades' }
        ]
    }
]);
</script>

<template>
    <ul class="layout-menu">
        <template v-for="(item, i) in model" :key="item">
            <app-menu-item v-if="!item.separator" :item="item" :index="i"></app-menu-item>
            <li v-if="item.separator" class="menu-separator"></li>
        </template>
    </ul>
</template>

<style lang="scss" scoped></style>
