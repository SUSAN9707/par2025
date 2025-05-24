<script setup>
import {ref, onMounted, computed} from 'vue';

import {useToast} from "primevue/usetoast";
import {actualizarCliente, crearCliente, eliminarCliente, getClientes} from "@/service/cliente.service";
const dt = ref();
const submitted = ref(false);
const clientes = ref([])



const cliente = ref({
    id: null,
    nombre: '',
    apellido: '',
    ruc: '',
    direccion: ''
});
const toast = useToast()

async function cargarClientes() {
    try {
        const response = await getClientes()
        // response.data tiene la estructura { mensaje, status, data }
        clientes.value = response.data.data // asignamos el array de clientes

        toast.add({
            severity: 'success',
            summary: 'Éxito',
            detail: response.data.mensaje || 'Clientes cargados',
            life: 3000
        })
    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.mensaje || 'Error al cargar clientes',
            life: 3000
        })
    }
}
const clienteDialog = ref(false);
const deleteClienteDialog = ref(false);

onMounted(() => {
    cargarClientes()


});

function openNew() {
    cliente.value = { id: '', nombre: '', apellido: '', ruc: '', direccion: '' };
    submitted.value = false;
    clienteDialog.value = true;
}




function editCliente(cli) {
    cliente.value = { ...cli };
    clienteDialog.value = true;
}

function confirmDeleteCliente(cli) {
    cliente.value = { ...cli };
    deleteClienteDialog.value = true;
}




function deleteCliente() {
    eliminarCliente(cliente.value.id)
        .then(response => {
            deleteClienteDialog.value = false;
            toast.add({
                severity: 'success',
                summary: 'Éxito',
                detail: response.data.mensaje,
                life: 3000
            });
            cargarClientes(); // 👈 vuelve a pedir la lista actualizada
            cliente.value = { id: null, nombre: '', apellido: '', ruc: '', direccion: '' };
        })
        .catch(error => {
            toast.add({
                severity: 'error',
                summary: 'Error',
                detail: error.response?.data?.mensaje || 'Error al eliminar cliente',
                life: 3000
            });
        });
}



const isFormInvalid = computed(() => {
    return !cliente.value.nombre.trim() ||
        !cliente.value.apellido.trim() ||
        !cliente.value.ruc.trim() ||
        !cliente.value.direccion.trim()
})

async function saveCliente() {
    try {
        if (cliente.value.id) {
            actualizarCliente(cliente.value.id, cliente.value)
                .then(response => {
                    toast.add({
                        severity: 'success',
                        summary: 'Éxito',
                        detail: response.data.mensaje,
                        life: 3000
                    })
                    cargarClientes()
                })
                .catch(error => {
                    toast.add({
                        severity: 'error',
                        summary: 'Error',
                        detail: error.response?.data?.mensaje || 'Error al actualizar cliente',
                        life: 3000
                    })
                })
        } else {
            crearCliente(cliente.value)
                .then(response => {
                    toast.add({
                        severity: 'success',
                        summary: 'Éxito',
                        detail: response.data.mensaje,
                        life: 3000
                    })
                    cargarClientes()
                })
                .catch(error => {
                    toast.add({
                        severity: 'error',
                        summary: 'Error',
                        detail: error.response?.data?.mensaje || 'Error al crear cliente',
                        life: 3000
                    })
                })
        }
        clienteDialog.value = false
        cliente.value = { id: null, nombre: '', apellido: '', ruc: '', direccion: '' }
    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.mensaje || 'Error al guardar cliente',
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

                :value="clientes"
                dataKey="id"

            >
                <Column field="id" header="ID" sortable style="min-width: 6rem"></Column>
                <Column field="nombre" header="Nombre" sortable style="min-width: 12rem"></Column>
                <Column field="apellido" header="Apellido" sortable style="min-width: 12rem"></Column>
                <Column field="ruc" header="RUC" sortable style="min-width: 12rem"></Column>
                <Column field="direccion" header="Dirección" sortable style="min-width: 20rem"></Column>
                <Column :exportable="false" style="min-width: 12rem">
                    <template #body="slotProps">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editCliente(slotProps.data)" />
                        <Button icon="pi pi-trash" outlined rounded severity="danger" @click="confirmDeleteCliente(slotProps.data)" />
                    </template>
                </Column>
            </DataTable>

        </div>

        <Dialog v-model:visible="clienteDialog" header="Cliente" modal :closable="false" :style="{ width: '450px' }">
            <div class="p-fluid">
                <div class="field">
                    <label for="nombre" class="block mb-2 font-bold">Nombre:</label>
                    <InputText id="nombre" v-model="cliente.nombre" class="w-full" />
                </div>
                <div class="field">
                    <label for="apellido" class="block mb-2 font-bold">Apellido:</label>
                    <InputText id="apellido" v-model="cliente.apellido" class="w-full" />
                </div>
                <div class="field">
                    <label for="ruc" class="block mb-2 font-bold">RUC:</label>
                    <InputText id="ruc" v-model="cliente.ruc" class="w-full" />
                </div>
                <div class="field">
                    <label for="direccion" class="block mb-2 font-bold">Dirección:</label>
                    <Textarea id="direccion" v-model="cliente.direccion" class="w-full" />
                </div>
            </div>



            <template #footer>
                <Button label="Cancelar" icon="pi pi-times" outlined @click="clienteDialog = false" />
                <Button label="Guardar" icon="pi pi-check" :disabled="isFormInvalid"  @click="saveCliente" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteClienteDialog" header="Confirmar" modal :closable="false" :style="{ width: '350px' }">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem;"></i>
                <span>¿Está seguro que desea eliminar el cliente <b>{{ cliente.nombre }}</b>?</span>
            </div>

            <template #footer>
                <Button label="No" icon="pi pi-times" outlined @click="deleteClienteDialog = false" />
                <Button label="Sí" icon="pi pi-check" severity="danger" @click="deleteCliente" />
            </template>
        </Dialog>



    </div>
</template>
<style scoped>
.field {
    margin-bottom: 1rem;
}
</style>
