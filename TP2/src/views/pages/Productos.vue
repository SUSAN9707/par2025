<script setup>
import { ref, onMounted, computed } from 'vue'
import { useToast } from 'primevue/usetoast'
import {
    getProductos,
    crearProducto,
    actualizarProducto,
    eliminarProducto
} from '@/service/producto.service'

const dt = ref()
const productos = ref([])
const productoDialog = ref(false)
const deleteProductoDialog = ref(false)
const submitted = ref(false)

const producto = ref({
    id: null,
    nombre: '',
    descripcion: '',
    precio: null,
    stock: 0
});


const toast = useToast()

async function cargarProductos() {
    try {
        const response = await getProductos()
        productos.value = response.data.data

        toast.add({
            severity: 'success',
            summary: 'Éxito',
            detail: response.data.mensaje || 'Productos cargados',
            life: 3000
        })
    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.mensaje || 'Error al cargar productos',
            life: 3000
        })
    }
}

onMounted(() => {
    cargarProductos()
})

function openNew() {
    producto.value = { id: null, nombre: '', precio: '', descripcion: '',stock: 0 }
    submitted.value = false
    productoDialog.value = true
}

function editProducto(p) {
    producto.value = { ...p }
    productoDialog.value = true
}

function confirmDeleteProducto(p) {
    producto.value = { ...p }
    deleteProductoDialog.value = true
}

function deleteProducto() {
    eliminarProducto(producto.value.id)
        .then(response => {
            deleteProductoDialog.value = false
            toast.add({
                severity: 'success',
                summary: 'Éxito',
                detail: response.data.mensaje,
                life: 3000
            })
            cargarProductos()
        })
        .catch(error => {
            toast.add({
                severity: 'error',
                summary: 'Error',
                detail: error.response?.data?.mensaje || 'Error al eliminar producto',
                life: 3000
            })
        })
}

const isFormInvalid = computed(() => {
    return !producto.value.nombre.trim() ||
        !producto.value.precio ||
        !producto.value.descripcion.trim()
})

function saveProducto() {
    if (producto.value.id) {
        actualizarProducto(producto.value.id, producto.value)
            .then(response => {
                toast.add({
                    severity: 'success',
                    summary: 'Éxito',
                    detail: response.data.mensaje,
                    life: 3000
                })
                cargarProductos()
            })
            .catch(error => {
                toast.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: error.response?.data?.mensaje || 'Error al actualizar producto',
                    life: 3000
                })
            })
    } else {
        crearProducto(producto.value)
            .then(response => {
                toast.add({
                    severity: 'success',
                    summary: 'Éxito',
                    detail: response.data.mensaje,
                    life: 3000
                })
                cargarProductos()
            })
            .catch(error => {
                toast.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: error.response?.data?.mensaje || 'Error al crear producto',
                    life: 3000
                })
            })
    }

    productoDialog.value = false
    producto.value = { id: null, nombre: '', precio: '', descripcion: '' }
}
function formatPrecio(valor) {
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
                    <Button label="Nuevo" icon="pi pi-plus" severity="secondary" class="mr-2" @click="openNew" />
                </template>
                <template #end>
                    <Button label="Exportar" icon="pi pi-upload" severity="secondary" @click="dt.exportCSV()" />
                </template>
            </Toolbar>

            <DataTable ref="dt" :value="productos" dataKey="id">
                <Column field="id" header="ID" sortable style="min-width: 6rem"></Column>
                <Column field="nombre" header="Nombre" sortable style="min-width: 12rem"></Column>
                <Column header="Precio" style="min-width: 10rem">
                    <template #body="slotProps">
                        {{ formatPrecio(slotProps.data.precio) }}
                    </template>
                </Column>
                <Column field="stock" header="Stock" sortable style="min-width: 8rem"></Column>
                <Column field="descripcion" header="Descripción" sortable style="min-width: 20rem"></Column>
                <Column :exportable="false" style="min-width: 12rem">
                    <template #body="slotProps">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editProducto(slotProps.data)" />
                        <Button icon="pi pi-trash" outlined rounded severity="danger" @click="confirmDeleteProducto(slotProps.data)" />
                    </template>
                </Column>
            </DataTable>
        </div>

        <Dialog v-model:visible="productoDialog" header="Producto" modal :closable="false" :style="{ width: '450px' }">
            <div class="p-fluid">
                <div class="field">
                    <label for="nombre" class="block mb-2 font-bold">Nombre:</label>
                    <InputText id="nombre" v-model="producto.nombre" class="w-full" />
                </div>
                <div class="field">
                    <label for="precio" class="block mb-2 font-bold">Precio:</label>
                    <InputNumber
                        id="precio"
                        v-model="producto.precio"
                        class="w-full"
                        prefix="Gs. "
                        :minFractionDigits="0"
                        :maxFractionDigits="0"
                    />
                </div>
                <div class="field">
                    <label for="stock" class="block mb-2 font-bold">Stock:</label>
                    <InputNumber
                        id="stock"
                        v-model="producto.stock"
                        class="w-full"
                        inputId="stock"
                        :min="0"
                        showButtons
                        buttonLayout="horizontal"
                        incrementButtonIcon="pi pi-plus"
                        decrementButtonIcon="pi pi-minus"
                    />
                </div>

                <div class="field">
                    <label for="descripcion" class="block mb-2 font-bold">Descripción:</label>
                    <Textarea id="descripcion" v-model="producto.descripcion" class="w-full" />
                </div>
            </div>

            <template #footer>
                <Button label="Cancelar" icon="pi pi-times" outlined @click="productoDialog = false" />
                <Button label="Guardar" icon="pi pi-check" :disabled="isFormInvalid" @click="saveProducto" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteProductoDialog" header="Confirmar" modal :closable="false" :style="{ width: '350px' }">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem;"></i>
                <span>¿Está seguro que desea eliminar el producto <b>{{ producto.nombre }}</b>?</span>
            </div>

            <template #footer>
                <Button label="No" icon="pi pi-times" outlined @click="deleteProductoDialog = false" />
                <Button label="Sí" icon="pi pi-check" severity="danger" @click="deleteProducto" />
            </template>
        </Dialog>
    </div>
</template>

<style scoped>
.field {
    margin-bottom: 1rem;
}
</style>
