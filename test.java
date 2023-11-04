import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Producto {
    int cantidad;
    String nombre;

    public Producto(String nombre, int cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }
}

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

public class test {
    private Nodo raiz;
    private List<Producto> listaProductos;

    public test() {
        raiz = null;
        listaProductos = new ArrayList<>();
    }

    private int altura(Nodo nodo) {
        if (nodo == null) {
            return 0;
        }
        return nodo.altura;
    }

    private int obtenerEquilibrio(Nodo nodo) {
        if (nodo == null) {
            return 0;
        }
        return altura(nodo.izquierdo) - altura(nodo.derecho);
    }

    // Rotación a la derecha
    private Nodo rotarDerecha(Nodo y) {
        Nodo x = y.izquierdo;
        Nodo T2 = x.derecho;
        x.derecho = y;
        y.izquierdo = T2;
        y.altura = Math.max(altura(y.izquierdo), altura(y.derecho)) + 1;
        x.altura = Math.max(altura(x.izquierdo), altura(x.derecho)) + 1;
        return x;
    }

    // Rotación a la izquierda
    private Nodo rotarIzquierda(Nodo x) {
        Nodo y = x.derecho;
        Nodo T2 = y.izquierdo;
        y.izquierdo = x;
        x.derecho = T2;
        x.altura = Math.max(altura(x.izquierdo), altura(x.derecho)) + 1;
        y.altura = Math.max(altura(y.izquierdo), altura(y.derecho)) + 1;
        return y;
    }

    // Método para insertar un producto en el árbol
    public void insertar(Producto producto) {
        raiz = insertarRec(raiz, producto);
    }

    // Método recursivo para insertar un producto en el árbol
    private Nodo insertarRec(Nodo nodo, Producto producto) {
        if (nodo == null) {
            return new Nodo(producto);
        }
        if (producto.nombre.compareTo(nodo.producto.nombre) < 0) {
            nodo.izquierdo = insertarRec(nodo.izquierdo, producto);
        } else if (producto.nombre.compareTo(nodo.producto.nombre) > 0) {
            nodo.derecho = insertarRec(nodo.derecho, producto);
        } else {
            // Si el producto ya existe, actualiza la cantidad
            nodo.producto.cantidad += producto.cantidad;
        }
        nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));
        int equilibrio = obtenerEquilibrio(nodo);
        // Casos de rotación
        if (equilibrio > 1) {
            if (producto.nombre.compareTo(nodo.izquierdo.producto.nombre) < 0) {
                return rotarDerecha(nodo);
            } else {
                nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
                return rotarDerecha(nodo);
            }
        }
        if (equilibrio < -1) {
            if (producto.nombre.compareTo(nodo.derecho.producto.nombre) > 0) {
                return rotarIzquierda(nodo);
            } else {
                nodo.derecho = rotarDerecha(nodo.derecho);
                return rotarIzquierda(nodo);
            }
        }
        return nodo;
    }

    // Método para eliminar un producto del árbol
    public void eliminar(String nombre) throws Exception {
        Scanner var = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a eliminar: ");
        int cantidadEliminar = var.nextInt();
        raiz = eliminarRec(raiz, nombre, cantidadEliminar);
    }

    // Método recursivo para eliminar un producto del árbol
    private Nodo eliminarRec(Nodo nodo, String nombre, int cantidadEliminar) throws Exception {
        if (nodo == null) {
            throw new Exception("El producto no se encuentra en el árbol.");
        }
        if (nombre.compareTo(nodo.producto.nombre) < 0) {
            nodo.izquierdo = eliminarRec(nodo.izquierdo, nombre, cantidadEliminar);
        } else if (nombre.compareTo(nodo.producto.nombre) > 0) {
            nodo.derecho = eliminarRec(nodo.derecho, nombre, cantidadEliminar);
        } else {
            // Si el producto existe, se descuenta la cantidad
            nodo.producto.cantidad -= cantidadEliminar;
            if (nodo.producto.cantidad <= 0) {
                // Si la cantidad llega a cero o menos, se elimina el nodo
                if (nodo.izquierdo == null) {
                    return nodo.derecho;
                } else if (nodo.derecho == null) {
                    return nodo.izquierdo;
                }
                Nodo sucesor = encontrarSucesor(nodo.derecho);
                nodo.producto = sucesor.producto;
                nodo.derecho = eliminarRec(nodo.derecho, sucesor.producto.nombre, cantidadEliminar);
            }
        }
        nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));
        int equilibrio = obtenerEquilibrio(nodo);
        // Casos de rotación
        if (equilibrio > 1) {
            if (nombre.compareTo(nodo.izquierdo.producto.nombre) < 0) {
                return rotarDerecha(nodo);
            } else {
                nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
                return rotarDerecha(nodo);
            }
        }
        if (equilibrio < -1) {
            if (nombre.compareTo(nodo.derecho.producto.nombre) > 0) {
                return rotarIzquierda(nodo);
            } else {
                nodo.derecho = rotarDerecha(nodo.derecho);
                return rotarIzquierda(nodo);
            }
        }
        return nodo;
    }

    // Método para encontrar el sucesor de un nodo
    private Nodo encontrarSucesor(Nodo nodo) {
        Nodo actual = nodo;
        while (actual.izquierdo != null) {
            actual = actual.izquierdo;
        }
        return actual;
    }

    // Método para mostrar los productos en orden alfabético
    public void mostrarInorden() {
        listaProductos.clear(); // Limpia la lista antes de comenzar
        mostrarInordenRec(raiz);
    }

    // Método recursivo para recorrer el árbol en orden y almacenar los productos en
    // la lista
    private void mostrarInordenRec(Nodo nodo) {
        if (nodo != null) {
            mostrarInordenRec(nodo.izquierdo);
            listaProductos.add(nodo.producto);
            mostrarInordenRec(nodo.derecho);
        }
    }

    // Método para buscar un producto en el árbol
    public Producto buscar(String nombre) throws Exception {
        return buscarRec(raiz, nombre);
    }

    /*
     * - El método toma dos parámetros: nodo , que representa el nodo actual en el
     * que estamos buscando, y nombre , que es el nombre del producto que queremos
     * encontrar.
     * - Primero, verifica si el nodo actual es nulo o si el nombre del producto en
     * el nodo coincide con el nombre que estamos buscando.
     * - Si es así, se imprime un mensaje indicando que el producto ha sido
     * encontrado y se devuelve el objeto Producto correspondiente al nodo.
     * - Si el nodo actual no es nulo y el nombre del producto no coincide, se
     * realiza una comparación para determinar si debemos buscar en el subárbol
     * izquierdo o en el subárbol derecho.
     * - Si el nombre que estamos buscando es menor que el nombre del producto en el
     * nodo actual, llamamos al método buscarRec recursivamente pasando el subárbol
     * izquierdo como parámetro.
     * - Si el nombre que estamos buscando es mayor que el nombre del producto en el
     * nodo actual, llamamos al método buscarRec recursivamente pasando el subárbol
     * derecho como parámetro.
     * - Si no se encuentra el producto en el árbol, se imprime un mensaje indicando
     * que el producto no ha sido encontrado y se devuelve null .
     */
    // Método recursivo para buscar un producto en el árbol
    // Método recursivo para buscar un producto en el árbol
    private Producto buscarRec(Nodo nodo, String nombre) throws Exception {
        if (nodo == null) {
            throw new Exception("El producto no se encuentra en el árbol.");
        }
        if (nombre.equals(nodo.producto.nombre)) {
            System.out
                    .println("Producto encontrado: " + nodo.producto.nombre + ", Cantidad: " + nodo.producto.cantidad);
            return nodo.producto;
        }
        if (nombre.compareTo(nodo.producto.nombre) < 0) {
            return buscarRec(nodo.izquierdo, nombre);
        }
        return buscarRec(nodo.derecho, nombre);
    }

    public static void main(String[] args) {
        test arbol = new test();
        Scanner var = new Scanner(System.in);
        int opcion;
        do {
            // Mostrar opciones al usuario
            System.out.println("\n1- Agregar Producto.");
            System.out.println("2- Eliminar Producto.");
            System.out.println("3- Buscar Producto.");
            System.out.println("4- Mostrar Inventario.");
            System.out.println("0- Salir.");
            System.out.print("\nIngrese la opción: ");
            opcion = var.nextInt();
            var.nextLine(); // Consumir el salto de línea pendiente
            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nombre del producto: ");
                    String nombre = var.nextLine();
                    System.out.print("Ingrese la cantidad a ingresar: ");
                    int cantidad = var.nextInt();
                    arbol.insertar(new Producto(nombre, cantidad));
                    break;
                case 2:
                    try {
                        System.out.print("Ingrese el nombre del producto a eliminar: ");
                        String nombreEliminar = var.nextLine();
                        arbol.eliminar(nombreEliminar);
                    } catch (Exception e) {
                        System.out.println("ERROR: NO SE ENCONTRO EL PRODUCTO: " + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        System.out.print("Ingrese el nombre del producto a buscar: ");
                        String nombreBuscar = var.nextLine();
                        arbol.buscar(nombreBuscar);
                    } catch (Exception e) {
                        System.out.println("ERROR: NO EXISTE EL PRODUCTO EN EL INVENTARIO: " + e.getMessage());
                    }
                    break;
                case 4:
                    arbol.mostrarInorden();
                    System.out.println("\nLista de productos en orden alfabético:");
                    for (Producto producto : arbol.listaProductos) {
                        System.out.println("Producto: " + producto.nombre + ", Cantidad: " + producto.cantidad);
                    }
                    break;
            }
        } while (opcion != 0);
    }
}