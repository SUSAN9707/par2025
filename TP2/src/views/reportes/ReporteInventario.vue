<script setup>
import { ref, onMounted } from 'vue'
import { useToast } from 'primevue/usetoast'
import { getReporteInventario } from '@/service/reportes.service'

import { jsPDF } from "jspdf";
import autoTable from "jspdf-autotable";


const dt = ref()
const toast = useToast()

const inventario = ref([])
const totalValor = ref(0)

async function cargarInventario() {
    try {
        const response = await getReporteInventario()
        inventario.value = response.data.data.productos
        totalValor.value = response.data.data.totalInventario

        toast.add({
            severity: 'success',
            summary: 'Éxito',
            detail: response.data.mensaje || 'Inventario cargado correctamente',
            life: 3000
        })
    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.mensaje || 'Error al cargar el inventario',
            life: 3000
        })
    }
}

function formatPrecio(valor) {
    return new Intl.NumberFormat('es-PY', {
        style: 'currency',
        currency: 'PYG',
        minimumFractionDigits: 0
    }).format(valor);
}

onMounted(() => {
    cargarInventario()
})

function exportarPDF(productos) {
    const doc = new jsPDF()

    //  TÍTULO
    doc.setFontSize(16)
    doc.setFont("helvetica", "bold")
    doc.text("Reporte de Inventario", 14, 15)

    const columns = [
        { header: 'Nombre', dataKey: 'nombre' },
        { header: 'Descripción', dataKey: 'descripcion' },
        { header: 'Precio Unitario', dataKey: 'precioFormateado' },
        { header: 'Stock', dataKey: 'stock' },
        { header: 'Stock Mínimo', dataKey: 'stockMinimo' },
        { header: 'Estado Stock', dataKey: 'estadoStock' },
        { header: 'Valor Total', dataKey: 'valorTotalFormateado' }
    ]

    const rows = productos.map(p => ({
        nombre: p.nombre,
        descripcion: p.descripcion,
        precioFormateado: formatPrecio(p.precio),
        stock: p.stock,
        stockMinimo: p.stockMinimo,
        estadoStock: p.estadoStock,
        valorTotalFormateado: formatPrecio(p.precio * p.stock)
    }))

    autoTable(doc, {
        columns,
        body: rows,
        startY: 25, // Más abajo por el título
        styles: {
            fontSize: 8
        },
        headStyles: {
            fillColor: [16, 185, 129], // #10b981 en RGB
            textColor: 255,            // blanco
            fontStyle: 'bold'
        }
    })

    const finalY = doc.lastAutoTable?.finalY || 25
    doc.setFontSize(11)
    doc.setFont("helvetica", "normal")
    doc.text(`Total en inventario: ${formatPrecio(totalValor.value)}`, 14, finalY + 10)

    doc.save('reporte_inventario.pdf')
}

</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-6">
                <template #start>
                    <span class="text-xl font-bold">Inventario de productos</span>
                </template>
                <template #end>
                    <Button label="Exportar CSV" class="mx-3" icon="pi pi-upload" severity="secondary" @click="dt.exportCSV()" />
                    <Button label="Exportar PDF" icon="pi pi-upload" severity="secondary" @click="exportarPDF(inventario)" />
                </template>
            </Toolbar>

            <DataTable ref="dt" :value="inventario" dataKey="nombre" stripedRows>
                <Column field="nombre" header="Nombre" sortable style="min-width: 12rem"></Column>
                <Column field="descripcion" header="Descripción" sortable style="min-width: 18rem"></Column>
                <Column header="Precio Unitario" style="min-width: 12rem">
                    <template #body="slotProps">
                        {{ formatPrecio(slotProps.data.precio) }}
                    </template>
                </Column>
                <Column field="stock" header="Stock" sortable style="min-width: 8rem"></Column>
                <Column field="stockMinimo" header="Stock Mínimo" sortable style="min-width: 8rem"></Column>
                <Column field="estadoStock" header="Estado Stock" sortable style="min-width: 10rem">
                    <template #body="slotProps">
                        <Tag
                            :value="slotProps.data.estadoStock"
                            :severity="slotProps.data.estadoStock === 'Suficiente' ? 'success' : 'danger'"
                        />
                    </template>
                </Column>

                <Column header="Valor Total" style="min-width: 12rem">
                    <template #body="slotProps">
                        {{ formatPrecio(slotProps.data.precio * slotProps.data.stock) }}
                    </template>
                </Column>
            </DataTable>

            <div class="text-right mt-4">
                <strong>Total en inventario:</strong> {{ formatPrecio(totalValor) }}
            </div>
        </div>
    </div>
</template>

<style scoped>
.text-right {
    text-align: right;
}
</style>
