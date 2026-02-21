
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