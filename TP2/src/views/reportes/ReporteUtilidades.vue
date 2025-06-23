<script setup>
import { ref, onMounted } from 'vue'
import { useToast } from 'primevue/usetoast'
import { getReporteUtilidad } from '@/service/reportes.service'
import jsPDF from 'jspdf'
import autoTable from 'jspdf-autotable'
import Chart from 'primevue/chart'

const toast = useToast()
const now = new Date()
const fechaInicio = ref(new Date(now.getFullYear(), now.getMonth(), 1))
const fechaFin = ref(new Date(now.getFullYear(), now.getMonth() + 1, 0))
const datos = ref({ totalVentas: 0, totalCompras: 0, utilidad: 0 })
const chartData = ref()
const chartOptions = ref()

onMounted(() => {
    cargarDatos()
})

function formatPrecio(valor) {
    return new Intl.NumberFormat('es-PY', {
        style: 'currency',
        currency: 'PYG',
        minimumFractionDigits: 0
    }).format(valor)
}

async function cargarDatos() {
    try {
        const res = await getReporteUtilidad(fechaInicio.value, fechaFin.value)
        datos.value = res.data.data

        const utilidadColor = datos.value.utilidad < 0 ? '#f87171' : '#10b981' // rojo o verde
        const comprasColor = '#60a5fa' // azul
        const ventasColor = '#fde047' // amarillo suave

        chartData.value = {
            labels: ['Ventas', 'Compras', 'Utilidad'],
            datasets: [
                {
                    label: 'Monto',
                    backgroundColor: [ventasColor, comprasColor, utilidadColor],
                    data: [datos.value.totalVentas, datos.value.totalCompras, datos.value.utilidad]
                }
            ]
        }

        chartOptions.value = {
            responsive: true,
            plugins: {
                legend: { display: false }
            },
            scales: {
                x: { ticks: { color: '#374151' } },
                y: { ticks: { color: '#374151' } }
            }
        }

        toast.add({ severity: 'success', summary: 'Éxito', detail: res.data.mensaje, life: 3000 })
    } catch (e) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'No se pudo cargar el reporte', life: 3000 })
    }
}

function exportarPDF() {
    const doc = new jsPDF()
    doc.setFontSize(16)
    doc.setFont('helvetica', 'bold')
    doc.text('Reporte de Utilidades', 14, 15)

    const rows = [
        ['Total Ventas', formatPrecio(datos.value.totalVentas)],
        ['Total Compras', formatPrecio(datos.value.totalCompras)],
        ['Utilidad', formatPrecio(datos.value.utilidad)]
    ]

    autoTable(doc, {
        head: [['Concepto', 'Monto']],
        body: rows,
        startY: 25,
        styles: { fontSize: 10 },
        headStyles: { fillColor: [16, 185, 129], textColor: 255 }
    })

    const chartCanvas = document.querySelector('canvas')
    if (chartCanvas) {
        const chartImage = chartCanvas.toDataURL('image/png', 1.0)
        doc.addPage()
        doc.setFontSize(14)
        doc.text('Gráfico de Utilidades', 14, 20)
        doc.addImage(chartImage, 'PNG', 15, 30, 180, 100)
    }

    doc.save('reporte_utilidades.pdf')
}
</script>

<template>
    <div class="card">
        <Toolbar class="mb-4">
            <template #start>
                <span class="text-xl font-bold">Reporte de Utilidades</span>
            </template>
            <template #end>
                <Calendar v-model="fechaInicio" showIcon class="mr-2" />
                <Calendar v-model="fechaFin" showIcon class="mr-3" />
                <Button label="Buscar" icon="pi pi-search" class="mr-2" @click="cargarDatos" />
                <Button
                    label="Exportar PDF"
                    icon="pi pi-upload"
                    severity="secondary"
                    @click="exportarPDF"
                />
            </template>
        </Toolbar>

        <div class="flex gap-4 justify-between flex-wrap p-4">
            <div class="flex-1 text-center bg-white border border-gray-300 p-4 rounded-lg">
                <h3 class="text-yellow-800 text-lg font-semibold">Ventas</h3>
                <p class="text-2xl font-bold">{{ formatPrecio(datos.totalVentas) }}</p>
            </div>
            <div class="flex-1 text-center bg-white border border-gray-300 p-4 rounded-lg">
                <h3 class="text-blue-700 text-lg font-semibold">Compras</h3>
                <p class="text-2xl font-bold">{{ formatPrecio(datos.totalCompras) }}</p>
            </div>
            <div
                class="flex-1 text-center bg-white border border-gray-300 p-4 rounded-lg"
                :class="datos.utilidad < 0 ? 'text-red-700' : 'text-green-700'"
            >
                <h3 class="text-lg font-semibold">Utilidad</h3>
                <p class="text-2xl font-bold">{{ formatPrecio(datos.utilidad) }}</p>
            </div>
        </div>

        <Chart type="bar" :data="chartData" :options="chartOptions" />
    </div>
</template>
