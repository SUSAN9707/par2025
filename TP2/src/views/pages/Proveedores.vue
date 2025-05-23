<script setup>
import { ref, onMounted } from 'vue';
import { useToast } from 'primevue/usetoast';
import { ProductService } from '@/service/ProductService'; // Puedes renombrar esto luego si creas un ProviderService

const toast = useToast();
const dt = ref();
const providers = ref([]);
const providerDialog = ref(false);
const deleteProviderDialog = ref(false);
const deleteProvidersDialog = ref(false);
const provider = ref({ id: '', nombre: '', ruc: '', telefono: '', email: '', direccion: '' });
const selectedProviders = ref([]);
const submitted = ref(false);
const proveedores = ref([
    {
        id: 1,
        nombre: 'Proveedor A',
        ruc: '1234567-8',
        telefono: '0981 123456',
        email: 'a@correo.com',
        direccion: 'Calle 1'
    },
    {
        id: 2,
        nombre: 'Proveedor B',
        ruc: '2345678-9',
        telefono: '0982 654321',
        email: 'b@correo.com',
        direccion: 'Calle 2'
    }
]);

onMounted(() => {
    // Cuando integres tu API, actualiza esta línea:
    ProductService.getProducts().then((data) => (providers.value = data));
});

function openNew() {
    provider.value = { id: '', nombre: '', ruc: '', telefono: '', email: '', direccion: '' };
    submitted.value = false;
    providerDialog.value = true;
}

function hideDialog() {
    providerDialog.value = false;
    submitted.value = false;
}

function saveProvider() {
    submitted.value = true;

    if (provider.value.nombre && provider.value.nombre.trim() !== '') {
        if (provider.value.id) {
            const index = providers.value.findIndex(p => p.id === provider.value.id);
            if (index !== -1) {
                providers.value[index] = { ...provider.value };
                toast.add({ severity: 'success', summary: 'Éxito', detail: 'Proveedor actualizado', life: 3000 });
            }
        } else {
            provider.value.id = createId();
            providers.value.push({ ...provider.value });
            toast.add({ severity: 'success', summary: 'Éxito', detail: 'Proveedor creado', life: 3000 });
        }
        providerDialog.value = false;
        provider.value = { id: '', nombre: '', ruc: '', telefono: '', email: '', direccion: '' };
    } else {
        toast.add({ severity: 'error', summary: 'Error', detail: 'El nombre es obligatorio', life: 3000 });
    }
}

function editProvider(p) {
    provider.value = { ...p };
    providerDialog.value = true;
}

function confirmDeleteProvider(p) {
    provider.value = { ...p };
    deleteProviderDialog.value = true;
}

function deleteProvider() {
    providers.value = providers.value.filter(p => p.id !== provider.value.id);
    deleteProviderDialog.value = false;
    provider.value = { id: '', nombre: '', ruc: '', telefono: '', email: '', direccion: '' };
    toast.add({ severity: 'success', summary: 'Éxito', detail: 'Proveedor eliminado', life: 3000 });
}

function confirmDeleteSelected() {
    deleteProvidersDialog.value = true;
}

function deleteSelectedProviders() {
    providers.value = providers.value.filter(p => !selectedProviders.value.includes(p));
    deleteProvidersDialog.value = false;
    selectedProviders.value = [];
    toast.add({ severity: 'success', summary: 'Éxito', detail: 'Proveedores eliminados', life: 3000 });
}

function createId() {
    let id = '';
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    for (let i = 0; i < 5; i++) {
        id += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return id;
}
</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-6">
                <template #start>
                    <Button label="Nuevo" icon="pi pi-plus" severity="secondary" class="mr-2" @click="openNew" />
                    <Button label="Eliminar" icon="pi pi-trash" severity="secondary" @click="confirmDeleteSelected" :disabled="!selectedProviders.length" />
                </template>
                <template #end>
                    <Button label="Exportar" icon="pi pi-upload" severity="secondary" @click="dt.exportCSV()" />
                </template>
            </Toolbar>

            <DataTable
                ref="dt"
                v-model:selection="selectedProviders"
                :value="proveedores"
                dataKey="id"
                :paginator="true"
                :rows="10"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                :rowsPerPageOptions="[5, 10, 25]"
                currentPageReportTemplate="Mostrando {first} a {last} de {totalRecords} proveedores"
            >
                <Column selectionMode="multiple" style="width: 3rem" :exportable="false"></Column>
                <Column field="id" header="ID" sortable style="min-width: 6rem" />
                <Column field="nombre" header="Nombre" sortable style="min-width: 12rem" />
                <Column field="ruc" header="RUC" sortable />
                <Column field="telefono" header="Teléfono" sortable />
                <Column field="email" header="Email" />
                <Column field="direccion" header="Dirección" />

                <Column :exportable="false" style="min-width: 12rem">
                    <template #body="slotProps">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editProvider(slotProps.data)" />
                        <Button icon="pi pi-trash" outlined rounded severity="danger" @click="confirmDeleteProvider(slotProps.data)" />
                    </template>
                </Column>
            </DataTable>
        </div>

        <Dialog v-model:visible="providerDialog" :style="{ width: '450px' }" header="Detalles del Proveedor" :modal="true">
            <div class="flex flex-col gap-6">
                <div>
                    <label for="nombre" class="block font-bold mb-3">Nombre</label>
                    <InputText id="nombre" v-model.trim="provider.nombre" autofocus :invalid="submitted && !provider.nombre" fluid />
                    <small v-if="submitted && !provider.nombre" class="text-red-500">El nombre es obligatorio.</small>
                </div>

                <div>
                    <label for="ruc" class="block font-bold mb-3">RUC</label>
                    <InputText id="ruc" v-model="provider.ruc" fluid />
                </div>

                <div>
                    <label for="telefono" class="block font-bold mb-3">Teléfono</label>
                    <InputText id="telefono" v-model="provider.telefono" fluid />
                </div>

                <div>
                    <label for="email" class="block font-bold mb-3">Email</label>
                    <InputText id="email" v-model="provider.email" fluid />
                </div>

                <div>
                    <label for="direccion" class="block font-bold mb-3">Dirección</label>
                    <Textarea id="direccion" v-model="provider.direccion" rows="2" cols="20" fluid />
                </div>
            </div>

            <template #footer>
                <Button label="Cancelar" icon="pi pi-times" text @click="hideDialog" />
                <Button label="Guardar" icon="pi pi-check" @click="saveProvider" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteProviderDialog" :style="{ width: '450px' }" header="Confirmar" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle !text-3xl" />
                <span v-if="provider">¿Estás seguro que quieres eliminar <b>{{ provider.nombre }}</b>?</span>
            </div>
            <template #footer>
                <Button label="No" icon="pi pi-times" text @click="deleteProviderDialog = false" />
                <Button label="Sí" icon="pi pi-check" @click="deleteProvider" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteProvidersDialog" :style="{ width: '450px' }" header="Confirmar" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle !text-3xl" />
                <span>¿Estás seguro que quieres eliminar los proveedores seleccionados?</span>
            </div>
            <template #footer>
                <Button label="No" icon="pi pi-times" text @click="deleteProvidersDialog = false" />
                <Button label="Sí" icon="pi pi-check" text @click="deleteSelectedProviders" />
            </template>
        </Dialog>
    </div>
</template>
