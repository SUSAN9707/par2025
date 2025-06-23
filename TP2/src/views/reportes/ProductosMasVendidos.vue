<script setup>
import { ref, onMounted } from 'vue'
import { useToast } from 'primevue/usetoast'
import { getProductosMasVendidos } from '@/service/reportes.service'
import Chart from 'primevue/chart'
import jsPDF from 'jspdf'
import autoTable from 'jspdf-autotable'


const toast = useToast()
const chartData = ref()
const chartOptions = ref()
const rawData = ref([])

const now = new Date()
const fechaInicio = ref(new Date(now.getFullYear(), now.getMonth(), 1))
const fechaFin = ref(new Date(now.getFullYear(), now.getMonth() + 1, 0))



onMounted(() => {
    cargarDatos()
})

async function cargarDatos() {
    try {
        const res = await getProductosMasVendidos(fechaInicio.value, fechaFin.value)
        rawData.value = res.data.data

        chartData.value = {
            labels: rawData.value.map(p => p.nombreProducto),
            datasets: [
                {
                    label: 'Cantidad Vendida',
                    backgroundColor: '#10b981', // color del sistema
                    data: rawData.value.map(p => p.cantidadVendida)
                }
            ]
        }

        chartOptions.value = {
            responsive: true,
            plugins: {
                legend: {
                    labels: {
                        color: '#374151' // texto gris oscuro
                    }
                }
            },
            scales: {
                x: {
                    ticks: {
                        color: '#374151'
                    }
                },
                y: {
                    ticks: {
                        color: '#374151'
                    }
                }
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
    doc.text('Reporte de Productos Más Vendidos', 14, 15)


    const rows = rawData.value.map(r => [
        r.nombreProducto,
        r.cantidadVendida
    ])

    autoTable(doc, {
        head: [['Producto', 'Cantidad Vendida']],
        body: rows,
        startY: 25,
        styles: { fontSize: 10 },
        headStyles: { fillColor: [16, 185, 129], textColor: 255 }
    })

    // Agrega nueva página con gráfico
    const chartCanvas = document.querySelector('canvas')
    if (chartCanvas) {
        const chartImage = chartCanvas.toDataURL('image/png', 1.0)
        doc.addPage()
        doc.setFontSize(14)
        doc.text('Gráfico de Productos Más Vendidos', 14, 20)
        doc.addImage(chartImage, 'PNG', 15, 30, 180, 100)
    }

    doc.save('productos_mas_vendidos.pdf')
}
</script>

<template>
    <div class="card">
        <Toolbar class="mb-4">
            <template #start>
                <span class="text-xl font-bold">Productos Más Vendidos</span>
            </template>
            <template #end>
                <Calendar v-model="fechaInicio" showIcon class="mr-2" />
                <Calendar v-model="fechaFin" showIcon class="mr-3" />
                <Button label="Buscar" icon="pi pi-search" class="mr-2" @click="cargarDatos" />
                <Button
                    label="Exportar PDF"
                    icon="pi pi-upload"
                    severity="secondary"
                    :disabled="rawData.length === 0"
                    class="mr-2"
                    @click="exportarPDF"
                />
            </template>
        </Toolbar>

        <div v-if="rawData.length > 0">
            <Chart type="bar" :data="chartData" :options="chartOptions" />
            <DataTable :value="rawData" responsiveLayout="scroll">
                <Column field="nombreProducto" header="Producto" />
                <Column field="cantidadVendida" header="Cantidad Vendida" />
            </DataTable>
        </div>

        <div v-else class="text-center italic text-gray-500 py-6">
            No se encontraron productos vendidos en este rango de fechas.
        </div>
    </div>
</template>

