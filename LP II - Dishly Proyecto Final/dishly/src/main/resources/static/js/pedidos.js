function filtrarPedidos() {
    // 1. Obtener el valor de búsqueda y pasarlo a minúsculas
    const input = document.getElementById("inputBusqueda");
    const filtro = input.value.toLowerCase();
    
    // 2. Obtener todas las filas de la tabla (excepto el encabezado)
    const tabla = document.querySelector("table tbody");
    const filas = tabla.getElementsByTagName("tr");

    // 3. Recorrer cada fila
    for (let i = 0; i < filas.length; i++) {
        const fila = filas[i];

        if (fila.innerText.includes("No hay pedidos registrados")) continue;

        const textoFila = fila.innerText.toLowerCase();

        // 4. Mostrar u ocultar según coincidencia
        if (textoFila.indexOf(filtro) > -1) {
            fila.style.display = "";
        } else {
            fila.style.display = "none";
        }
    }
}