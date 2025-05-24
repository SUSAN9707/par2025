<script setup>
import { ref, onMounted, computed } from 'vue'
import { useToast } from 'primevue/usetoast'
import {
    getCompras,
    crearCompra,
    actualizarCompra,
    eliminarCompra
} from '@/service/compra.service'

const dt = ref()
const submitted = ref(false)
const compras = ref([])

const compra = ref({
    id: null,
    productoId: '',
    cantidad: 1,
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

onMounted(() => {
    cargarCompras()
})

function openNew() {
    compra.value = {
        id: null,
        productoId: '',
        cantidad: 1
    }
    submitted.value = false
    compraDialog.value = true
}

function editCompra(com) {
    compra.value = { ...com }
    compraDialog.value = true
}

function confirmDeleteCompra(com) {
    compra.value = { ...com }
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
            compra.value = { id: null, productoId: '', cantidad: 1 }
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
        const compraBody = {
            productoId: compra.value.productoId,
            cantidad: compra.value.cantidad
        }

        if (compra.value.id) {
            actualizarCompra(compra.value.id, compraBody)
                .then(response => {
                    toast.add({
                        severity: 'success',
                        summary: 'Éxito',
                        detail: response.data.mensaje,
                        life: 3000
                    })
                    cargarCompras()
                })
                .catch(error => {
                    toast.add({
                        severity: 'error',
                        summary: 'Error',
                        detail: error.response?.data?.mensaje || 'Error al actualizar compra',
                        life: 3000
                    })
                })
        } else {
            console.log(compraBody)
            crearCompra(compraBody)
                .then(response => {
                    toast.add({
                        severity: 'success',
                        summary: 'Éxito',
                        detail: response.data.mensaje,
                        life: 3000
                    })
                    cargarCompras()
                })
                .catch(error => {
                    toast.add({
                        severity: 'error',
                        summary: 'Error',
                        detail: error.response?.data?.mensaje || 'Error al crear compra',
                        life: 3000
                    })
                })
        }

        compraDialog.value = false
        compra.value = { id: null, productoId: '', cantidad: 1 }
    }
}
</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-6">
                <template #start>
                    <Button label="Nueva compra" icon="pi pi-plus" severity="secondary" class="mr-2" @click="openNew" />
                </template>
                <template #end>
                    <Button label="Exportar" icon="pi pi-upload" severity="secondary" @click="dt.exportCSV()" />
                </template>
            </Toolbar>

            <DataTable ref="dt" :value="compras" dataKey="id">
                <Column field="id" header="ID" sortable style="min-width: 6rem" />
                <Column field="productoId" header="Producto ID" sortable style="min-width: 12rem" />
                <Column field="cantidad" header="Cantidad" sortable style="min-width: 10rem" />
                <Column field="fecha" header="Fecha" sortable style="min-width: 14rem" />
                <Column :exportable="false" style="min-width: 12rem">
                    <template #body="slotProps">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editCompra(slotProps.data)" />
                        <Button icon="pi pi-trash" outlined rounded severity="danger" @click="confirmDeleteCompra(slotProps.data)" />
                    </template>
                </Column>
            </DataTable>
        </div>

        <Dialog v-model:visible="compraDialog" header="Compra" modal :closable="false" :style="{ width: '450px' }">
            <div class="p-fluid">
                <div class="field">
                    <label for="productoId" class="block mb-2 font-bold">Producto ID:</label>
                    <InputText id="productoId" v-model="compra.productoId" class="w-full" />
                </div>
                <div class="field">
                    <label for="cantidad" class="block mb-2 font-bold">Cantidad:</label>
                    <InputNumber id="cantidad" v-model="compra.cantidad" class="w-full" :min="1" />
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

<style scoped>
.field {
    margin-bottom: 1rem;
}
</style>
