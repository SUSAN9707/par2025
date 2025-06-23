<script setup>
import {onMounted, ref} from 'vue'
import { useToast } from 'primevue/usetoast'
import { getReporteCompras } from '@/service/reportes.service'
import jsPDF from 'jspdf'
import autoTable from 'jspdf-autotable'

const toast = useToast()

const reporte = ref([])
const now = new Date();
const fechaInicio = ref(new Date(now.getFullYear(), now.getMonth(), 1, 0, 0, 0)); // 1er día del mes actual
const fechaFin = ref(new Date(now.getFullYear(), now.getMonth() + 1, 0, 23, 59, 59)); // último día del mes actual


function formatPrecio(valor) {
    return new Intl.NumberFormat('es-PY', {
        style: 'currency',
        currency: 'PYG',
        minimumFractionDigits: 0
    }).format(valor)
}

onMounted(() => {

    cargarReporte()
})
async function cargarReporte() {
    try {
        if (!fechaInicio.value || !fechaFin.value) {
            toast.add({
                severity: 'warn',
                summary: 'Fechas requeridas',
                detail: 'Seleccione fecha de inicio y fin',
                life: 3000
            })
            return
        }

        const response = await getReporteCompras(
            fechaInicio.value,
            fechaFin.value
        )
        reporte.value = response.data.data

        toast.add({
            severity: 'success',
            summary: 'Éxito',
            detail: response.data.mensaje || 'Reporte cargado correctamente',
            life: 3000
        })
    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.mensaje || 'Error al cargar el reporte',
            life: 3000
        })
    }
}

function exportarPDF() {
    const doc = new jsPDF()

    doc.setFontSize(16)
    doc.setFont('helvetica', 'bold')
    doc.text(`Reporte de Compras`, 14, 15)

    const columns = [
        { header: 'Producto', dataKey: 'producto' },
        { header: 'Cantidad', dataKey: 'cantidad' },
        { header: 'Precio Unitario', dataKey: 'precioUnitario' },
        { header: 'Total', dataKey: 'total' },
        { header: 'Fecha', dataKey: 'fecha' }
    ]

    const rows = reporte.value.map(r => ({
        producto: r.producto,
        cantidad: r.cantidad,
        precioUnitario: formatPrecio(r.precioUnitario),
        total: formatPrecio(r.total),
        fecha: new Date(r.fecha).toLocaleDateString()
    }))

    autoTable(doc, {
        columns,
        body: rows,
        startY: 25,
        styles: { fontSize: 10 },
        headStyles: {
            fillColor: [16, 185, 129],
            textColor: 255,
            fontStyle: 'bold'
        }
    })

    doc.save('reporte_compras.pdf')
}
</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-4">
                <template #start>
                    <span class="text-xl font-bold">Reporte de Compras</span>
                </template>
                <template #end>
                    <Calendar v-model="fechaInicio" showIcon placeholder="Inicio" class="mr-2" />
                    <Calendar v-model="fechaFin" showIcon placeholder="Fin" class="mr-3" />
                    <Button label="Buscar" icon="pi pi-search" class="mr-2" @click="cargarReporte" />
                    <Button label="Exportar PDF" icon="pi pi-upload" severity="secondary" @click="exportarPDF" />
                </template>
            </Toolbar>

            <div class="p-4">
                <template v-if="reporte.length">
                    <DataTable :value="reporte" responsiveLayout="scroll">
                        <Column field="producto" header="Producto" />
                        <Column field="cantidad" header="Cantidad" />
                        <Column field="precioUnitario" header="Precio Unitario">
                            <template #body="{ data }">
                                {{ formatPrecio(data.precioUnitario) }}
                            </template>
                        </Column>
                        <Column field="total" header="Total">
                            <template #body="{ data }">
                                {{ formatPrecio(data.total) }}
                            </template>
                        </Column>
                        <Column field="fecha" header="Fecha">
                            <template #body="{ data }">
                                {{ new Date(data.fecha).toLocaleDateString() }}
                            </template>
                        </Column>
                    </DataTable>
                </template>
                <template v-else>
                    <p class="text-center italic text-gray-500 mt-4">
                        No hay resultados para mostrar en este rango de fechas.
                    </p>
                </template>
            </div>
        </div>
    </div>
</template>
