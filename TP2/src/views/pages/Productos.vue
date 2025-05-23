<script setup>
import { ref, onMounted } from 'vue';
import { useToast } from 'primevue/usetoast';
import { ProductService } from '@/service/ProductService'; // lo usarás para integrar tu API local

const toast = useToast();
const dt = ref();
const products = ref([]);
const productDialog = ref(false);
const deleteProductDialog = ref(false);
const deleteProductsDialog = ref(false);
const product = ref({ id: '', nombre: '', precio: 0, descripcion: '' });
const selectedProducts = ref([]);
const submitted = ref(false);
const productos = ref([
    {
        id: 1,
        nombre: 'Producto A',
        descripcion: 'Este es el producto A con características básicas.',
        precio: 1200000
    },
    {
        id: 2,
        nombre: 'Producto B',
        descripcion: 'Producto B pensado para usuarios avanzados.',
        precio: 350000
    },
    {
        id: 3,
        nombre: 'Producto C',
        descripcion: 'Una opción económica y funcional.',
        precio: 98000
    }
])

onMounted(() => {
    // Aquí puedes llamar a tu API cuando la integres
    ProductService.getProducts().then((data) => (products.value = data));

});

function openNew() {
    product.value = { id: '', nombre: '', precio: 0, descripcion: '' };
    submitted.value = false;
    productDialog.value = true;
}

function hideDialog() {
    productDialog.value = false;
    submitted.value = false;
}

function saveProduct() {
    submitted.value = true;

    // Validamos que nombre no esté vacío
    if (product.value.nombre && product.value.nombre.trim() !== '') {
        if (product.value.id) {
            // Editar producto existente
            const index = products.value.findIndex(p => p.id === product.value.id);
            if (index !== -1) {
                products.value[index] = { ...product.value };
                toast.add({ severity: 'success', summary: 'Éxito', detail: 'Producto actualizado', life: 3000 });
            }
        } else {
            // Crear nuevo producto
            product.value.id = createId();
            products.value.push({ ...product.value });
            toast.add({ severity: 'success', summary: 'Éxito', detail: 'Producto creado', life: 3000 });
        }
        productDialog.value = false;
        product.value = { id: '', nombre: '', precio: 0, descripcion: '' };
    } else {
        toast.add({ severity: 'error', summary: 'Error', detail: 'El nombre es obligatorio', life: 3000 });
    }
}

function editProduct(prod) {
    product.value = { ...prod };
    productDialog.value = true;
}

function confirmDeleteProduct(prod) {
    product.value = { ...prod };
    deleteProductDialog.value = true;
}

function deleteProduct() {
    products.value = products.value.filter(p => p.id !== product.value.id);
    deleteProductDialog.value = false;
    product.value = { id: '', nombre: '', precio: 0, descripcion: '' };
    toast.add({ severity: 'success', summary: 'Éxito', detail: 'Producto eliminado', life: 3000 });
}

function confirmDeleteSelected() {
    deleteProductsDialog.value = true;
}

function deleteSelectedProducts() {
    products.value = products.value.filter(p => !selectedProducts.value.includes(p));
    deleteProductsDialog.value = false;
    selectedProducts.value = [];
    toast.add({ severity: 'success', summary: 'Éxito', detail: 'Productos eliminados', life: 3000 });
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
                    <Button label="Eliminar" icon="pi pi-trash" severity="secondary" @click="confirmDeleteSelected" :disabled="!selectedProducts.length" />
                </template>
                <template #end>
                    <Button label="Exportar" icon="pi pi-upload" severity="secondary" @click="dt.exportCSV()" />
                </template>
            </Toolbar>

            <DataTable
                ref="dt"
                v-model:selection="selectedProducts"
                :value="productos"
                dataKey="id"
                :paginator="true"
                :rows="10"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                :rowsPerPageOptions="[5, 10, 25]"
                currentPageReportTemplate="Mostrando {first} a {last} de {totalRecords} productos"
            >
                <Column selectionMode="multiple" style="width: 3rem" :exportable="false"></Column>
                <Column field="id" header="ID" sortable style="min-width: 6rem"></Column>
                <Column field="nombre" header="Nombre" sortable style="min-width: 16rem"></Column>
                <Column field="descripcion" header="Descripción" style="min-width: 20rem"></Column>
                <Column field="precio" header="Precio" :body="(data) => formatoGs(data.precio)"></Column>
                <Column :exportable="false" style="min-width: 12rem">
                    <template #body="slotProps">
                        <Button icon="pi pi-pencil" outlined rounded class="mr-2" @click="editProduct(slotProps.data)" />
                        <Button icon="pi pi-trash" outlined rounded severity="danger" @click="confirmDeleteProduct(slotProps.data)" />
                    </template>
                </Column>
            </DataTable>
        </div>

        <Dialog v-model:visible="productDialog" :style="{ width: '450px' }" header="Detalles del Producto" :modal="true">
            <div class="flex flex-col gap-6">
                <div>
                    <label for="nombre" class="block font-bold mb-3">Nombre</label>
                    <InputText id="nombre" v-model.trim="product.nombre" autofocus :invalid="submitted && !product.nombre" fluid />
                    <small v-if="submitted && !product.nombre" class="text-red-500">El nombre es obligatorio.</small>
                </div>

                <div>
                    <label for="descripcion" class="block font-bold mb-3">Descripción</label>
                    <Textarea id="descripcion" v-model="product.descripcion" rows="3" cols="20" fluid />
                </div>

                <div>
                    <label for="precio" class="block font-bold mb-3">Precio</label>
                    <InputNumber
                        id="precio"
                        v-model="product.precio"
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
                <Button label="Guardar" icon="pi pi-check" @click="saveProduct" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteProductDialog" :style="{ width: '450px' }" header="Confirmar" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle !text-3xl" />
                <span v-if="product">¿Estás seguro que quieres eliminar <b>{{ product.nombre }}</b>?</span>
            </div>
            <template #footer>
                <Button label="No" icon="pi pi-times" text @click="deleteProductDialog = false" />
                <Button label="Sí" icon="pi pi-check" @click="deleteProduct" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteProductsDialog" :style="{ width: '450px' }" header="Confirmar" :modal="true">
            <div class="flex items-center gap-4">
                <i class="pi pi-exclamation-triangle !text-3xl" />
                <span>¿Estás seguro que quieres eliminar los productos seleccionados?</span>
            </div>
            <template #footer>
                <Button label="No" icon="pi pi-times" text @click="deleteProductsDialog = false" />
                <Button label="Sí" icon="pi pi-check" text @click="deleteSelectedProducts" />
            </template>
        </Dialog>
    </div>
</template>
