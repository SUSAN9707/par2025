<script setup>
import {ref, onMounted, computed, watch} from 'vue'
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
    monto:0
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
})

function openNew() {
    venta.value = {
        id: null,
        productoId: '',
        cantidad: 1,
        monto:0
    }
    submitted.value = false
    cargarProductos()
    ventaDialog.value = true
}
async function cargarProductos() {
    try {
        const response = await getProductos();
        productos.value = response.data.data; // lista de clientes
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
    cargarProductos()
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
            venta.value = { id: null, productoId: '', cantidad: 1,
                monto:0 }
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
        if (venta.value.id) {
            const ventaParaActualizar= {
                productoId: venta.value.productoId,
                cantidad: venta.value.cantidad
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
                cantidad: venta.value.cantidad
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
            monto:0 }
    }
}
watch(
    [productoSeleccionado, () => venta.value.cantidad],
    ([nuevoProducto, nuevaCantidad]) => {
        if (nuevoProducto && nuevaCantidad) {
            console.log('1')
            venta.value.monto = nuevoProducto.precio * nuevaCantidad;
            console.log(venta.value.monto)
        } else {
            console.log('2')
            venta.value.monto = 0;
        }
    }
);



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
                <Column field="productoId" header="Producto ID" sortable style="min-width: 12rem" />
                <Column field="cantidad" header="Cantidad" sortable style="min-width: 10rem" />
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
                    <InputNumber id="cantidad" v-model="venta.cantidad" class="w-full" :min="1" />
                </div>
                <div class="field" v-if="productoSeleccionado">
                    <label for="monto" class="block mb-2 font-bold">Monto total:</label>
                    <InputNumber
                        id="monto"
                        v-model="venta.value.monto"
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
