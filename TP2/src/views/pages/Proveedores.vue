<script setup>
import { ref, onMounted, computed } from 'vue'
import { useToast } from 'primevue/usetoast'
import {
    getProveedores,
    crearProveedor,
    actualizarProveedor,
    eliminarProveedor
} from '@/service/proveedor.service'

const dt = ref()
const proveedores = ref([])
const proveedor = ref({
    id: null,
    nombre: '',
    ruc: '',
    telefono: '',
    email: '',
    direccion: ''
})
const proveedorDialog = ref(false)
const deleteProveedorDialog = ref(false)
const submitted = ref(false)
const toast = useToast()

async function cargarProveedores() {
    try {
        const response = await getProveedores()
        proveedores.value = response.data.data
        toast.add({
            severity: 'success',
            summary: 'Éxito',
            detail: response.data.mensaje || 'Proveedores cargados',
            life: 3000
        })
    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.mensaje || 'Error al cargar proveedores',
            life: 3000
        })
    }
}

onMounted(() => {
    cargarProveedores()
})

function openNew() {
    proveedor.value = { id: null, nombre: '', ruc: '', telefono: '', email: '', direccion: '' }
    submitted.value = false
    proveedorDialog.value = true
}

function editProveedor(p) {
    proveedor.value = { ...p }
    proveedorDialog.value = true
}

function confirmDeleteProveedor(p) {
    proveedor.value = { ...p }
    deleteProveedorDialog.value = true
}

async function deleteProveedor() {
    try {
        const response = await eliminarProveedor(proveedor.value.id)
        toast.add({
            severity: 'success',
            summary: 'Éxito',
            detail: response.data.mensaje,
            life: 3000
        })
        deleteProveedorDialog.value = false
        cargarProveedores()
        proveedor.value = { id: null, nombre: '', ruc: '', telefono: '', email: '', direccion: '' }
    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.mensaje || 'Error al eliminar proveedor',
            life: 3000
        })
    }
}

const isFormInvalid = computed(() => {
    return !proveedor.value.nombre.trim() ||
        !proveedor.value.ruc.trim() ||
        !proveedor.value.telefono.trim() ||
        !proveedor.value.email.trim() ||
        !proveedor.value.direccion.trim()
})

async function saveProveedor() {
    try {
        if (proveedor.value.id) {
            const response = await actualizarProveedor(proveedor.value.id, proveedor.value)
            toast.add({
                severity: 'success',
                summary: 'Éxito',
                detail: response.data.mensaje,
                life: 3000
            })
        } else {
            const response = await crearProveedor(proveedor.value)
            toast.add({
                severity: 'success',
                summary: 'Éxito',
                detail: response.data.mensaje,
                life: 3000
            })
        }
        cargarProveedores()
        proveedorDialog.value = false
        proveedor.value = { id: null, nombre: '', ruc: '', telefono: '', email: '', direccion: '' }
    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.mensaje || 'Error al guardar proveedor',
            life: 3000
        })
    }
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

            <DataTable
                ref="dt"
                :value="proveedores"
                dataKey="id"
            >
                <Column field="id" header="ID" sortable style="min-width: 6rem"></Column>
                <Column field="nombre" header="Nombre" sortable style="min-width: 12rem"></Column>
                <Column field="ruc" header="RUC" sortable style="min-width: 12rem"></Column>
                <Column field="telefono" header="Teléfono" sortable style="min-width: 12rem"></Column>
                <Column field="email" header="Email" sortable style="min-width: 16rem"></Column>
                <Column field="direccion" header="Dirección" sortable style="min-width: 20rem"></Column>
                <Column :exportable="false" style="min-width: 12rem">
                    <template #body="slotProps">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editProveedor(slotProps.data)" />
                        <Button icon="pi pi-trash" outlined rounded severity="danger" @click="confirmDeleteProveedor(slotProps.data)" />
                    </template>
                </Column>
            </DataTable>
        </div>

        <Dialog v-model:visible="proveedorDialog" header="Proveedor" modal :closable="false" :style="{ width: '500px' }">
            <div class="p-fluid">
                <div class="field">
                    <label for="nombre" class="block mb-2 font-bold">Nombre:</label>
                    <InputText id="nombre" v-model="proveedor.nombre" class="w-full" />
                </div>
                <div class="field">
                    <label for="ruc" class="block mb-2 font-bold">RUC:</label>
                    <InputText id="ruc" v-model="proveedor.ruc" class="w-full" />
                </div>
                <div class="field">
                    <label for="telefono" class="block mb-2 font-bold">Teléfono:</label>
                    <InputText id="telefono" v-model="proveedor.telefono" class="w-full" />
                </div>
                <div class="field">
                    <label for="email" class="block mb-2 font-bold">Email:</label>
                    <InputText id="email" v-model="proveedor.email" class="w-full" />
                </div>
                <div class="field">
                    <label for="direccion" class="block mb-2 font-bold">Dirección:</label>
                    <Textarea id="direccion" v-model="proveedor.direccion" class="w-full" />
                </div>
            </div>

            <template #footer>
                <Button label="Cancelar" icon="pi pi-times" outlined @click="proveedorDialog = false" />
                <Button label="Guardar" icon="pi pi-check" :disabled="isFormInvalid" @click="saveProveedor" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteProveedorDialog" header="Confirmar" modal :closable="false" :style="{ width: '350px' }">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem;"></i>
                <span>¿Está seguro que desea eliminar el proveedor <b>{{ proveedor.nombre }}</b>?</span>
            </div>

            <template #footer>
                <Button label="No" icon="pi pi-times" outlined @click="deleteProveedorDialog = false" />
                <Button label="Sí" icon="pi pi-check" severity="danger" @click="deleteProveedor" />
            </template>
        </Dialog>
    </div>
</template>

<style scoped>
.field {
    margin-bottom: 1rem;
}
</style>
