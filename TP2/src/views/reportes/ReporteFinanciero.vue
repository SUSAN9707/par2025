<script setup>
import { ref, onMounted } from 'vue'
import { useToast } from 'primevue/usetoast'
import { getReporteMovimientos } from '@/service/reportes.service'

import { jsPDF } from 'jspdf'
import autoTable from 'jspdf-autotable'

const toast = useToast()

const mes = ref(new Date().getMonth() + 1)
const anho = ref(new Date().getFullYear())
const fecha = ref(new Date())

function onFechaChange() {
    mes.value = fecha.value.getMonth() + 1
    anho.value = fecha.value.getFullYear()
    cargarReporte()
}
const reporte = ref({
    cantidadVentas: 0,
    totalVentas: 0,
    cantidadCompras: 0,
    totalCompras: 0
})

async function cargarReporte() {
    try {
        const response = await getReporteMovimientos(mes.value, anho.value)
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

function formatPrecio(valor) {
    return new Intl.NumberFormat('es-PY', {
        style: 'currency',
        currency: 'PYG',
        minimumFractionDigits: 0
    }).format(valor);
}

function exportarPDF() {
    const doc = new jsPDF()

    const meses = [
        'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
        'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
    ]
    const mesNombre = meses[mes.value - 1] || 'Mes inválido'

    // Título con mes y año
    doc.setFontSize(16)
    doc.setFont('helvetica', 'bold')
    doc.text(`Reporte Financiero - ${mesNombre} ${anho.value}`, 14, 15)

    const columns = [
        { header: 'Tipo', dataKey: 'tipo' },
        { header: 'Cantidad', dataKey: 'cantidad' },
        { header: 'Total', dataKey: 'totalFormateado' }
    ]

    const rows = [
        {
            tipo: 'Ventas',
            cantidad: reporte.value.cantidadVentas,
            totalFormateado: formatPrecio(reporte.value.totalVentas)
        },
        {
            tipo: 'Compras',
            cantidad: reporte.value.cantidadCompras,
            totalFormateado: formatPrecio(reporte.value.totalCompras)
        }
    ]

    autoTable(doc, {
        columns,
        body: rows,
        startY: 25,
        styles: { fontSize: 10 },
        headStyles: {
            fillColor: [16, 185, 129], // #10b981
            textColor: 255,
            fontStyle: 'bold'
        }
    })

    doc.save('reporte_financiero.pdf')
}


onMounted(() => {
    cargarReporte()
})
</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-4">
                <template #start>
                    <span class="text-xl font-bold">Reporte financiero</span>
                </template>
                <template #end>
                    <Calendar
                        v-model="fecha"
                        view="month"
                        dateFormat="mm/yy"
                        showIcon
                        class="mr-3"
                        @update:modelValue="onFechaChange"
                    />
                    <Button label="Exportar PDF" icon="pi pi-upload" severity="secondary" @click="exportarPDF" />
                </template>
            </Toolbar>

            <div class="p-4">
                <p><strong>Mes:</strong> {{ mes }} / <strong>Año:</strong> {{ anho }}</p>
                <p><strong>Cantidad de Ventas:</strong> {{ reporte.cantidadVentas }}</p>
                <p><strong>Total de Ventas:</strong> {{ formatPrecio(reporte.totalVentas) }}</p>
                <p><strong>Cantidad de Compras:</strong> {{ reporte.cantidadCompras }}</p>
                <p><strong>Total de Compras:</strong> {{ formatPrecio(reporte.totalCompras) }}</p>
            </div>
        </div>
    </div>
</template>

