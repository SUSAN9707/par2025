<script setup>
import { ref, onMounted, computed, watch, watchEffect } from 'vue'
import { useToast } from 'primevue/usetoast'
import {
    getCompras,
    crearCompra,
    actualizarCompra,
    eliminarCompra
} from '@/service/compra.service'
import { getProductos } from '@/service/producto.service'

const dt = ref()
const submitted = ref(false)
const compras = ref([])
const productos = ref([])
const productoSeleccionado = ref(null)

const compra = ref({
    id: null,
    productoId: '',
    cantidad: 1,
    montoTotal: 0
})

const toast = useToast()
const compraDialog = ref(false)
const deleteCompraDialog = ref(false)

async function cargarCompras() {
    try {
        const response = await getCompras()
        compras.value = response.data.data
        toast.add({
            severity: 'success',
            summary: 'Éxito',
            detail: response.data.mensaje || 'Compras cargadas',
            life: 3000
        })
    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.mensaje || 'Error al cargar compras',
            life: 3000
        })
    }
}

async function cargarProductos() {
    try {
        const response = await getProductos()
        productos.value = response.data.data
    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.mensaje || 'Error al cargar productos',
            life: 3000
        })
        compraDialog.value = false
    }
}

onMounted(() => {
    cargarCompras()
    cargarProductos()
})

function openNew() {
    compra.value = {
        id: null,
        productoId: '',
        cantidad: 1,
        montoTotal: 0
    }
    productoSeleccionado.value = null
    submitted.value = false
    compraDialog.value = true
}

function editCompra(comp) {
    compra.value = { ...comp }
    productoSeleccionado.value = productos.value.find(p => p.id === comp.productoId) || null
    compraDialog.value = true
}

function confirmDeleteCompra(comp) {
    compra.value = { ...comp }
    deleteCompraDialog.value = true
}

function deleteCompra() {
    eliminarCompra(compra.value.id)
        .then(response => {
            deleteCompraDialog.value = false
            toast.add({
                severity: 'success',
                summary: 'Éxito',
                detail: response.data.mensaje,
                life: 3000
            })
            cargarCompras()
            cargarProductos()
        })
        .catch(error => {
            toast.add({
                severity: 'error',
                summary: 'Error',
                detail: error.response?.data?.mensaje || 'Error al eliminar compra',
                life: 3000
            })
        })
}

const isFormInvalid = computed(() => {
    return compra.value.productoId === '' || !compra.value.cantidad
})

function saveCompra() {
    submitted.value = true

    if (!isFormInvalid.value) {
        if (productoSeleccionado.value && compra.value.cantidad) {

            const montoTotalCalculado = productoSeleccionado.value.precio * compra.value.cantidad

            const compraParaGuardar = {
                productoId: compra.value.productoId,
                cantidad: compra.value.cantidad,
                montoTotal: montoTotalCalculado
            }

            const accion = compra.value.id
                ? actualizarCompra(compra.value.id, compraParaGuardar)
                : crearCompra(compraParaGuardar)

            accion.then(response => {
                toast.add({
                    severity: 'success',
                    summary: 'Éxito',
                    detail: response.data.mensaje,
                    life: 3000
                })
                cargarCompras()
                cargarProductos()
                compraDialog.value = false
                compra.value = { id: null, productoId: '', cantidad: 1, montoTotal: 0 }
            }).catch(error => {
                toast.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: error.response?.data?.mensaje || 'Error al guardar compra',
                    life: 3000
                })
            })
        }
    }
}


watchEffect(() => {
    if (productoSeleccionado.value && compra.value.cantidad) {
        compra.value.montoTotal = productoSeleccionado.value.precio * compra.value.cantidad
    } else {
        compra.value.montoTotal = 0
    }
})

watch(productoSeleccionado, nuevoProducto => {
    compra.value.productoId = nuevoProducto?.id || ''
})

function formatPrecio(valor) {
    if (!valor) return 'Desconocido'
    return new Intl.NumberFormat('es-PY', {
        style: 'currency',
        currency: 'PYG',
        minimumFractionDigits: 0
    }).format(valor)
}
function formatFecha(fechaISO) {
    if (!fechaISO) return 'Desconocido'

    const fecha = new Date(fechaISO)
    const dia = String(fecha.getDate()).padStart(2, '0')
    const mes = String(fecha.getMonth() + 1).padStart(2, '0')
    const anio = fecha.getFullYear()

    const horas = String(fecha.getHours()).padStart(2, '0')
    const minutos = String(fecha.getMinutes()).padStart(2, '0')

    return `${dia}/${mes}/${anio} ${horas}:${minutos}`
}

</script>



<template>
    <div>
        <div class="card">
            <Toolbar class="mb-6">

                <template #end>
                    <Button label="Exportar" icon="pi pi-upload" severity="secondary" @click="dt.exportCSV()" />
                </template>
            </Toolbar>

            <DataTable ref="dt" :value="compras" dataKey="id">
                <Column field="id" header="ID" sortable />
                <Column header="Producto" sortable>
                    <template #body="slotProps">
                        {{ productos.find(p => p.id === slotProps.data.productoId)?.nombre || 'Desconocido' }}
                    </template>
                </Column>
                <Column field="cantidad" header="Cantidad" sortable />
                <Column field="montoTotal" header="Monto Total" sortable>
                    <template #body="slotProps">
                         {{ formatPrecio(slotProps.data.totalPorArticulo) }}
                    </template>
                </Column>
                <Column field="fecha" header="Fecha" sortable style="min-width: 14rem">
                    <template #body="slotProps">
                        {{ formatFecha(slotProps.data.fecha) }}
                    </template>
                </Column>


            </DataTable>
        </div>

        <!-- Diálogo de edición -->
        <Dialog v-model:visible="compraDialog" header="Compra" modal :closable="false" :style="{ width: '450px' }">
            <div class="p-fluid">
                <div class="field">
                    <label for="idProducto">Nombre Producto:</label>
                    <Dropdown
                        id="idProducto"
                        v-model="productoSeleccionado"
                        :options="productos"
                        optionLabel="nombre"
                        :optionValue="producto => producto"
                        filter
                        filterBy="nombre"
                        placeholder="Seleccione un producto"
                        class="w-full mb-2"
                    />
                </div>
                <div class="field" v-if="productoSeleccionado">
                    <label for="cantidad">Cantidad:</label>
                    <InputNumber
                        id="cantidad"
                        v-model="compra.cantidad"
                        class="w-full"
                        :min="1"
                        :step="1"
                        showButtons
                        buttonLayout="horizontal"
                        incrementButtonIcon="pi pi-plus"
                        decrementButtonIcon="pi pi-minus"
                    />
                </div>
                <div class="field" v-if="productoSeleccionado">
                    <label for="totalPorArticulo">Monto total:</label>
                    <InputNumber
                        id="totalPorArticulo"
                        v-model="compra.totalPorArticulo"
                        class="w-full"
                        prefix="Gs. "
                        :minFractionDigits="0"
                        :maxFractionDigits="0"
                        :disabled="true"
                    />
                </div>
            </div>
            <template #footer>
                <Button label="Cancelar" icon="pi pi-times" outlined @click="compraDialog = false" />
                <Button label="Guardar" icon="pi pi-check" :disabled="isFormInvalid" @click="saveCompra" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteCompraDialog" header="Confirmar" modal :closable="false" :style="{ width: '350px' }">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem;"></i>
                <span>¿Está seguro que desea eliminar la compra con ID <b>{{ compra.id }}</b>?</span>
            </div>
            <template #footer>
                <Button label="No" icon="pi pi-times" outlined @click="deleteCompraDialog = false" />
                <Button label="Sí" icon="pi pi-check" severity="danger" @click="deleteCompra" />
            </template>
        </Dialog>
    </div>
</template>
