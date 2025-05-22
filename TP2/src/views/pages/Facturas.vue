<script setup>
import { ref, onMounted } from 'vue';
import { useToast } from 'primevue/usetoast';

const toast = useToast();
const dt = ref();
const facturas = ref([]);
const facturaDialog = ref(false);
const deleteFacturaDialog = ref(false);
const deleteFacturasDialog = ref(false);
const factura = ref({ id: '', numero: '', cliente: '', total: 0 });
const selectedFacturas = ref([]);
const submitted = ref(false);

// Datos de ejemplo
const facturasEjemplo = ref([
    { id: 1, numero: 'F001', cliente: 'Cliente A', total: 1500000 },
    { id: 2, numero: 'F002', cliente: 'Cliente B', total: 750000 },
    { id: 3, numero: 'F003', cliente: 'Cliente C', total: 275000 }
]);

onMounted(() => {
    facturas.value = facturasEjemplo.value;
});

function openNew() {
    factura.value = { id: '', numero: '', cliente: '', total: 0 };
    submitted.value = false;
    facturaDialog.value = true;
}

function hideDialog() {
    facturaDialog.value = false;
    submitted.value = false;
}

function saveFactura() {
    submitted.value = true;

    if (factura.value.numero && factura.value.numero.trim() !== '') {
        if (factura.value.id) {
            const index = facturas.value.findIndex(f => f.id === factura.value.id);
            if (index !== -1) {
                facturas.value[index] = { ...factura.value };
                toast.add({ severity: 'success', summary: 'Éxito', detail: 'Factura actualizada', life: 3000 });
            }
        } else {
            factura.value.id = createId();
            facturas.value.push({ ...factura.value });
            toast.add({ severity: 'success', summary: 'Éxito', detail: 'Factura creada', life: 3000 });
        }
        facturaDialog.value = false;
        factura.value = { id: '', numero: '', cliente: '', total: 0 };
    } else {
        toast.add({ severity: 'error', summary: 'Error', detail: 'El número es obligatorio', life: 3000 });
    }
}

function editFactura(fact) {
    factura.value = { ...fact };
    facturaDialog.value = true;
}

function confirmDeleteFactura(fact) {
    factura.value = { ...fact };
    deleteFacturaDialog.value = true;
}

function deleteFactura() {
    facturas.value = facturas.value.filter(f => f.id !== factura.value.id);
    deleteFacturaDialog.value = false;
    factura.value = { id: '', numero: '', cliente: '', total: 0 };
    toast.add({ severity: 'success', summary: 'Éxito', detail: 'Factura eliminada', life: 3000 });
}

function confirmDeleteSelected() {
    deleteFacturasDialog.value = true;
}

function deleteSelectedFacturas() {
    facturas.value = facturas.value.filter(f => !selectedFacturas.value.includes(f));
    deleteFacturasDialog.value = false;
    selectedFacturas.value = [];
    toast.add({ severity: 'success', summary: 'Éxito', detail: 'Facturas eliminadas', life: 3000 });
}

function formatoGs(valor) {
    if (valor == null) return '';
    return 'Gs. ' + valor.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
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
                    <Button label="Nueva Factura" icon="pi pi-plus" severity="secondary" class="mr-2" @click="openNew" />
                    <Button label="Eliminar" icon="pi pi-trash" severity="secondary" @click="confirmDeleteSelected" :disabled="!selectedFacturas.length" />
                </template>
                <template #end>
                    <Button label="Exportar" icon="pi pi-upload" severity="secondary" @click="dt.exportCSV()" />
                </template>
            </Toolbar>

            <DataTable
                ref="dt"
                v-model:selection="selectedFacturas"
                :value="facturas"
                dataKey="id"
                :paginator="true"
                :rows="10"
                :rowsPerPageOptions="[5, 10, 25]"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                currentPageReportTemplate="Mostrando {first} a {last} de {totalRecords} facturas"
            >
                <Column selectionMode="multiple" style="width: 3rem" :exportable="false" />
                <Column field="id" header="ID" sortable />
                <Column field="numero" header="Número" sortable />
                <Column field="cliente" header="Cliente" sortable />
                <Column field="total" header="Total" :body="(data) => formatoGs(data.total)" sortable />
                <Column :exportable="false" style="min-width: 12rem">
                    <template #body="slotProps">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editFactura(slotProps.data)" />
                        <Button icon="pi pi-trash" outlined rounded severity="danger" @click="confirmDeleteFactura(slotProps.data)" />
                    </template>
                </Column>
            </DataTable>
        </div>

        <Dialog v-model:visible="facturaDialog" :style="{ width: '450px' }" header="Detalles de la Factura" :modal="true">
            <div class="flex flex-col gap-6">
                <div>
                    <label for="numero" class="block font-bold mb-3">Número</label>
                    <InputText id="numero" v-model.trim="factura.numero" autofocus :invalid="submitted && !factura.numero" />
                    <small v-if="submitted && !factura.numero" class="text-red-500">El número es obligatorio.</small>
                </div>

                <div>
                    <label for="cliente" class="block font-bold mb-3">Cliente</label>
                    <InputText id="cliente" v-model="factura.cliente" />
                </div>

                <div>
                    <label for="total" class="block font-bold mb-3">Total</label>
                    <InputNumber
                        id="total"
                        v-model="factura.total"
                        mode="currency"
                        currency="PYG"
                        locale="es-PY"
                        :min="0"
                        :useGrouping="true"
                        :currencyDisplay="'symbol'"
                    />
                </div>
            </div>

            <template #footer>
                <Button label="Cancelar" icon="pi pi-times" text @click="hideDialog" />
                <Button label="Guardar" icon="pi pi-check" @click="saveFactura" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteFacturaDialog" :style="{ width: '450px' }" header="Confirmar" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle !text-3xl" />
                <span v-if="factura">¿Estás seguro que deseas eliminar la factura <b>{{ factura.numero }}</b>?</span>
            </div>
            <template #footer>
                <Button label="No" icon="pi pi-times" text @click="deleteFacturaDialog = false" />
                <Button label="Sí" icon="pi pi-check" @click="deleteFactura" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteFacturasDialog" :style="{ width: '450px' }" header="Confirmar" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle !text-3xl" />
                <span>¿Estás seguro que deseas eliminar las facturas seleccionadas?</span>
            </div>
            <template #footer>
                <Button label="No" icon="pi pi-times" text @click="deleteFacturasDialog = false" />
                <Button label="Sí" icon="pi pi-check" text @click="deleteSelectedFacturas" />
            </template>
        </Dialog>
    </div>
</template>
