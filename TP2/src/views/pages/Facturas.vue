<script setup>
import {ref, onMounted, computed, watch} from 'vue';

import { useToast } from "primevue/usetoast";
import {anularFactura, crearFactura, getFacturas} from "@/service/facturas.service";
import {getClientes} from "@/service/cliente.service";

const dt = ref();
const submitted = ref(false);
const facturas = ref([]);
const clientes = ref([]);
const clienteSeleccionado = ref(null);
const factura = ref({
    id: null,
    numero: '',
    total: null,
    idClienteProv: null,  // cliente o proveedor según tipoFactura
    tipoFactura: '',      // 'COMPRA' o 'VENTA'
    estado: 'VIGENTE',     // estado inicial por defecto,
    referencias:[]
});

const toast = useToast();
async function cargarClientes() {
    try {
        const response = await getClientes();
        clientes.value = response.data.data; // lista de clientes
    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.mensaje || 'Error al cargar clientes',
            life: 3000
        });
        facturaDialog.value=false
    }
}
async function cargarFacturas() {
    try {
        const response = await getFacturas();
        facturas.value = response.data.data;
        console.log(factura.value.numero)
        toast.add({
            severity: 'success',
            summary: 'Éxito',
            detail: response.data.mensaje || 'Facturas cargadas',
            life: 3000
        });
    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.mensaje || 'Error al cargar facturas',
            life: 3000
        });
    }
}

const facturaDialog = ref(false);
const deleteFacturaDialog = ref(false);

onMounted(() => {
    cargarFacturas();
});

function openNew() {
    factura.value = { id: null,
        numero: '',
        total: null,
        idClienteProv: null,  // cliente o proveedor según tipoFactura
        tipoFactura: '',      // 'COMPRA' o 'VENTA'
        estado: 'VIGENTE',
        referencias:[]};
    factura.value.numero = generarNumero();
    clienteSeleccionado.value = null; // limpiar selección
    submitted.value = false;
    facturaDialog.value = true;
    cargarClientes();
}
watch(clienteSeleccionado, (nuevo) => {
    factura.value.idCliente = nuevo ? nuevo.id : null;
});


function anular(fac) {
    factura.value = { ...fac };
    deleteFacturaDialog.value = true;
}

function confirmarAnular() {
    anularFactura(factura.value.id)
        .then(response => {
            deleteFacturaDialog.value = false;
            toast.add({
                severity: 'success',
                summary: 'Éxito',
                detail: response.data.mensaje,
                life: 3000
            });
            cargarFacturas();
            factura.value = { id: null, numero: '', total: null, idCliente: null };
        })
        .catch(error => {
            toast.add({
                severity: 'error',
                summary: 'Error',
                detail: error.response?.data?.mensaje || 'Error al anular factura',
                life: 3000
            });
        });
}

const isFormInvalid = computed(() => {
    return !factura.value.numero.trim() ||
        factura.value.total === null || factura.value.total <= 0 ||
        factura.value.idCliente === null||clienteSeleccionado.value===null;
});
function generarNumero(){
    if (facturas.value.length === 0) {
        return 'F-0001';
    }

    // Buscar el número más alto
    const numeros = facturas.value
        .map(f => f.numero)
        .filter(num => /^F-\d+$/.test(num)) // aseguramos formato correcto
        .map(num => parseInt(num.split('-')[1])) // nos quedamos con el número

    const maxNumero = Math.max(...numeros);
    const siguiente = maxNumero + 1;

    // Formateamos con ceros a la izquierda (mínimo 4 dígitos)
    const numeroFormateado = siguiente.toString().padStart(4, '0');

    return `F-${numeroFormateado}`;
}
async function saveFactura() {
    try {
        const facturaBody = {
            cliente:clienteSeleccionado.value?.nombre+' '+clienteSeleccionado.value?.apellido,
            numero: factura.value.numero === '' ? generarNumero() : factura.value.numero,
            total: factura.value.total,
            idCliente:  clienteSeleccionado.value?.id ?? null,
            //FALTA estado y tipo factura
            referencias: []
        }
            crearFactura(facturaBody)
                .then(response => {
                    toast.add({
                        severity: 'success',
                        summary: 'Éxito',
                        detail: response.data.mensaje,
                        life: 3000
                    });
                    cargarFacturas();
                })
                .catch(error => {
                    toast.add({
                        severity: 'error',
                        summary: 'Error',
                        detail: error.response?.data?.mensaje || 'Error al crear factura',
                        life: 3000
                    });
                });

        facturaDialog.value = false;
        factura.value = { id: null, numero: '', total: null, idCliente: null };
    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.mensaje || 'Error al guardar factura',
            life: 3000
        });
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
                :value="facturas"
                dataKey="id"
            >
                <Column field="id" header="ID" sortable style="min-width: 6rem" />
                <Column field="numero" header="Número" sortable style="min-width: 12rem" />
                <Column field="total" header="Total" sortable style="min-width: 12rem" />
                <Column field="idClienteProv" header="ID Cliente/Proveedor" sortable style="min-width: 14rem" />
                <Column field="tipoFactura" header="Tipo Factura" sortable style="min-width: 12rem" />
                <Column field="estado" header="Estado" sortable style="min-width: 10rem">
                    <template #body="slotProps">
                        <span
                            :class="{
                            'estado-vigente': slotProps.data.estado === 'VIGENTE',
                            'estado-anulada': slotProps.data.estado === 'ANULADA'
                          }"
                        >
                          {{ slotProps.data.estado }}
                        </span>
                    </template>
                </Column>

                <Column :exportable="false" style="min-width: 12rem">
                    <template #body="slotProps">
                        <Button icon="pi pi-times" outlined rounded severity="danger" @click="anular(slotProps.data)" />
                    </template>
                </Column>
            </DataTable>

        </div>

        <Dialog v-model:visible="facturaDialog" header="Factura" modal :closable="false" :style="{ width: '450px' }">
            <div class="p-fluid">
                <div class="field" >
                    <label for="numero" class="block mb-2 font-bold">Número:</label>
                    <InputText id="numero" v-model="factura.numero" class="w-full" disabled />
                </div>
                <div class="field">
                    <label for="total" class="block mb-2 font-bold">Total:</label>
                    <InputNumber
                        id="total"
                        v-model="factura.total"
                        class="w-full"
                        prefix="Gs. "
                        :minFractionDigits="0"
                        :maxFractionDigits="0"
                    />
                </div>
                <div class="field">
                    <label for="idCliente" class="block mb-2 font-bold">Cliente (RUC):</label>

                    <Dropdown
                        id="idCliente"
                        v-model="clienteSeleccionado"
                        :options="clientes"
                        optionLabel="ruc"
                        :optionValue="cliente => cliente"
                        filter
                        filterBy="ruc"
                        placeholder="Seleccione un cliente"
                        class="w-full mb-2"
                    />

                    <div class="flex justify-end">
                        <Button
                            label="Limpiar"
                            icon="pi pi-trash"
                            iconPos="right"
                            class="p-button-secondary"
                            @click="clienteSeleccionado = null"
                            :disabled="!clienteSeleccionado"
                            title="Limpiar selección"
                        />
                    </div>
                </div>


                <div class="field" v-if="clienteSeleccionado">
                    <label class="block mb-2 font-bold">Cliente Seleccionado:</label>
                    <InputText :value="clienteSeleccionado.nombre + ' ' + clienteSeleccionado.apellido" disabled class="w-full" />
                </div>
            </div>

            <template #footer>
                <Button label="Cancelar" icon="pi pi-times" outlined @click="facturaDialog = false" />
                <Button label="Guardar" icon="pi pi-check" :disabled="isFormInvalid" @click="saveFactura" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteFacturaDialog" header="Confirmar" modal :closable="false" :style="{ width: '350px' }">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem;"></i>
                <span>¿Está seguro que desea anular la factura <b>{{ factura.numero }}</b>?</span>
            </div>

            <template #footer>
                <Button label="No" icon="pi pi-times" outlined @click="deleteFacturaDialog = false" />
                <Button label="Sí" icon="pi pi-check" severity="danger" @click="confirmarAnular" />
            </template>
        </Dialog>
    </div>
</template>

<style scoped>
.field {
    margin-bottom: 1rem;
}
.estado-vigente {
    color: #155724;
    background-color: rgba(40, 167, 69, 0.5); /* verde con 50% opacidad */
    border: 1px solid #28a745;
    border-radius: 8px;
    padding: 2px 8px;
    font-weight: bold;
}

.estado-anulada {
    color: #721c24;
    background-color: rgba(220, 53, 69, 0.5); /* rojo con 50% opacidad */
    border: 1px solid #dc3545;
    border-radius: 8px;
    padding: 2px 8px;
    font-weight: bold;
}

</style>
