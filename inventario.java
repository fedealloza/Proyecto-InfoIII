import java.util.ArrayList;
import java.util.InputMismatchException;
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

public class inventario {
    private Nodo raiz;
    private List<Producto> listaProductos;

    public inventario() {
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
        raiz = insertarRec(raiz, producto); // Llama al método recursivo insertarRec con la raíz del árbol y el producto
                                            // a insertar
    }

    // Método recursivo para insertar un producto en el árbol
    private Nodo insertarRec(Nodo nodo, Producto producto) {
        if (nodo == null) {
            return new Nodo(producto); // Crea un nuevo nodo con el producto si el nodo actual es nulo
        }
        if (producto.nombre.compareTo(nodo.producto.nombre) < 0) {
            nodo.izquierdo = insertarRec(nodo.izquierdo, producto); // Inserta en el subárbol izquierdo si el nombre es
                                                                    // menor
        } else if (producto.nombre.compareTo(nodo.producto.nombre) > 0) {
            nodo.derecho = insertarRec(nodo.derecho, producto); // Inserta en el subárbol derecho si el nombre es mayor
        } else {
            // Si el producto ya existe, actualiza la cantidad
            nodo.producto.cantidad += producto.cantidad;
        }
        nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho)); // Actualiza la altura del nodo
        int equilibrio = obtenerEquilibrio(nodo); // Calcula el equilibrio del nodo
        // Casos de rotación para restaurar el equilibrio
        if (equilibrio > 1) {
            if (producto.nombre.compareTo(nodo.izquierdo.producto.nombre) < 0) {
                return rotarDerecha(nodo); // Rotación a la derecha
            } else {
                nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
                return rotarDerecha(nodo); // Rotación a la derecha después de una rotación a la izquierda
            }
        }
        if (equilibrio < -1) {
            if (producto.nombre.compareTo(nodo.derecho.producto.nombre) > 0) {
                return rotarIzquierda(nodo); // Rotación a la izquierda
            } else {
                nodo.derecho = rotarDerecha(nodo.derecho);
                return rotarIzquierda(nodo); // Rotación a la izquierda después de una rotación a la derecha
            }
        }
        return nodo; // Devuelve el nodo actual, que puede haber cambiado debido a las operaciones de
                     // inserción y rotación
    }

    // Método para eliminar un producto del árbol
    public void eliminar(String nombre) throws Exception {
        Scanner var = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a eliminar: ");
        int cantidadEliminar = var.nextInt();
        raiz = eliminarRec(raiz, nombre, cantidadEliminar); // Llama al método recursivo eliminarRec con la raíz del
                                                            // árbol
    }

    // Método recursivo para eliminar un producto del árbol
    private Nodo eliminarRec(Nodo nodo, String nombre, int cantidadEliminar) throws Exception {
        int tmpCantidad = nodo.producto.cantidad;
        if (nodo == null) {
            throw new Exception("El producto no se encuentra en el árbol.");
        }
        if (nombre.compareTo(nodo.producto.nombre) < 0) {
            nodo.izquierdo = eliminarRec(nodo.izquierdo, nombre, cantidadEliminar); // Busca en el subárbol izquierdo
        } else if (nombre.compareTo(nodo.producto.nombre) > 0) {
            nodo.derecho = eliminarRec(nodo.derecho, nombre, cantidadEliminar); // Busca en el subárbol derecho
        } else {
            // Si el producto existe, se descuenta la cantidad
            tmpCantidad -= cantidadEliminar;
            if (tmpCantidad == 0) {
                nodo.producto.cantidad -= cantidadEliminar;
                // Si la cantidad llega a cero, se elimina el nodo
                if (nodo.izquierdo == null) {
                    return nodo.derecho; // Reemplaza el nodo por su hijo derecho
                } else if (nodo.derecho == null) {
                    return nodo.izquierdo; // Reemplaza el nodo por su hijo izquier mdo
                }
                Nodo sucesor = encontrarSucesor(nodo.derecho); // Encuentra el sucesor en el subárbol derecho
                nodo.producto = sucesor.producto; // Copia el sucesor al nodo actual
                nodo.derecho = eliminarRec(nodo.derecho, sucesor.producto.nombre, cantidadEliminar); // Elimina el
                                                                                                     // sucesor
            } else if (tmpCantidad < 0) {
                System.out.println(
                        "La cantidad ingresada es mayor a la que se encuentra disponible en stock.\nLa cantidad disponible de "
                                + nodo.producto.nombre + " es " + nodo.producto.cantidad);
            }
        }
        nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));
        int equilibrio = obtenerEquilibrio(nodo); // Calcula el equilibrio del nodo
        // Casos de rotación para restaurar el equilibrio
        if (equilibrio > 1) {
            if (nombre.compareTo(nodo.izquierdo.producto.nombre) < 0) {
                return rotarDerecha(nodo); // Rotación a la derecha
            } else {
                nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
                return rotarDerecha(nodo); // Rotación a la derecha después de una rotación a la izquierda
            }
        }
        if (equilibrio < -1) {
            if (nombre.compareTo(nodo.derecho.producto.nombre) > 0) {
                return rotarIzquierda(nodo); // Rotación a la izquierda
            } else {
                nodo.derecho = rotarDerecha(nodo.derecho);
                return rotarIzquierda(nodo); // Rotación a la izquierda después de una rotación a la derecha
            }
        }
        return nodo; // Devuelve el nodo actual, que puede haber cambiado debido a las operaciones de
                     // eliminación y rotación
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
        return buscarRec(raiz, nombre); // Llama al método recursivo buscarRec con la raíz del árbol y el nombre del
                                        // producto a buscar
    }

    // Método recursivo para buscar un producto en el árbol
    private Producto buscarRec(Nodo nodo, String nombre) throws Exception {
        if (nodo == null) {
            throw new Exception("El producto no se encuentra en el árbol.");
        }
        if (nombre.equals(nodo.producto.nombre)) {
            System.out
                    .println("Producto encontrado: " + nodo.producto.nombre + ", Cantidad: " + nodo.producto.cantidad);
            return nodo.producto; // El producto se ha encontrado en el nodo actual
        }
        if (nombre.compareTo(nodo.producto.nombre) < 0) {
            return buscarRec(nodo.izquierdo, nombre); // Busca en el subárbol izquierdo si el nombre es menor
        }
        return buscarRec(nodo.derecho, nombre); // Busca en el subárbol derecho si el nombre es mayor
    }

    public static void main(String[] args) {
        inventario arbol = new inventario();
        Scanner var = new Scanner(System.in);
        int opcion;
        do {
            // Mostrar opciones al usuario
            System.out.println("\n1- Agregar Producto.");
            System.out.println("2- Eliminar Producto.");
            System.out.println("3- Buscar Producto.");
            System.out.println("4- Mostrar Inventario.");
            System.out.println("0- Salir.");
            try {
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
            } catch (InputMismatchException e) {
                System.out.println("ERROR: Debe ingresar un número entero para seleccionar una opción.");
                var.nextLine(); // Consumir la entrada inválida
                opcion = -1; // Establecer una opción inválida para continuar el bucle
            }
        } while (opcion != 0);
    }
}
