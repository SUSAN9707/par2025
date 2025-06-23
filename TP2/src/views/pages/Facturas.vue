<script setup>
import {ref, onMounted, watch, computed} from 'vue';
import { useToast } from "primevue/usetoast";
import { crearFactura, getFacturas ,anularFactura} from "@/service/facturas.service";
import { getProveedores } from "@/service/proveedor.service";
import { getClientes } from "@/service/cliente.service";
import { getProductos } from "@/service/producto.service";

const toast = useToast();
const facturas = ref([]);
const proveedores = ref([]);
const clientes = ref([]);
const productos = ref([]);
const proveedorSeleccionado = ref(null);
const clienteSeleccionado = ref(null);
const productoSeleccionado = ref(null);
const productosAgregados = ref([]);

const tipoFacturaSeleccionado = ref(null);
const facturaDialog = ref(false);
const deleteFacturaDialog = ref(false);
const facturaSeleccionada = ref(null);

const factura = ref({
    numero: '',
    total: 0,
    tipoFactura: 'COMPRA',
    idClienteProv: null,
    formaPago: 'EFECTIVO',
});
const tiposFactura =ref( [
    { label: 'Compra', value: 'COMPRA' },
    { label: 'Venta', value: 'VENTA' }
]);

const formasPago = ref([
    { label: 'Efectivo', value: 'EFECTIVO' },
    { label: 'Crédito', value: 'CREDITO' },
    { label: 'Débito', value: 'DEBITO' }
]);

watch(tipoFacturaSeleccionado, async (nuevo) => {
    if (nuevo === 'COMPRA') {
        const response = await getProveedores();
        proveedores.value = response.data.data;
    } else if (nuevo === 'VENTA') {
        const response = await getClientes();
        clientes.value = response.data.data;
    }
});

onMounted(() => {
    cargarFacturas();
});

async function cargarFacturas() {
    try {
        const response = await getFacturas();
        facturas.value = response.data.data;
    } catch (error) {
        toast.add({ severity: 'error', summary: 'Error', detail: 'Error al cargar facturas', life: 3000 });
    }
}

async function cargarProductos() {
    const res = await getProductos();
    // Solo mostrar productos con stock > 0
    productos.value = res.data.data.filter(p => p.stock > 0);
}


function openNew() {
    factura.value = {
        numero: generarNumero(),
        total: 0,
        tipoFactura: 'COMPRA',
        idClienteProv: null,
        formaPago: 'EFECTIVO',
    };
    proveedorSeleccionado.value = null;
    clienteSeleccionado.value = null;
    productoSeleccionado.value = null;
    productosAgregados.value = [];
    tipoFacturaSeleccionado.value = 'COMPRA';
    facturaDialog.value = true;
    cargarProductos();
}
function formatPrecio(valor) {
    return new Intl.NumberFormat('es-PY', {
        style: 'currency',
        currency: 'PYG',
        minimumFractionDigits: 0
    }).format(valor);
}
function agregarProducto() {
    if (!productoSeleccionado.value) return;

    const yaAgregado = productosAgregados.value.find(p => p.productoId === productoSeleccionado.value.id);
    if (yaAgregado) {
        toast.add({ severity: 'warn', summary: 'Aviso', detail: 'Producto ya agregado', life: 2500 });
        return;
    }

    const nuevo = {
        productoId: productoSeleccionado.value.id,
        cantidad: 1,
        precioUnitario: productoSeleccionado.value.precio,
        totalPorArticulo: productoSeleccionado.value.precio,
        nombre: productoSeleccionado.value.nombre, // nuevo campo
    };
    productosAgregados.value.push(nuevo);
    actualizarTotal();
}
async function anularFacturaConfirmada() {
    try {
        await anularFactura(facturaSeleccionada.value.id);
        toast.add({
            severity: 'success',
            summary: 'Éxito',
            detail: 'Factura anulada correctamente',
            life: 3000
        });

        deleteFacturaDialog.value = false;
        facturaSeleccionada.value = null;

        const response = await getFacturas();
        facturas.value = response.data.data;

    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.mensaje || 'Error al anular la factura',
            life: 3000
        });
    }
}

function eliminarProducto(index) {
    productosAgregados.value.splice(index, 1);
    actualizarTotal();
}
function confirmarAnularFactura(factura) {
    facturaSeleccionada.value = factura;
    deleteFacturaDialog.value = true;
}

function actualizarTotal() {
    factura.value.total = productosAgregados.value.reduce((acc, p) => acc + (p.precioUnitario * p.cantidad), 0);
}
const formularioInvalido = computed(() => {
    const sinProveedor = tipoFacturaSeleccionado.value === 'COMPRA' && !proveedorSeleccionado.value;
    const sinCliente = tipoFacturaSeleccionado.value === 'VENTA' && !clienteSeleccionado.value;
    const sinProductos = productosAgregados.value.length === 0;
    const sinFormaPago = !factura.value.formaPago;
    const sinTipoFactura = !tipoFacturaSeleccionado.value;
    const sinNumero = !factura.value.numero || factura.value.numero.trim() === '';
    const totalInvalido = !factura.value.total || factura.value.total <= 0;

    return (
        sinProveedor ||
        sinCliente ||
        sinProductos ||
        sinFormaPago ||
        sinTipoFactura ||
        sinNumero ||
        totalInvalido
    );
});

function generarNumero() {
    const numeros = facturas.value
        .map(f => f.numero)
        .filter(num => /^F-\d+$/.test(num))
        .map(num => parseInt(num.split('-')[1]));

    const siguiente = Math.max(0, ...numeros) + 1;
    return `F-${siguiente.toString().padStart(4, '0')}`;
}

async function saveFactura() {
    try {
        const cuerpo = {
            numero: factura.value.numero || generarNumero(),
            total: factura.value.total,
            idClienteProv:
                tipoFacturaSeleccionado.value === 'COMPRA'
                    ? proveedorSeleccionado.value?.id ?? null
                    : clienteSeleccionado.value?.id ?? null,
            tipoFactura: tipoFacturaSeleccionado.value,
            formaPago: factura.value.formaPago,
            compras: tipoFacturaSeleccionado.value === 'COMPRA' ? productosAgregados.value : [],
            ventas: tipoFacturaSeleccionado.value === 'VENTA' ? productosAgregados.value : []
        };

        await crearFactura(cuerpo);
        toast.add({ severity: 'success', summary: 'Éxito', detail: 'Factura creada exitosamente', life: 3000 });
        facturaDialog.value = false;
        cargarFacturas();
    } catch (error) {
        toast.add({
            severity: 'error',
            summary: 'Error',
            detail: error.response?.data?.mensaje || 'Error al crear factura',
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

            <DataTable ref="dt" :value="facturas" dataKey="id">
                <Column field="id" header="ID" sortable style="min-width: 6rem" />
                <Column field="numero" header="Número" sortable style="min-width: 10rem" />
                <Column header="Total" style="min-width: 10rem">
                    <template #body="slotProps">
                        {{ formatPrecio(slotProps.data.total) }}
                    </template>
                </Column>
                <Column field="idClienteProv" header="ID Cliente/Proveedor" sortable style="min-width: 14rem" />
                <Column field="tipoFactura" header="Tipo Factura" sortable style="min-width: 12rem" />
                <Column field="estado" header="Estado" sortable style="min-width: 10rem" />
                <Column field="fecha" header="Fecha" sortable style="min-width: 14rem">
                    <template #body="slotProps">
                        {{ new Date(slotProps.data.fecha).toLocaleString() }}
                    </template>
                </Column>
                <Column field="formaPago" header="Forma de Pago" sortable style="min-width: 12rem" />
                <Column :exportable="false" style="min-width: 12rem">
                    <template #body="slotProps">
                        <Button
                            v-if="slotProps.data.estado !== 'ANULADA'"
                            icon="pi pi-times"
                            outlined
                            rounded
                            severity="danger"
                            @click="confirmarAnularFactura(slotProps.data)"
                            title="Anular factura"
                        />

                    </template>
                </Column>
            </DataTable>

        </div>
        <Dialog v-model:visible="deleteFacturaDialog" header="Confirmar" modal :closable="false" :style="{ width: '350px' }">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem;"></i>
                <span>¿Está seguro que desea anular la factura <b>{{ facturaSeleccionada.numero }}</b>?</span>
            </div>

            <template #footer>
                <Button label="No" icon="pi pi-times" outlined @click="deleteFacturaDialog = false" />
                <Button label="Sí" icon="pi pi-check" severity="danger" @click="anularFacturaConfirmada" />
            </template>
        </Dialog>

        <Dialog  :style="{ width: '75vw' }" v-model:visible="facturaDialog" header="Nueva Factura" modal>
            <div class="p-fluid">
                <label class="block mb-2 font-bold">Número:</label>
                <InputText class="w-full" v-model="factura.numero" disabled />

                <div class="field mt-3">
                    <label class="block mb-2 font-bold">Tipo de Factura:</label>
                    <Dropdown
                        v-model="tipoFacturaSeleccionado"
                        :options="tiposFactura"
                        optionLabel="label"
                        optionValue="value"
                        placeholder="Seleccionar tipo"
                        class="w-full mb-5"
                    />

                </div>


                <div class="field" v-if="tipoFacturaSeleccionado === 'VENTA'">
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
                        class="w-full mb-5"
                    />
                </div>

                <div class="field" v-if="tipoFacturaSeleccionado === 'COMPRA'">
                    <label for="idProveedor" class="block mb-2 font-bold">Proveedor (RUC):</label>
                    <Dropdown
                        id="idProveedor"
                        v-model="proveedorSeleccionado"
                        :options="proveedores"
                        optionLabel="ruc"
                        :optionValue="proveedor => proveedor"
                        filter
                        filterBy="ruc"
                        placeholder="Seleccione un proveedor"
                        class="w-full mb-5"
                    />
                </div>

                <label class="block mb-2 font-bold">Forma de Pago:</label>
                <Dropdown
                    v-model="factura.formaPago"
                    :options="formasPago"
                    optionLabel="label"
                    optionValue="value"
                    placeholder="Seleccione forma de pago"
                    class="w-full mb-5"
                />
                <label class="block mb-2 font-bold">Agregar Producto:</label>
                <div class="field w-full">
                    <div class="flex items-center gap-3">
                        <Dropdown class="w-2/3" v-model="productoSeleccionado" :options="productos" optionLabel="nombre" placeholder="Seleccionar producto" />
                        <Button class="w-1/4" label="Agregar" icon="pi pi-plus" @click="agregarProducto" />
                    </div>
                </div>


                <div class="w-full mt-5" v-if="productosAgregados.length">
                    <h4>Productos agregados:</h4>
                    <ul>
                        <li v-for="(prod, idx) in productosAgregados" :key="prod.productoId">
                            <strong>{{ prod.nombre }}</strong> -
                            Cant:
                            <InputNumber
                                v-model="prod.cantidad"
                                :min="1"
                                :step="1"
                                showButtons
                                buttonLayout="horizontal"
                                incrementButtonIcon="pi pi-plus"
                                decrementButtonIcon="pi pi-minus"
                                @update:modelValue="() => {
                                    prod.totalPorArticulo = prod.cantidad * prod.precioUnitario;
                                    actualizarTotal();
                                  }"
                            />
                            - Precio:
                            <InputNumber disabled
                                         v-model="prod.precioUnitario"
                                         @update:modelValue="() => {
                                            prod.totalPorArticulo = prod.cantidad * prod.precioUnitario;
                                            actualizarTotal();
                                          }"
                            />
                            - Total: {{ prod.totalPorArticulo.toLocaleString() }}
                            <Button icon="pi pi-times" @click="eliminarProducto(idx)" severity="danger" text />
                        </li>
                    </ul>

                </div>
                <label class="block mb-2 mt-2 font-bold">Total:</label>
                <InputNumber class="w-full" v-model="factura.total" :disabled="true" />

                <div class="mt-3 flex justify-end">
                    <Button :disabled="formularioInvalido" label="Guardar" icon="pi pi-check" @click="saveFactura" />
                    <Button label="Cancelar" icon="pi pi-times" severity="secondary" class="ml-2" @click="facturaDialog = false" />
                </div>
            </div>
        </Dialog>
    </div>
</template>

<style scoped>
ul {
    list-style: none;
    padding: 0;
}
li {
    margin-bottom: 0.75rem;
}
</style>
