<script setup>
import { ref, onMounted } from 'vue';
import { useToast } from 'primevue/usetoast';

const toast = useToast();
const dt = ref();
const reports = ref([]);
const reportDialog = ref(false);
const deleteReportDialog = ref(false);
const deleteReportsDialog = ref(false);
const report = ref({ id: '', montoTotal: 0, descripcion: '', idProductos: '' });
const selectedReports = ref([]);
const submitted = ref(false);
const reportes = ref([
    {
        id: 'R1',
        montoTotal: 1500000,
        descripcion: 'Reporte de ventas enero',
        idProductos: 'P1,P2,P3'
    },
    {
        id: 'R2',
        montoTotal: 980000,
        descripcion: 'Compras a proveedores',
        idProductos: 'P4,P5'
    },
    {
        id: 'R3',
        montoTotal: 500000,
        descripcion: 'Reporte interno',
        idProductos: 'P6'
    }
]);

onMounted(() => {
    // Aquí puedes llamar a tu API cuando la integres

});

function openNew() {
    report.value = { id: '', montoTotal: 0, descripcion: '', idProductos: '' };
    submitted.value = false;
    reportDialog.value = true;
}

function hideDialog() {
    reportDialog.value = false;
    submitted.value = false;
}

function saveReport() {
    submitted.value = true;

    if (report.value.descripcion && report.value.descripcion.trim() !== '') {
        if (report.value.id) {
            const index = reports.value.findIndex(r => r.id === report.value.id);
            if (index !== -1) {
                reports.value[index] = { ...report.value };
                toast.add({ severity: 'success', summary: 'Éxito', detail: 'Reporte actualizado', life: 3000 });
            }
        } else {
            report.value.id = createId();
            reports.value.push({ ...report.value });
            toast.add({ severity: 'success', summary: 'Éxito', detail: 'Reporte creado', life: 3000 });
        }
        reportDialog.value = false;
        report.value = { id: '', montoTotal: 0, descripcion: '', idProductos: '' };
    } else {
        toast.add({ severity: 'error', summary: 'Error', detail: 'La descripción es obligatoria', life: 3000 });
    }
}

function editReport(rep) {
    report.value = { ...rep };
    reportDialog.value = true;
}

function confirmDeleteReport(rep) {
    report.value = { ...rep };
    deleteReportDialog.value = true;
}

function deleteReport() {
    reports.value = reports.value.filter(r => r.id !== report.value.id);
    deleteReportDialog.value = false;
    report.value = { id: '', montoTotal: 0, descripcion: '', idProductos: '' };
    toast.add({ severity: 'success', summary: 'Éxito', detail: 'Reporte eliminado', life: 3000 });
}

function confirmDeleteSelected() {
    deleteReportsDialog.value = true;
}

function deleteSelectedReports() {
    reports.value = reports.value.filter(r => !selectedReports.value.includes(r));
    deleteReportsDialog.value = false;
    selectedReports.value = [];
    toast.add({ severity: 'success', summary: 'Éxito', detail: 'Reportes eliminados', life: 3000 });
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
                    <Button label="Nuevo" icon="pi pi-plus" severity="secondary" class="mr-2" @click="openNew" />
                    <Button label="Eliminar" icon="pi pi-trash" severity="secondary" @click="confirmDeleteSelected" :disabled="!selectedReports.length" />
                </template>
                <template #end>
                    <Button label="Exportar" icon="pi pi-upload" severity="secondary" @click="dt.exportCSV()" />
                </template>
            </Toolbar>

            <DataTable
                ref="dt"
                v-model:selection="selectedReports"
                :value="reportes"
                dataKey="id"
                :paginator="true"
                :rows="10"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                :rowsPerPageOptions="[5, 10, 25]"
                currentPageReportTemplate="Mostrando {first} a {last} de {totalRecords} reportes"
            >
                <Column selectionMode="multiple" style="width: 3rem" :exportable="false"></Column>
                <Column field="id" header="ID" sortable style="min-width: 6rem"></Column>
                <Column field="descripcion" header="Descripción" style="min-width: 20rem"></Column>
                <Column field="idProductos" header="IDs de Productos" style="min-width: 16rem"></Column>
                <Column field="montoTotal" header="Monto Total" :body="(data) => formatoGs(data.montoTotal)"></Column>
                <Column :exportable="false" style="min-width: 12rem">
                    <template #body="slotProps">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editReport(slotProps.data)" />
                        <Button icon="pi pi-trash" outlined rounded severity="danger" @click="confirmDeleteReport(slotProps.data)" />
                    </template>
                </Column>
            </DataTable>
        </div>

        <Dialog v-model:visible="reportDialog" :style="{ width: '450px' }" header="Detalles del Reporte" :modal="true">
            <div class="flex flex-col gap-6">
                <div>
                    <label for="descripcion" class="block font-bold mb-3">Descripción</label>
                    <Textarea id="descripcion" v-model="report.descripcion" rows="3" cols="20" fluid />
                    <small v-if="submitted && !report.descripcion" class="text-red-500">La descripción es obligatoria.</small>
                </div>

                <div>
                    <label for="idProductos" class="block font-bold mb-3">IDs de Productos</label>
                    <InputText id="idProductos" v-model.trim="report.idProductos" fluid />
                </div>

                <div>
                    <label for="montoTotal" class="block font-bold mb-3">Monto Total</label>
                    <InputNumber
                        id="montoTotal"
                        v-model="report.montoTotal"
                        mode="currency"
                        currency="PYG"
                        locale="es-PY"
                        :min="0"
                        :useGrouping="true"
                        :currencyDisplay="'symbol'"
                        fluid
                    />
                </div>
            </div>

            <template #footer>
                <Button label="Cancelar" icon="pi pi-times" text @click="hideDialog" />
                <Button label="Guardar" icon="pi pi-check" @click="saveReport" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteReportDialog" :style="{ width: '450px' }" header="Confirmar" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle !text-3xl" />
                <span v-if="report">¿Estás seguro que quieres eliminar <b>{{ report.descripcion }}</b>?</span>
            </div>
            <template #footer>
                <Button label="No" icon="pi pi-times" text @click="deleteReportDialog = false" />
                <Button label="Sí" icon="pi pi-check" @click="deleteReport" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteReportsDialog" :style="{ width: '450px' }" header="Confirmar" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle !text-3xl" />
                <span>¿Estás seguro que quieres eliminar los reportes seleccionados?</span>
            </div>
            <template #footer>
                <Button label="No" icon="pi pi-times" text @click="deleteReportsDialog = false" />
                <Button label="Sí" icon="pi pi-check" text @click="deleteSelectedReports" />
            </template>
        </Dialog>
    </div>
</template>
