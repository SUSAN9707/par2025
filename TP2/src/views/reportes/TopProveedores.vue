<script setup>
import { ref, onMounted } from 'vue'
import { useToast } from 'primevue/usetoast'
import { getTopProveedores } from '@/service/reportes.service'
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
        const res = await getTopProveedores(fechaInicio.value, fechaFin.value)
        rawData.value = res.data.data

        chartData.value = {
            labels: rawData.value.map(p => p.nombre),
            datasets: [
                {
                    label: 'Total Comprado',
                    backgroundColor: getComputedStyle(document.documentElement).getPropertyValue('--primary-color') || '#10b981',
                    data: rawData.value.map(p => p.total)
                }
            ]
        }

        chartOptions.value = {
            indexAxis: 'y', // HORIZONTAL
            responsive: true,
            plugins: {
                legend: {
                    labels: {
                        color: '#374151'
                    }
                }
            },
            scales: {
                x: {
                    ticks: { color: '#374151' }
                },
                y: {
                    ticks: { color: '#374151' }
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
    doc.text('Top 15 Proveedores', 14, 15)

    const rows = rawData.value.map(r => [r.nombre, r.total.toLocaleString()])

    autoTable(doc, {
        head: [['Proveedor', 'Total Comprado']],
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
        doc.text('Gráfico de Top Proveedores', 14, 20)
        doc.addImage(chartImage, 'PNG', 15, 30, 180, 100)
    }

    doc.save('top_proveedores.pdf')
}
</script>

<template>
    <div class="card">
        <Toolbar class="mb-4">
            <template #start>
                <span class="text-xl font-bold">Top 15 Proveedores</span>
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
                    @click="exportarPDF"
                />
            </template>
        </Toolbar>

        <div v-if="rawData.length > 0">
            <Chart type="bar" :data="chartData" :options="chartOptions" />
            <DataTable :value="rawData" responsiveLayout="scroll">
                <Column field="nombre" header="Proveedor" />
                <Column field="total" header="Total Comprado" />
            </DataTable>
        </div>

        <div v-else class="text-center italic text-gray-500 py-6">
            No se encontraron proveedores con compras en este rango de fechas.
        </div>
    </div>
</template>
