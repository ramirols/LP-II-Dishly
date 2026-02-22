/* Platos */
function abrirModalAgregar(){
	document.getElementById("idplato").value = "";
	document.getElementById("plato").value = "";
	document.getElementById("precio").value = "";
	document.getElementById("imagen").value = "";
	document.getElementById("categorias").value = "";

	document.getElementById("modalGuardar").style.display = "block";
}

function abrirModalEditar(btn){
    document.getElementById("idplato").value = btn.dataset.id;
    document.getElementById("plato").value = btn.dataset.nombre;
    document.getElementById("precio").value = btn.dataset.precio;
    document.getElementById("imagen").value = btn.dataset.imgurl;
    document.getElementById("categorias").value = btn.dataset.idcategoria;

    document.getElementById("modalGuardar").style.display = "block";
}

function cerrarModalEditar(){
    document.getElementById("idplato").value = "";
    document.getElementById("plato").value = "";
    document.getElementById("precio").value = "";
    document.getElementById("imagen").value = "";
    document.getElementById("categorias").value = "";

    document.getElementById("modalGuardar").style.display = "none";
}

function abrirModalEliminar(btn){
    document.getElementById("platoEliminar").value = btn.dataset.id;

    document.getElementById("modalEliminar").style.display = "block";
}

function cerrarModalEliminar(){
    document.getElementById("platoEliminar").value = "";

    document.getElementById("modalEliminar").style.display = "none";
}

/* Categorias */

function abrirModalCategoria(){
    document.getElementById("modalCategoria").style.display = "block";
}
function cerrarModalCategoria(){
    document.getElementById("modalCategoria").style.display = "none";
}

function abrirModalAgregarCategoria(){
	document.getElementById("idcategoria").value = "";
	document.getElementById("categoria").value = "";
	document.getElementById("categoriaDescripcion").value = "";

	document.getElementById("modalGuardarCategoria").style.display = "block";
}

function abrirModalEditarCategoria(btn){
    document.getElementById("idcategoria").value = btn.dataset.idcat;
    document.getElementById("categoria").value = btn.dataset.nombrecat;
    document.getElementById("categoriaDescripcion").value = btn.dataset.desc;

    document.getElementById("modalGuardarCategoria").style.display = "block";
}

function cerrarModalEditarCategoria(){
	document.getElementById("idcategoria").value = "";
	document.getElementById("categoria").value = "";
	document.getElementById("categoriaDescripcion").value = "";

	document.getElementById("modalGuardarCategoria").style.display = "none";
}

function abrirModalEliminarCategoria(btn){
    document.getElementById("categoriaEliminar").value = btn.dataset.idcat;

    document.getElementById("modalEliminarCategoria").style.display = "block";
}

function cerrarModalEliminarCategoria(){
    document.getElementById("categoriaEliminar").value = "";

    document.getElementById("modalEliminarCategoria").style.display = "none";
}
