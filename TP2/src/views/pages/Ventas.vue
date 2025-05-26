<script setup>
import {ref, onMounted, computed, watch, watchEffect} from 'vue'
import { useToast } from 'primevue/usetoast'
import {
    getVentas,
    crearVenta,
    actualizarVenta,
    eliminarVenta
} from '@/service/venta.service'
import {getProductos} from "@/service/producto.service";

const dt = ref()
const submitted = ref(false)
const ventas = ref([])
const productos = ref([])
const productoSeleccionado = ref(null);

const venta = ref({
    id: null,
    productoId: '',
    cantidad: 1,
    montoTotal:0
})

const toast = useToast()
const ventaDialog = ref(false)
const deleteVentaDialog = ref(false)

async function cargarVentas() {
    try {
        const response = await getVentas()
        ventas.value = response.data.data

        toast.add({
            severity: 'success',
            summary: 'Éxito',
            detail: response.data.mensaje || 'Ventas cargadas',
            life: 3000
        })
    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.mensaje || 'Error al cargar ventas',
            life: 3000
        })
    }
}

onMounted(() => {
    cargarVentas()
    cargarProductos()
})

function openNew() {
    venta.value = {
        id: null,
        productoId: '',
        cantidad: 1,
        montoTotal:0
    }
    productoSeleccionado.value=null
    submitted.value = false
    ventaDialog.value = true
}
async function cargarProductos() {
    try {
        const response = await getProductos();
        productos.value = response.data.data.filter(p => p.stock !== 0); // lista de clientes
    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.mensaje || 'Error al cargar productos',
            life: 3000
        });
        ventaDialog.value=false
    }
}
function editVenta(ven) {
    venta.value = { ...ven }
    productoSeleccionado.value = productos.value.find(p => p.id === ven.productoId) || null;
    ventaDialog.value = true

}

function confirmDeleteVenta(ven) {
    venta.value = { ...ven }
    deleteVentaDialog.value = true
}

function deleteVenta() {
    eliminarVenta(venta.value.id)
        .then(response => {
            deleteVentaDialog.value = false
            toast.add({
                severity: 'success',
                summary: 'Éxito',
                detail: response.data.mensaje,
                life: 3000
            })
            cargarVentas()
            cargarProductos()
            venta.value = { id: null, productoId: '', cantidad: 1,
                montoTotal:0 }
        })
        .catch(error => {
            toast.add({
                severity: 'error',
                summary: 'Error',
                detail: error.response?.data?.mensaje || 'Error al eliminar venta',
                life: 3000
            })
        })

}

const isFormInvalid = computed(() => {
    return venta.value.productoId==='' || !venta.value.cantidad
})

function saveVenta() {
    submitted.value = true

    if (!isFormInvalid.value) {
        if (productoSeleccionado.value && venta.value.cantidad) {
            venta.value.montoTotal = productoSeleccionado.value.precio * venta.value.cantidad;
        }
        if (venta.value.id) {
            const ventaParaActualizar= {
                productoId: venta.value.productoId,
                cantidad: venta.value.cantidad,
                montoTotal:venta.value.montoTotal
            };

            actualizarVenta(venta.value.id, ventaParaActualizar)
                .then(response => {
                    toast.add({
                        severity: 'success',
                        summary: 'Éxito',
                        detail: response.data.mensaje,
                        life: 3000
                    })
                    cargarVentas()
                    cargarProductos()
                })
                .catch(error => {
                    toast.add({
                        severity: 'error',
                        summary: 'Error',
                        detail: error.response?.data?.mensaje || 'Error al actualizar venta',
                        life: 3000
                    })
                })
        } else {
            const ventaParaGuardar = {
                productoId: venta.value.productoId,
                cantidad: venta.value.cantidad,
                montoTotal:venta.value.montoTotal
            };
            console.log(ventaParaGuardar)
            crearVenta(ventaParaGuardar)
                .then(response => {
                    toast.add({
                        severity: 'success',
                        summary: 'Éxito',
                        detail: response.data.mensaje,
                        life: 3000
                    })
                    cargarVentas()
                    cargarProductos()
                })
                .catch(error => {
                    toast.add({
                        severity: 'error',
                        summary: 'Error',
                        detail: error.response?.data?.mensaje || 'Error al crear venta',
                        life: 3000
                    })
                })
        }
        ventaDialog.value = false
        venta.value = { id: null, productoId: '', cantidad: 1,
            montoTotal:0 }
    }
}
watchEffect(() => {
    if (productoSeleccionado.value && venta.value.cantidad) {
        venta.value.montoTotal = productoSeleccionado.value.precio * venta.value.cantidad;
    } else {
        venta.value.montoTotal = 0;
    }
})

watch(productoSeleccionado, (nuevoProducto) => {
    venta.value.productoId = nuevoProducto?.id || ''
})

function formatPrecio(valor) {
    if(!valor){
        return 'Desconocido'
    }
    return new Intl.NumberFormat('es-PY', {
        style: 'currency',
        currency: 'PYG',
        minimumFractionDigits: 0
    }).format(valor);
}

</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-6">
                <template #start>
                    <Button label="Nueva venta" icon="pi pi-plus" severity="secondary" class="mr-2" @click="openNew" />
                </template>
                <template #end>
                    <Button label="Exportar" icon="pi pi-upload" severity="secondary" @click="dt.exportCSV()" />
                </template>
            </Toolbar>

            <DataTable ref="dt" :value="ventas" dataKey="id">
                <Column field="id" header="ID" sortable style="min-width: 6rem" />
                <Column header="Producto" sortable style="min-width: 12rem">
                    <template #body="slotProps">
                        {{
                            productos.find(p => p.id === slotProps.data.productoId)?.nombre || 'Desconocido'
                        }}
                    </template>
                </Column>

                <Column field="cantidad" header="Cantidad" sortable style="min-width: 10rem" />
                <Column field="montoTotal" header="montoTotal Total" sortable style="min-width: 12rem">
                    <template #body="slotProps">
                        Gs. {{ formatPrecio(slotProps.data.montoTotal) }}
                    </template>
                </Column>

                <Column field="fecha" header="Fecha" sortable style="min-width: 14rem" />
                <Column :exportable="false" style="min-width: 12rem">
                    <template #body="slotProps">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editVenta(slotProps.data)" />
                        <Button icon="pi pi-trash" outlined rounded severity="danger" @click="confirmDeleteVenta(slotProps.data)" />
                    </template>
                </Column>
            </DataTable>
        </div>

        <Dialog v-model:visible="ventaDialog" header="Venta" modal :closable="false" :style="{ width: '450px' }">
            <div class="p-fluid">
                <div class="field">
                    <label for="idProducto" class="block mb-2 font-bold">Nombre Producto:</label>

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
                    <small class="text-gray-500 italic">Solo se muestran productos con stock disponible.</small>
                    <div class="flex justify-end">
                        <Button
                            label="Limpiar"
                            icon="pi pi-trash"
                            iconPos="right"
                            class="p-button-secondary"
                            @click="productoSeleccionado = null"
                            :disabled="!productoSeleccionado"
                            title="Limpiar selección"
                        />
                    </div>
                </div>
                <div class="field" v-if="productoSeleccionado">
                    <label for="cantidad" class="block mb-2 font-bold">Cantidad:</label>
                    <InputNumber
                        id="cantidad"
                        v-model="venta.cantidad"
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
                    <label for="montoTotal" class="block mb-2 font-bold">Monto total:</label>
                    <InputNumber
                        id="montoTotal"
                        v-model="venta.montoTotal"
                        class="w-full"
                        prefix="Gs. "
                        :minFractionDigits="0"
                        :maxFractionDigits="0"
                        :disabled="true"
                    />

                </div>


            </div>

            <template #footer>
                <Button label="Cancelar" icon="pi pi-times" outlined @click="ventaDialog = false" />
                <Button label="Guardar" icon="pi pi-check" :disabled="isFormInvalid" @click="saveVenta" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteVentaDialog" header="Confirmar" modal :closable="false" :style="{ width: '350px' }">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem;"></i>
                <span>¿Está seguro que desea eliminar la venta con ID <b>{{ venta.id }}</b>?</span>
            </div>

            <template #footer>
                <Button label="No" icon="pi pi-times" outlined @click="deleteVentaDialog = false" />
                <Button label="Sí" icon="pi pi-check" severity="danger" @click="deleteVenta" />
            </template>
        </Dialog>
    </div>
</template>

<style scoped>
.field {
    margin-bottom: 1rem;
}
</style>
