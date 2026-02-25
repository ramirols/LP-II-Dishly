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