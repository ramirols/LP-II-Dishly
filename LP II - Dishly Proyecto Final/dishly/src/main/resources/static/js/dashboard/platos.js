// ── Preview de imagen al seleccionar archivo ──────────────
// ── Preview de imagen al seleccionar archivo ──────────────
const inputFile = document.getElementById('imagenFile');
const uploadArea = document.getElementById('uploadArea');

inputFile.addEventListener('change', function () {
    mostrarPreview(this.files[0]);
});

// Drag & drop
uploadArea.addEventListener('dragover', function (e) {
    e.preventDefault();
    this.style.borderColor = '#6366f1';
    this.style.background = '#f5f3ff';
});
uploadArea.addEventListener('dragleave', function () {
    this.style.borderColor = '';
    this.style.background = '';
});
uploadArea.addEventListener('drop', function (e) {
    e.preventDefault();
    this.style.borderColor = '';
    this.style.background = '';
    const file = e.dataTransfer.files[0];
    if (file && file.type.startsWith('image/')) {
        inputFile.files = e.dataTransfer.files;
        mostrarPreview(file);
    }
});

function mostrarPreview(file) {
    if (!file) return;
    const reader = new FileReader();
    reader.onload = function (e) {
        const preview = document.getElementById('imgNuevaPreview');
        const holder = document.getElementById('uploadPlaceholder');
        preview.src = e.target.result;
        preview.classList.remove('hidden');
        holder.classList.add('hidden');
        document.getElementById('uploadArea').classList.add('has-image');
    };
    reader.readAsDataURL(file);
}

// ── Modal plato: Agregar ──────────────────────────────────
function abrirModalAgregar() {
    document.getElementById('modalGuardarTitulo').textContent = 'Agregar plato';
    document.getElementById('idplato').value = '';
    document.getElementById('platoNombre').value = '';
    document.getElementById('platoPrecio').value = '';
    document.getElementById('imagenUrlActual').value = '';
    document.getElementById('categorias').value = '';
    resetUploadArea();
    document.getElementById('previewActual').classList.add('hidden');
    document.getElementById('modalGuardar').classList.add('open');
}

// ── Modal plato: Editar ───────────────────────────────────
function abrirModalEditar(btn) {
    document.getElementById('modalGuardarTitulo').textContent = 'Editar plato';
    document.getElementById('idplato').value = btn.dataset.id;
    document.getElementById('platoNombre').value = btn.dataset.nombre;
    document.getElementById('platoPrecio').value = btn.dataset.precio;
    document.getElementById('categorias').value = btn.dataset.idcategoria;
    document.getElementById('imagenUrlActual').value = btn.dataset.imgurl || '';

    // Mostrar imagen actual si existe
    const imgUrl = btn.dataset.imgurl;
    if (imgUrl && imgUrl.trim() !== '' && imgUrl !== 'null') {
        document.getElementById('imgActualPreview').src = '/img/' + imgUrl;
        document.getElementById('previewActual').classList.remove('hidden');
    } else {
        document.getElementById('previewActual').classList.add('hidden');
    }

    resetUploadArea();
    document.getElementById('modalGuardar').classList.add('open');
}

function cerrarModalEditar() {
    document.getElementById('modalGuardar').classList.remove('open');
}

// ── Reset area de carga ───────────────────────────────────
function resetUploadArea() {
    document.getElementById('imagenFile').value = '';
    document.getElementById('imgNuevaPreview').classList.add('hidden');
    document.getElementById('imgNuevaPreview').src = '';
    document.getElementById('uploadPlaceholder').classList.remove('hidden');
    document.getElementById('uploadArea').classList.remove('has-image');
}

/* --- MODAL ELIMINAR PLATO --- */
function abrirModalEliminar(btn) {
    // Obtenemos el ID del atributo data-id del botón
    const id = btn.getAttribute('data-id'); 
    document.getElementById("platoEliminar").value = id;
    document.getElementById("modalEliminar").style.display = "flex";
}

function cerrarModalEliminar() {
    document.getElementById("modalEliminar").style.display = "none";
}

function filtrarPlatos() {
    const input = document.getElementById("inputBusqueda");
    const filter = input.value.toLowerCase();
    const table = document.querySelector("table tbody");
    const tr = table.getElementsByTagName("tr");

    for (let i = 0; i < tr.length; i++) {
        const td = tr[i].getElementsByTagName("td")[1];
        if (td) {
            const txtValue = td.textContent || td.innerText;
            if (txtValue.toLowerCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}


const precioInput = document.getElementById('platoPrecio');


precioInput.addEventListener('keydown', function(e) {
    if (e.key === '-' || e.key === 'e') {
        e.preventDefault();
    }
});

precioInput.addEventListener('input', function() {
    if (this.value < 0) {
        this.value = 0;
    }
});

document.querySelector('#modalGuardar form').addEventListener('submit', function(e) {
    const valor = parseFloat(precioInput.value);
    if (valor < 0 || isNaN(valor)) {
        e.preventDefault();
        alert("Por favor, ingrese un precio válido (no negativo).");
        precioInput.focus();
    }
});