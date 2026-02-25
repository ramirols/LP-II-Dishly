function abrirEditar(btn) {
    document.getElementById('tituloModalCat').textContent = 'Editar Categoría';
    document.getElementById('idcategoria').value = btn.dataset.idcat;
    document.getElementById('nombreCat').value = btn.dataset.nombrecat;
    document.getElementById('descCat').value = btn.dataset.desc;
    document.getElementById('modalGuardarCategoria').classList.add('open');
}
function abrirEliminar(btn) {
    document.getElementById('idcategoriaEliminar').value = btn.dataset.idcat;
    document.getElementById('modalEliminarCategoria').classList.add('open');
}