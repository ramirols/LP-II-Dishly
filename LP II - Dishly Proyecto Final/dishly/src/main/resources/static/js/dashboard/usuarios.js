// Agregar Staff
document.getElementById('btnAgregarStaff').addEventListener('click', function() {
    document.getElementById('modalAgregar').classList.add('open');
});

// Editar Staff — lee los data-* del boton
document.querySelectorAll('.btn-editar').forEach(function(btn) {
    btn.addEventListener('click', function() {
        document.getElementById('editarId').value     = this.dataset.id;
        document.getElementById('editarNombre').value = this.dataset.nombre;
        document.getElementById('editarEmail').value  = this.dataset.email;
        document.getElementById('editarSubtitulo').textContent = 'Editando: ' + this.dataset.nombre;
        document.getElementById('modalEditar').classList.add('open');
    });
});

function cerrarModal(id) {
    document.getElementById(id).classList.remove('open');
}
function cerrarSiOverlay(event, id) {
    if (event.target === document.getElementById(id)) cerrarModal(id);
}