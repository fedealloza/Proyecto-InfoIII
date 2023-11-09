class Nodo {
    Producto producto;
    Nodo izquierdo, derecho;
    int altura;

    public Nodo(Producto producto) {
        this.producto = producto;
        izquierdo = derecho = null;
        altura = 1;
    }
}
